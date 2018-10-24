package common;

import DataGenerator.DataGenerator;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class ProcessManager extends UnicastRemoteObject implements ProcessManagerInterface {

    boolean usePrimary = true;
    boolean useBackup = false;

    long counter;

    static ClientCommunicationInterface primaryRef;
    static ClientCommunicationInterface backupRef;
    private static Properties props;


    private static void loadProperties() {
        try {
            props = new Properties();
            props.load(DataGenerator.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected ProcessManager() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        String primaryProcess = props.getProperty("primary.process.reference");
        String backupProcess = props.getProperty("backup.process.reference");

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 8888);
            primaryRef = (ClientCommunicationInterface) registry.lookup(primaryProcess);
            backupRef = (ClientCommunicationInterface) registry.lookup(backupProcess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void useBackup(boolean flag) throws RemoteException {
        this.useBackup = flag;
        this.usePrimary = !flag;

        this.startBackupStream(counter);
    }

    private void startBackupStream(long counter) {

    }

    @Override
    public void handleData(long counter, double latitude, double longitude) throws RemoteException {

    }


}
