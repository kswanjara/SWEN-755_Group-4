package DataGenerator;

import common.ClientCommunicationInterface;
import common.ServerCommunicationInterface;

import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

public class DataGenerator implements Runnable {

    private static Properties props;

    static ClientCommunicationInterface primaryRef;
    static ClientCommunicationInterface backupRef;

    public static void main(String[] args) {
        String primaryProcess = props.getProperty("primary.process.reference");
        String backupProcess = props.getProperty("backup.process.reference");

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 8888);
            primaryRef = (ClientCommunicationInterface) registry.lookup(primaryProcess);
            backupRef = (ClientCommunicationInterface) registry.lookup(backupProcess);

            Thread t = new Thread(new DataGenerator());
            t.start();
        } catch (ConnectException e) {
            System.out.println("Processes not available is not available!");
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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


    public static boolean getCoordinates() throws IOException {
        double minLat = -90.00;
        double maxLat = 90.00;
        double latitude = minLat + (double) (Math.random() * ((maxLat - minLat) + 1));

        double minLon = 0.00;
        double maxLon = 180.00;
        double longitude = minLon + (double) (Math.random() * ((maxLon - minLon) + 1));

        primaryRef.collectData(latitude, longitude);
        backupRef.collectData(latitude, longitude);

        return true;
    }

    @Override
    public void run() {
        try {
            getCoordinates();
            Thread.sleep(2000);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
