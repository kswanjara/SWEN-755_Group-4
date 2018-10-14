package assign1.main;

import java.util.Date;
import java.util.TimerTask;

public class Receiver {
    public Date lastUpdated;
    
    public Receiver(Date date) {
        this.lastUpdated = date;
    }

    class CheckUpdate extends TimerTask {
        public final int expirationTime = 15;

        public void run(){
            //Turn the current time and last update time into seconds 
            long current_time = (new Date().getTime())/1000;
            long last_updated = ((Receiver.this.lastUpdated).getTime())/1000;

            if(current_time - last_updated >= this.expirationTime){
                System.out.println("PROCESS NOT AVAILABLE ABORT ABORT");
            }else{
                System.out.println("Process Available");
            }
        }
    }

    /*
    *   Receive the heartbeat and update it to
    *   the passed in time
    */
    public void receiveHeartbeat(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        System.out.println("Newly updated time: " + lastUpdated);
    }

    public int getCheckingInterval(){
        return this.checkingInterval;
    }
}