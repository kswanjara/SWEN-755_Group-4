package assign1.main;

import java.util.Random;
import java.util.TimerTask;
import java.util.Date;

public class VehicleApp {
    public double latitude;
    public double longitude;
    public Receiver receiver;

    public VehicleApp() {

    }

    class HeartBeatSender extends TimerTask {

        /*
        * Scheduled task to send heartbeat every 5 seconds,
        * this is called from ApplicationStarter
        */
        public void run(){
            Random random_number = new Random();
            int num = random_number.nextInt(50) + 1;

            if(num < 45){
                receiver.receiveHeartBeat(new Date());
            }else{
                //system broke, don't do anything
            }
        }
    }

    public string getCoordinates() {
        return "43.0846, 77.6743";
    }

    public void sendCoordinates() {

    }

}