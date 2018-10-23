package assign1.server;

import assign1.common.CommunicationInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Timer;

public class HeartbeatReceiver extends UnicastRemoteObject implements CommunicationInterface {

    private Date lastUpdated;
    private boolean checkFlag = false;
    private Timer check_heartbeat;

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
    public void sendHeartbeat(Date lastUpdated, CommunicationInterface serverObj) {
        this.lastUpdated = lastUpdated;
        if (!this.checkFlag) {
            this.checkFlag = true;
            CheckHeartbeat chb = new CheckHeartbeat(serverObj);
            this.check_heartbeat = new Timer();
            this.check_heartbeat.schedule(chb, 0, chb.getCheckingInterval());
        }

        System.out.println("New heartbeat time: " + lastUpdated);
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
