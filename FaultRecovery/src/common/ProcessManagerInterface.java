package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProcessManagerInterface extends Remote {
    void useBackup(boolean flag) throws RemoteException;

    void handleData(long processName, double latitude, double longitude) throws RemoteException;
}
