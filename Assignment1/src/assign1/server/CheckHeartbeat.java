package assign1.server;

import assign1.common.CommunicationInterface;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.TimerTask;

class CheckHeartbeat extends TimerTask {
    private final int expirationTime = 15;
    private CommunicationInterface serverRef;

    CheckHeartbeat(CommunicationInterface serverRef) {
        this.serverRef = serverRef;
    }

    public void run() {
        //Turn the current time and last update time into seconds
        long current_time = (new Date().getTime()) / 1000;
        long last_updated = 0;
        try {
            last_updated = serverRef.getLastUpdated().getTime() / 1000;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (current_time - last_updated >= this.expirationTime) {
            System.out.println("PROCESS NOT AVAILABLE ABORTING");
            try {
                serverRef.setCheckFlag(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
