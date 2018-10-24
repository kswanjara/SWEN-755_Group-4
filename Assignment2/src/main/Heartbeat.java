package main;

import common.ServerCommunicationInterface;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

class Heartbeat extends TimerTask {

    ServerCommunicationInterface serverRef;
    AtomicLong counter;

    Heartbeat(ServerCommunicationInterface serverRef, AtomicLong counter) {
        this.serverRef = serverRef;
        this.counter = counter;
    }

    public void run() {
        try {
            serverRef.sendHeartbeat(new Date(), counter.longValue());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
