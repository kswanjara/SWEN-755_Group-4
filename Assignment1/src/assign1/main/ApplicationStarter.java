package assign1.main;

import assign1.common.CommunicationInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ApplicationStarter {
    private static CommunicationInterface serverRef;

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost" , 8888);
        serverRef = (CommunicationInterface) registry.lookup("ServerReference");
        System.out.println(serverRef.connectToServer("Hello server!"));;
    }
}
