package server;

import DataGenerator.DataGenerator;
import common.ServerCommunicationInterface;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;

public class ServerManager extends UnicastRemoteObject implements ServerCommunicationInterface {

    private Date lastUpdated;
    private boolean checkFlag = false;
    private Timer check_heartbeat = new Timer();
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

    @Override
    public Date getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public void setCheckFlag(boolean checkFlag) {
        this.checkFlag = checkFlag;
        if (!checkFlag) {
            this.check_heartbeat.cancel();
        }
    }

    @Override
    public String connectToServer(String connectionMessage) throws RemoteException {
        System.out.println("Message from client : " + connectionMessage);

        return "Hello from server!";
    }

    @Override
    public void sendHeartbeat(Date lastUpdated, long serverObj) {
        this.lastUpdated = lastUpdated;
        if (!this.checkFlag) {
            this.checkFlag = true;
            this.check_heartbeat = new Timer();
            this.check_heartbeat.schedule(new CheckHeartbeat(this), 0, 1000);
        }

        System.out.println("New heartbeat time: " + lastUpdated);
    }

    protected ServerManager() throws RemoteException {
        super();
    }

    public static void main(String[] args) throws RemoteException {
        ServerCommunicationInterface serverObj = new ServerManager();
        int portNumber = 8888;

        Registry registry = LocateRegistry.createRegistry(portNumber);
        registry.rebind("ServerReference", serverObj);

        System.out.println("Server ready");
    }
}
