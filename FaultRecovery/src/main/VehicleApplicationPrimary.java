package main;

import DataGenerator.DataGenerator;
import common.ClientCommunicationInterface;
import common.ProcessManagerInterface;
import common.ServerCommunicationInterface;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleApplicationPrimary extends UnicastRemoteObject implements ClientCommunicationInterface {

    private static ServerCommunicationInterface serverRef;
    private static ClientCommunicationInterface backupRef;
    private static ProcessManagerInterface pmanagerRef;

    private static Timer timer_heartbeat = new Timer();
    private static AtomicLong counter = new AtomicLong(0L);

    private boolean validCoordinates = true;

    private static Properties props;

    /**
     * This method loads the properties of application.
     */
    private static void loadProperties() {
        try {
            InputStream is;
            is = VehicleApplicationPrimary.class.getClassLoader().getResourceAsStream("application.properties");
            props = new Properties();
            props.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected VehicleApplicationPrimary() throws RemoteException {
        super();
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        try {
            loadProperties();

            String primaryPort = props.getProperty("vehicle.app.port1");
            String primaryReference = props.getProperty("primary.process.reference");


            Registry reg = LocateRegistry.createRegistry(Integer.parseInt(primaryPort));
            reg.rebind(primaryReference, new VehicleApplicationPrimary());


            String serverIp = props.getProperty("server.ip");
            String serverPort = props.getProperty("server.port");
            String serverReference = props.getProperty("server.reference");

            Registry registry = LocateRegistry.getRegistry(serverIp, Integer.parseInt(serverPort));
            serverRef = (ServerCommunicationInterface) registry.lookup(serverReference);


            String backupIp = props.getProperty("vehicle.app.ip");
            String backupPort = props.getProperty("vehicle.app.port2");
            String backupReference = props.getProperty("backup.process.reference");

            Registry registry1 = LocateRegistry.getRegistry(backupIp, Integer.parseInt(backupPort));
            backupRef = (ClientCommunicationInterface) registry1.lookup(backupReference);

            System.out.println("Primary process ready!");
        } catch (ConnectException e) {
            System.out.println("Server is not available!");
            System.exit(-1);
        } catch (Exception e) {
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

    }

    @Override
    public void collectData(double latitude, double longitude) throws RemoteException {
        long current = counter.longValue();
        //send data to server as well
        try {
            if (current == 0) {
                timer_heartbeat.schedule(new Heartbeat(serverRef, counter, 1), 0, 2000);
            }
            if (latitude > 80.0 || longitude < 10.0) {
                System.out.println("Error in critical process at counter " + current);
                this.validCoordinates = false;
                timer_heartbeat.cancel();
                System.exit(-1);
            } else {
                counter.getAndIncrement();
                backupRef.aliveStatus(new Date(), current);
                pmanagerRef.handleData(current, latitude, longitude);
            }
        } catch (Exception e) {
            timer_heartbeat.cancel();
            System.out.println("Exception occurred at " + current + "! Not sending heartbeat anymore!");
            e.printStackTrace();
        }

    }

    @Override
    public Date getPrimaryLastUpdated() throws RemoteException {
        return new Date();
    }

    @Override
    public void setActiveFlag(boolean b) throws RemoteException {

    }
}