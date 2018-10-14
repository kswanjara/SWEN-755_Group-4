package assign1.main;

import java.util.Date;
import java.util.TimerTask;
import java.util.Timer;

public class Receiver {
    public Date lastUpdated;
    public Timer timer;

    public Receiver(Date date, Timer timer) {
        this.lastUpdated = date;
        this.timer = timer;
    }

    class CheckUpdate extends TimerTask {
        public final int expirationTime = 15;

        public void run(){
            //Turn the current time and last update time into seconds 
            long current_time = (new Date().getTime())/1000;
            long last_updated = ((Receiver.this.lastUpdated).getTime())/1000;

            if(current_time - last_updated >= this.expirationTime){
                System.out.println("PROCESS NOT AVAILABLE ABORTING");
                Receiver.this.timer.cancel();

                /*
                *   we need to do more stuff here since this is where we realize
                *   the critical process is no longer working
                */
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