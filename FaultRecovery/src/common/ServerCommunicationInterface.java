package common;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface ServerCommunicationInterface extends Remote {
    void setPrimaryCheckFlag(boolean checkFlag) throws RemoteException;

    void setBackupCheckFlag(boolean checkFlag) throws RemoteException;

    Date getLastUpdated(int processNumber) throws RemoteException;

    String connectToServer(String connectionMessage) throws RemoteException;

    void sendHeartbeat(Date lastUpdated, long serverRef, int processNumber) throws RemoteException;

    void processManagerUp() throws RemoteException, NotBoundException;
}