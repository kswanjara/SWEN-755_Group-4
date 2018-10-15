package assign1.server;

import assign1.common.CommunicationInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Timer;

public class HeartbeatReceiver extends UnicastRemoteObject implements CommunicationInterface {

    private static Date lastUpdated;

    public static void setCheckFlag(boolean checkFlag) {
        HeartbeatReceiver.checkFlag = checkFlag;
    }

    private static boolean checkFlag = false;
    private static Timer check_heartbeat = new Timer();

    public static Date getLastUpdated() {
        return lastUpdated;
    }

    protected HeartbeatReceiver() throws RemoteException {
        super();
    }

    @Override
    public String connectToServer(String connectionMessage) throws RemoteException {
        System.out.println("Message from client : " + connectionMessage);

        return "Hello from server!";
    }

    public static void main(String[] args) throws RemoteException {
        CommunicationInterface serverObj = new HeartbeatReceiver();
        int portNumber = 8888;

        Registry registry = LocateRegistry.createRegistry(portNumber);
        registry.rebind("ServerReference", serverObj);

        System.out.println("Server ready");

        if (checkFlag) {
            check_heartbeat.schedule(new CheckHeartbeat((HeartbeatReceiver) serverObj), 0, 5000);
        }
    }

    @Override
    public void sendHeartbeat(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        this.checkFlag = true;
        System.out.println("Newly updated time: " + lastUpdated);
    }

}
