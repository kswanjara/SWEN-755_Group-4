package assign1.main;

import assign1.common.CommunicationInterface;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.TimerTask;

class Heartbeat extends TimerTask {

    CommunicationInterface serverRef;

    Heartbeat(CommunicationInterface serverRef) {
        this.serverRef = serverRef;
    }

    public void run() {
        try {
            serverRef.sendHeartbeat(new Date(), serverRef);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
