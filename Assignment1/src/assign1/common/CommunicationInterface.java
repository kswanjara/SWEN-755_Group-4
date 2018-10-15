package assign1.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface CommunicationInterface extends Remote {
    public String connectToServer(String connectionMessage) throws RemoteException;

    public void sendHeartbeat(Date lastUpdated) throws RemoteException;
}
