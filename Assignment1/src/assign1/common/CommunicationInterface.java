package assign1.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CommunicationInterface extends Remote {
    public String connectToServer(String connectionMessage) throws RemoteException;
}
