package server;

import assign1.common.CommunicationInterface;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.TimerTask;

class CheckHeartbeat extends TimerTask {
    private final int expirationTime = 5;
    private final int checkingInterval = 1;
    private int processNumber;

    private CommunicationInterface serverRef;

    CheckHeartbeat(CommunicationInterface serverRef, int processNumber) {
        this.serverRef = serverRef;
        this.processNumber = processNumber;
    }

    public void run() {
        //Turn the current time and last update time into seconds
        long current_time = (new Date().getTime()) / 1000;
        long primary_last_updated = 0;
        long backup_last_updated = 0;

        try {
            primary_last_updated = serverRef.getLastUpdated(1).getTime() / 1000;
            backup_last_updated = serverRef.getLastUpdated(0).getTime() / 1000;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int fail = -1;
        if (current_time - primary_last_updated >= this.expirationTime) {
            fail = 1;
        }else if(current_time - backup_last_updated >= this.expirationTime){
            fail = 0;
        }

        if(fail > 0){
            System.out.println("PROCESS " + fail + " NOT AVAILABLE - timeout of " + this.expirationTime + " seconds");
            try {
                serverRef.setCheckFlag(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return;
        }
        
        System.out.println((current_time - last_updated) + " seconds since last update, checking again in ( " + this.checkingInterval + " ) second....");
    }

    public int getCheckingInterval(){
        return this.checkingInterval * 1000;
    }
}