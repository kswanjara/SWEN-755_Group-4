package assign1.server;

import java.util.Date;
import java.util.TimerTask;

class CheckHeartbeat extends TimerTask {
    private static final int expirationTime = 15;
    private static HeartbeatReceiver serverRef;

    CheckHeartbeat(HeartbeatReceiver serverRef) {
        this.serverRef = serverRef;
    }

    public void run() {
        //Turn the current time and last update time into seconds
        long current_time = (new Date().getTime()) / 1000;
        long last_updated = serverRef.getLastUpdated().getTime() / 1000;

        if (current_time - last_updated >= this.expirationTime) {
            System.out.println("PROCESS NOT AVAILABLE ABORTING");
            serverRef.setCheckFlag(false);
        } else {
            System.out.println("Process Available");
        }
    }
}
