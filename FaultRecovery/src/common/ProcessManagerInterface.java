package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProcessManagerInterface extends Remote {
    public void useBackup(boolean flag) throws RemoteException;

    public void handleData(long processName, double latitude, double longitude) throws RemoteException;
}
