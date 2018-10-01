package assign1.server;

import assign1.common.CommunicationInterface;

import javax.sql.CommonDataSource;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain extends UnicastRemoteObject implements CommunicationInterface {

    protected ServerMain() throws RemoteException {
        super();
    }

    @Override
    public String connectToServer(String connectionMessage) throws RemoteException {
        System.out.println("Message from client : " + connectionMessage);

        return "Hello from server!";
    }

    public static void main(String[] args) throws RemoteException {
        CommunicationInterface serverObj = new ServerMain();
        int portNumber = 8888;

        Registry registry = LocateRegistry.createRegistry(portNumber);
        registry.rebind("ServerReference", serverObj);

        System.out.println("Server ready");

    }
}
