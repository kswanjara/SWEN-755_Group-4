package server;

import common.ServerCommunicationInterface;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.TimerTask;

class CheckHeartbeat extends TimerTask {
    private final int expirationTime = 5;
    private ServerCommunicationInterface serverRef;

    CheckHeartbeat(ServerCommunicationInterface serverRef) {
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
        } else {
            System.out.println((current_time - last_updated) + " seconds since last update, checking again in ( 1 ) second....");
        }
    }
}
