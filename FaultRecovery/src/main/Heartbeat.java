package main;

import common.ServerCommunicationInterface;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

class Heartbeat extends TimerTask {

    private int processNumber;
    ServerCommunicationInterface serverRef;
    AtomicLong counter;

    Heartbeat(ServerCommunicationInterface serverRef, AtomicLong counter, int process) {
        this.serverRef = serverRef;
        this.counter = counter;
        this.processNumber = process;
    }

    public int getProcessNumber(){
        return this.processNumber;
    }

    public void run() {
        try {
            serverRef.sendHeartbeat(new Date(), counter.longValue(), this.processNumber);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
