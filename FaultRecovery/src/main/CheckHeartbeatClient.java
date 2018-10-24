package main;

import common.ClientCommunicationInterface;
import common.ServerCommunicationInterface;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.TimerTask;

class CheckHeartbeatClient extends TimerTask {
    private final int expirationTime = 3;
    private ClientCommunicationInterface backupRef;

    CheckHeartbeatClient(ClientCommunicationInterface backupRef) {
        this.backupRef = backupRef;
    }

    public void run() {
        //Turn the current time and last update time into seconds
        long current_time = (new Date().getTime()) / 1000;
        long last_updated = 0;
        try {
            last_updated = backupRef.getPrimaryLastUpdated().getTime() / 1000;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (current_time - last_updated >= this.expirationTime) {
            System.out.println("PROCESS NOT AVAILABLE ABORTING");
            try {
                backupRef.setActiveFlag(true);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println((current_time - last_updated) + " seconds since last update, checking again in ( 1 ) second....");
        }
    }
}
