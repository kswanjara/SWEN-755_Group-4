package assign1.main;

import java.util.Date;

public class Receiver {
    private final int checkingInterval = 10;
    private final int expirationTime = 15;

    public Date lastUpdated;
    public boolean processAvailable;

    public Receiver() {

    }

    /*
    *   Receive the heartbeat and update it to
    *   the passed in time
    */
    public void receiveHeartbeat(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
        this.processAvailable = true;
    }

    /*
    *   Check to see we have a recent update within 
    *   the expirationTime threshold
    */
    public void checkLastUpdate(){

        //Turn the current time and last update time into seconds 
        int current_time = (new Date().getTime())/1000;
        int last_updated = (this.lastUpdated.getTime())/1000;

        if(current_time - last_updated > this.expirationTime){
            //the heartbeat isn't getting sent, we need to do something
        }

    }

    public int getCheckingInterval(){
        return this.checkingInterval;
    }
}