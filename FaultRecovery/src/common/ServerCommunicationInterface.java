package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface ServerCommunicationInterface extends Remote {
    void setCheckFlag(boolean checkFlag) throws RemoteException;

    Date getLastUpdated() throws RemoteException;

    public String connectToServer(String connectionMessage) throws RemoteException;

    public void sendHeartbeat(Date lastUpdated, long serverRef, int processNumber) throws RemoteException;
}