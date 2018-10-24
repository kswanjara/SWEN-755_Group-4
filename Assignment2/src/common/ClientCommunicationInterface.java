package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface ClientCommunicationInterface extends Remote {
    public void sendData(boolean flag) throws RemoteException;

    public void aliveStatus(Date statusTime, long dataPointer) throws RemoteException;

    public void collectData(double latitude, double longitude) throws RemoteException;
}