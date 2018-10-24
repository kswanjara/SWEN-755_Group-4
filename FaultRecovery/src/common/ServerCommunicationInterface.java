package common;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface ServerCommunicationInterface extends Remote {
    void setCheckFlag(boolean checkFlag) throws RemoteException;

    Date getLastUpdated(int processNumber) throws RemoteException;

    public String connectToServer(String connectionMessage) throws RemoteException;

    public void sendHeartbeat(Date lastUpdated, long serverRef, int processNumber) throws RemoteException;

    public void processManagerUp() throws RemoteException, NotBoundException;
}