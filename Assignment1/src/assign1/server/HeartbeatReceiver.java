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
    private static boolean checkFlag = false;
    private static Timer check_heartbeat = new Timer();

    @Override
    public Date getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public void setCheckFlag(boolean checkFlag) {
        HeartbeatReceiver.checkFlag = checkFlag;
    }

    @Override
    public String connectToServer(String connectionMessage) throws RemoteException {
        System.out.println("Message from client : " + connectionMessage);

        return "Hello from server!";
    }

    @Override
    public void sendHeartbeat(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        if(!this.checkFlag){
            this.checkFlag = true;
            check_heartbeat.schedule(new CheckHeartbeat(serverObj), 0, 4000);
        }

        System.out.println("Newly updated time: " + lastUpdated);
    }

    protected HeartbeatReceiver() throws RemoteException {
        super();
    }

    public static void main(String[] args) throws RemoteException {
        CommunicationInterface serverObj = new HeartbeatReceiver();
        int portNumber = 8888;

        Registry registry = LocateRegistry.createRegistry(portNumber);
        registry.rebind("ServerReference", serverObj);

        System.out.println("Server ready");
    }
}
