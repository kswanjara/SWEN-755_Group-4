package server;

import common.ProcessManagerInterface;
import common.ServerCommunicationInterface;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;

public class ServerManager extends UnicastRemoteObject implements ServerCommunicationInterface {

    private Date primary_lastUpdated;
    private Date backup_primary;

    private boolean checkPrimaryFlag = false;
    private boolean checkBackupFlag = false;
    private Timer checkPrimaryHeartbeat;
    private Timer checkBackupHeartbeat;
    private static Properties props;
    private static ProcessManagerInterface pmanagerRef;


    private long processCounter = 0;

    /**
     * This method loads the properties of application.
     */
    private static void loadProperties() {
        try {
            InputStream is;
            is = ServerManager.class.getClassLoader().getResourceAsStream("application.properties");
            props = new Properties();
            props.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Date getLastUpdated(int processNumber) {
        if (processNumber == 1) {
            return this.primary_lastUpdated;
        } else {
            return this.backup_primary;
        }
    }

    @Override
    public void setPrimaryCheckFlag(boolean checkFlag) {
        this.checkPrimaryFlag = checkFlag;
        if (!checkFlag) {
            this.checkPrimaryHeartbeat.cancel();
        }
    }

    @Override
    public void setBackupCheckFlag(boolean checkFlag) throws RemoteException {
        this.checkBackupFlag = checkFlag;
        if (!checkFlag) {
            this.checkBackupHeartbeat.cancel();
        }
    }

    @Override
    public String connectToServer(String connectionMessage) throws RemoteException {
        System.out.println("Message from client : " + connectionMessage);
        return "Hello from server!";
    }

    @Override
    public void sendHeartbeat(Date lastUpdated, long serverObj, int processNumber) {

        //check to see if the heartbeat is coming from the primary process - 1 or the back up process - 0
        if (processNumber == 1) {
            this.primary_lastUpdated = lastUpdated;
            if (!this.checkPrimaryFlag) {
                this.checkPrimaryFlag = true;
                this.checkPrimaryHeartbeat = new Timer();
                this.checkPrimaryHeartbeat.schedule(new CheckHeartbeat(this, processNumber, pmanagerRef), 0, 1000);
            }
        } else {
            this.backup_primary = lastUpdated;
            if (!this.checkBackupFlag) {
                this.checkBackupFlag = true;
                this.checkPrimaryHeartbeat = new Timer();
                this.checkPrimaryHeartbeat.schedule(new CheckHeartbeat(this, processNumber, pmanagerRef), 0, 1000);
            }
        }

        System.out.println("New heartbeat time from " + (processNumber == 1 ? "Primary" : "Backup ") + " : " + lastUpdated);

    }

    protected ServerManager() throws RemoteException {
        super();
    }

    @Override
    public void processManagerUp() throws RemoteException, NotBoundException {
        String thirdCompIP = props.getProperty("third.component.ip");
        int thirdCompPort = Integer.parseInt(props.getProperty("third.component.port"));
        String thirdCompReference = props.getProperty("third.component.reference");


        Registry registry1 = LocateRegistry.getRegistry(thirdCompIP, thirdCompPort);
        pmanagerRef = (ProcessManagerInterface) registry1.lookup(thirdCompReference);

    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        loadProperties();

        ServerCommunicationInterface serverObj = new ServerManager();
        int portNumber = 8888;

        Registry registry = LocateRegistry.createRegistry(portNumber);
        registry.rebind("ServerReference", serverObj);

        System.out.println("Server ready!");


    }
}
