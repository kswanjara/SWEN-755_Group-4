package main;

import DataGenerator.DataGenerator;
import common.ClientCommunicationInterface;
import common.ProcessManagerInterface;
import common.ServerCommunicationInterface;

import java.io.*;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicLong;

import static DataGenerator.DataGenerator.getCoordinates;

public class VehicleApplicationBackup extends UnicastRemoteObject implements ClientCommunicationInterface {

    private static ServerCommunicationInterface serverRef;
    private static Timer timer_heartbeat = new Timer();
    private static Properties props;

    private static boolean active = false;
    private static AtomicLong counter = new AtomicLong(0L);

    private static File file = new File("./CriticalData.txt");

    private long primaryProcess = counter.longValue();
    private Date lastUpdate;


    private static ProcessManagerInterface pmanagerRef;

    protected VehicleApplicationBackup() throws RemoteException {
        super();
    }

    /**
     * This method loads the properties of application.
     */
    private static void loadProperties() {
        try {
            InputStream is;
            is = VehicleApplicationBackup.class.getClassLoader().getResourceAsStream("application.properties");
            props = new Properties();
            props.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        try {
            loadProperties();

            String backupIp = props.getProperty("vehicle.app.ip");
            String backupPort = props.getProperty("vehicle.app.port2");

            String backupReference = props.getProperty("backup.process.reference");
            Registry reg = LocateRegistry.createRegistry(Integer.parseInt(backupPort));
            reg.rebind(backupReference, new VehicleApplicationBackup());


            String serverIp = props.getProperty("server.ip");
            String serverPort = props.getProperty("server.port");
            String serverReference = props.getProperty("server.reference");

            Registry registry = LocateRegistry.getRegistry(serverIp, Integer.parseInt(serverPort));
            serverRef = (ServerCommunicationInterface) registry.lookup(serverReference);

        } catch (ConnectException e) {
            System.out.println("Server is not available!");
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            timer_heartbeat.schedule(new Heartbeat(serverRef, counter, 0), 0, 400);
        } catch (Exception e) {
            timer_heartbeat.cancel();
            System.out.println("Exception occurred! Not sending heartbeat anymore!");
            e.printStackTrace();
        }
    }

    @Override
    public void processManagerUp() throws RemoteException, NotBoundException {
        String thirdCompIP = props.getProperty("third.component.ip");
        int thirdCompPort = Integer.parseInt(props.getProperty("third.component.port"));
        String thirdCompReference = props.getProperty("third.component.reference");

        Registry registry1 = LocateRegistry.getRegistry(thirdCompIP, thirdCompPort);
        pmanagerRef = (ProcessManagerInterface) registry1.lookup(thirdCompReference);

    }

    @Override
    public void aliveStatus(Date statusTime, long dataPointer) throws RemoteException {
        this.lastUpdate = statusTime;
        this.primaryProcess = dataPointer;
    }

    @Override
    public void collectData(double latitude, double longitude) throws IOException {
        long current = counter.longValue();
        if (!active) {
            counter.getAndIncrement();
            file.setWritable(true);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(current + "\tLatitude = " + latitude + "\tLongitude = " + longitude);
            writer.close();
        } else {
            //Send data to server
            counter.getAndIncrement();
            pmanagerRef.handleData(current, latitude, longitude);
        }
    }

    @Override
    public Date getPrimaryLastUpdated() throws RemoteException {
        return lastUpdate;
    }

    @Override
    public void setActiveFlag(boolean b) throws RemoteException {
        active = true;
    }
}
