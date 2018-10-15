package assign1.main;

import assign1.common.CommunicationInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;


public class VehicleApplication {
    private static CommunicationInterface serverRef;

    private static boolean keepNotifying = true;

    private static Timer timer_heartbeat = new Timer();

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 8888);
        serverRef = (CommunicationInterface) registry.lookup("ServerReference");

        timer_heartbeat.schedule(new Heartbeat(serverRef), 0, 12000);

        boolean validCoordinates = true;
        while (validCoordinates) {
            System.out.println("gibberish");
            if (!getCoordinates()) {
                validCoordinates = false;
                timer_heartbeat.cancel();
            }
        }
    }

    public static boolean getCoordinates() {
        double minLat = -90.00;
        double maxLat = 90.00;
        double latitude = minLat + (double) (Math.random() * ((maxLat - minLat) + 1));

        double minLon = 0.00;
        double maxLon = 180.00;
        double longitude = minLon + (double) (Math.random() * ((maxLon - minLon) + 1));

        if (latitude > 80 || longitude < 20) {
            return false;
        }

        return true;
    }

}


