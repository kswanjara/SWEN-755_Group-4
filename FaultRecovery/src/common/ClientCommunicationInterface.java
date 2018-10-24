package common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface ClientCommunicationInterface extends Remote {

    public void processManagerUp() throws RemoteException, NotBoundException;

    public void aliveStatus(Date statusTime, long dataPointer) throws RemoteException;

    public void collectData(double latitude, double longitude) throws IOException;

    public Date getPrimaryLastUpdated() throws RemoteException;

    public void setActiveFlag(boolean b) throws RemoteException;

}