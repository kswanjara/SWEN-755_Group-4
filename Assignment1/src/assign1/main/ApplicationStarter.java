package assign1.main;

import assign1.common.CommunicationInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.Timer;
import java.util.Date;

import assign1.main.VehicleApp;
import assign1.main.VehicleApp.HeartBeatSender;
import assign1.main.Receiver;

public class ApplicationStarter {
    private static CommunicationInterface serverRef;

    private static Timer timer_heartbeat = new Timer();
    private static Timer timer_check = new Timer();

    private static Receiver receiver = new Receiver(new Date(), timer_heartbeat);
    private static VehicleApp vapp = new VehicleApp(receiver);

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost" , 8888);
        serverRef = (CommunicationInterface) registry.lookup("ServerReference");

        timer_heartbeat.schedule(vapp.new HeartBeatSender(), 0, 12000);
        timer_check.schedule(receiver.new CheckUpdate(), 0, 5000);
    }
}
