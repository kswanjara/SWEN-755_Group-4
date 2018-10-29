package server;

import common.ProcessManagerInterface;
import common.ServerCommunicationInterface;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.TimerTask;

class CheckHeartbeat extends TimerTask {
    private final int expirationTime = 5;
    private final int checkingInterval = 1;
    private int processNumber;
    private ProcessManagerInterface pmanagerRef;
    private boolean useBackup = false;

    private boolean checkForBackupProcess = false;
    private boolean checkForPrimaryProcess = false;

    private ServerCommunicationInterface serverRef;

    CheckHeartbeat(ServerCommunicationInterface serverRef, int processNumber, ProcessManagerInterface pmanagerRef) {
        this.serverRef = serverRef;
        this.processNumber = processNumber;
        this.pmanagerRef = pmanagerRef;
        if (processNumber == 1) {
            this.checkForPrimaryProcess = true;
        } else {
            this.checkForBackupProcess = true;
        }
    }

    public void run() {
        //Turn the current time and last update time into seconds
        long current_time = (new Date().getTime()) / 1000;
        long primary_last_updated = -1;
        long backup_last_updated = -1;

        try {

            Date prim = serverRef.getLastUpdated(1);
            if (prim != null) {
                primary_last_updated = serverRef.getLastUpdated(1).getTime() / 1000;
            }
            Date backup = serverRef.getLastUpdated(0);
            if (backup != null) {
                backup_last_updated = serverRef.getLastUpdated(0).getTime() / 1000;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (checkForPrimaryProcess) {
            int fail = -1;
            if (primary_last_updated > -1 && current_time - primary_last_updated >= this.expirationTime) {
                fail = 1;
                try {
                    if (!useBackup) {
                        useBackup = true;
                        checkForPrimaryProcess = false;
                        serverRef.setPrimaryCheckFlag(false);
                        System.out.println("PRIMARY PROCESS NOT AVAILABLE - timeout of " + this.expirationTime + " seconds");
                        pmanagerRef.useBackup(true);

                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        if (backup_last_updated > -1 && current_time - backup_last_updated >= this.expirationTime) {
            System.out.println("BACKUP PROCESS NOT AVAILABLE - timeout of " + this.expirationTime + " seconds");
            checkForBackupProcess = false;
            try {
                serverRef.setBackupCheckFlag(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }


        //System.out.println((current_time - last_updated) + " seconds since last update, checking again in ( " + this.checkingInterval + " ) second....");
    }

    public int getCheckingInterval() {
        return this.checkingInterval * 1000;
    }
}