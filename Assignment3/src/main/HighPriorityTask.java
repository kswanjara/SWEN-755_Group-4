package main;

public class HighPriorityTask extends Thread {

    int count;

    public HighPriorityTask(int count) {
        this.count = count;
    }

    @Override
    public void run() {
        System.out.println("High Priority task number : " + count);
    }
}
