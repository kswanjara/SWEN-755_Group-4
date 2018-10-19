package assign1.main;

import assign1.common.CommunicationInterface;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;

public class VehicleApplication {

    private static CommunicationInterface serverRef;
    private static Timer timer_heartbeat = new Timer();

    public static void main(String[] args) throws RemoteException, NotBoundException {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 8888);
            serverRef = (CommunicationInterface) registry.lookup("ServerReference");

        } catch (ConnectException e) {
            System.out.println("Server is not available!");
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            timer_heartbeat.schedule(new Heartbeat(serverRef), 0, 400);

            boolean validCoordinates = true;


            // run the commented code to check how the unexpected exception can be handled
            //int a = 1;
            //if (a == 1)
            //   throw new NullPointerException();

            while (validCoordinates) {
                if (!getCoordinates()) {
                    validCoordinates = false;
                    timer_heartbeat.cancel();
                }
            }
        } catch (Exception e) {
            timer_heartbeat.cancel();
            System.out.println("Exception occured! Not sending heartbeat anymore!");
            e.printStackTrace();
        }

    }

    public static boolean getCoordinates() {
        double minLat = -90.00;
        double maxLat = 90.00;
        double latitude = minLat + (double) (Math.random() * ((maxLat - minLat) + 1));

        double minLon = 0.00;
        double maxLon = 180.00;
        double longitude = minLon + (double) (Math.random() * ((maxLon - minLon) + 1));

        if (latitude > 89.8 && longitude < 0.2) {
            System.out.println("Error in critical process");
            return false;
        }

        return true;
    }
}