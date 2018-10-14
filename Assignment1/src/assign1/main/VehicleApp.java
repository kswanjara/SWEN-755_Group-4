package assign1.main;

import java.util.Random;

public class VehicleApp {
    public double latitude;
    public double longitude;
    public Receiver receiver;

    public VehicleApp() {

    }

    public string getCoordinates() {
        return "43.0846, 77.6743";
    }

    public void sendCoordinates() {

    }

    /*
    *   Function that sends the notification of the critical
    *   process to the Receiver.java file
    */
    public void sendNotification() {
        Random random_number = new Random();
        int num = random_number.nextInt(50) + 1;

        if(num < 45){
            receiver.receiveHeartBeat(new Date());
        }else{
            //system broke, don't do anything
        }
    }
}