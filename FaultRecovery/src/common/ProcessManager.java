package common;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class ProcessManager extends UnicastRemoteObject implements ProcessManagerInterface {

    private static ServerCommunicationInterface serverRef;
    private boolean usePrimary = true;
    private boolean useBackup = false;

    private long counter;

    private static ClientCommunicationInterface primaryRef;
    private static ClientCommunicationInterface backupRef;
    private static Properties props;


    private static void loadProperties() {
        try {
            InputStream is;
            is = ProcessManager.class.getClassLoader().getResourceAsStream("application.properties");
            props = new Properties();
            props.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected ProcessManager() throws RemoteException {
        super();
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        //be available to vehicle and server apps
        loadProperties();


        Registry reg = LocateRegistry.createRegistry(Integer.parseInt(props.getProperty("third.component.port")));
        reg.rebind(props.getProperty("third.component.reference"), new ProcessManager());

        String serverIp = props.getProperty("server.ip");
        String serverPort = props.getProperty("server.port");
        String serverReference = props.getProperty("server.reference");

        Registry serverRegistry = LocateRegistry.getRegistry(serverIp, Integer.parseInt(serverPort));
        serverRef = (ServerCommunicationInterface) serverRegistry.lookup(serverReference);

        serverRef.processManagerUp();

        String primaryProcess = props.getProperty("primary.process.reference");
        String backupProcess = props.getProperty("backup.process.reference");

        try {
            Registry registry = LocateRegistry.getRegistry(props.getProperty("vehicle.app.ip"), Integer.parseInt(props.getProperty("vehicle.app.port1")));
            primaryRef = (ClientCommunicationInterface) registry.lookup(primaryProcess);

            Registry registry1 = LocateRegistry.getRegistry(props.getProperty("vehicle.app.ip"), Integer.parseInt(props.getProperty("vehicle.app.port2")));
            backupRef = (ClientCommunicationInterface) registry1.lookup(backupProcess);

            primaryRef.processManagerUp();
            backupRef.processManagerUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Process manager(Third component) is ready!");
    }

    @Override
    public void useBackup(boolean flag) throws RemoteException {
        this.useBackup = flag;
        this.usePrimary = !flag;

        System.out.println("Switching to backup!");
        this.startBackupStream(counter);
    }

    private void startBackupStream(long counter) throws RemoteException {
        backupRef.setActiveFlag(true);
    }

    @Override
    public void handleData(long counter, double latitude, double longitude) throws RemoteException {
        //System.out.println("Process in use : " + (useBackup ? "Backup " : "Primary ") + counter + " " + latitude + " " + longitude);
    }
}
