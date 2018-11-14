package main;

import DataGenerator.Constants;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentPrimeGenerator extends Thread {
    static AtomicLong number;
    static CopyOnWriteArrayList primeNumbers;

    public ConcurrentPrimeGenerator(AtomicLong number, CopyOnWriteArrayList primeNumbers) {
        this.number = number;
        this.primeNumbers = primeNumbers;
    }

    @Override
    public void run() {
        long threadId = Thread.currentThread().getId() % Constants.THREAD_POOL_SIZE + 1;

        long n = number.getAndIncrement();
        if (isPrime(n)) {
            primeNumbers.add(n);
            System.out.println("[Thread " + threadId + "] Prime number generated : " + n);
        }
    }

    private boolean isPrime(long l) {
        if (l == 0 || l == 1)
            return false;

        for (long i = 2; i < l; i++) {
            if (l % i == 0)
                return false;
        }

        return true;
    }
}
