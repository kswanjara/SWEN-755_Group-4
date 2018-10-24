package main;

import DataGenerator.DataGenerator;
import common.ClientCommunicationInterface;
import common.ServerCommunicationInterface;

import java.io.IOException;
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

import static DataGenerator.DataGenerator.getCoordinates;

public class VehicleApplicationPrimary extends UnicastRemoteObject implements ClientCommunicationInterface {

    private static ServerCommunicationInterface serverRef;
    private static ClientCommunicationInterface backupRef;
    private static Timer timer_heartbeat = new Timer();
    private static AtomicLong counter = new AtomicLong(0L);

    private static boolean validCoordinates = true;

    private static Properties props;

    /**
     * This method loads the properties of application.
     */
    private static void loadProperties() {
        try {
            props = new Properties();
            props.load(DataGenerator.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected VehicleApplicationPrimary() throws RemoteException {
        super();
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        try {
            String serverIp = props.getProperty("server.ip");
            String serverPort = props.getProperty("server.port");
            String serverReference = props.getProperty("server.reference");

            Registry registry = LocateRegistry.getRegistry(serverIp, Integer.parseInt(serverPort));
            serverRef = (ServerCommunicationInterface) registry.lookup(serverReference);


            String backupIp = props.getProperty("vehicle.app.ip");
            String backupPort = props.getProperty("vehicle.app.port");
            String backupReference = props.getProperty("backup.process.reference");

            Registry registry1 = LocateRegistry.getRegistry(backupIp, Integer.parseInt(backupPort));
            backupRef = (ClientCommunicationInterface) registry1.lookup(backupReference);

        } catch (ConnectException e) {
            System.out.println("Server is not available!");
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            timer_heartbeat.schedule(new Heartbeat(serverRef), 0, 400);

            while (validCoordinates) {
                timer_heartbeat.cancel();
            }
        } catch (Exception e) {
            timer_heartbeat.cancel();
            System.out.println("Exception occurred! Not sending heartbeat anymore!");
            e.printStackTrace();
        }

    }

    @Override
    public void sendData(boolean flag) throws RemoteException {

    }

    @Override
    public void aliveStatus(Date statusTime, long dataPointer) throws RemoteException {

    }

    @Override
    public void collectData(double latitude, double longitude) throws RemoteException {
        long current = counter.longValue();
        //send data to server as well

        if (latitude > 89.8 && longitude < 0.2) {
            System.out.println("Error in critical process");
            this.validCoordinates = false;
        } else {
            counter.getAndIncrement();
            backupRef.aliveStatus(new Date(), current);
        }
    }
}