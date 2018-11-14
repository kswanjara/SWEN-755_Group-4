package main;

import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeneratePrimeNumber {
    public static void main(String[] args) {
        System.out.println("Started : " + new Date());
        long n = 1000000;
        List<Long> primeNumners = new ArrayList<>();
        for (long l = 1; l < n; l++) {
            if (isPrime(l)) {
                primeNumners.add(l);
            }
        }

        System.out.println("Total prime numbers generated : " + primeNumners.size());
        System.out.println("Ended : " + new Date());

        System.out.println();

        System.out.println("Started : " + new Date());
        primeNumners = new ArrayList<>();
        for (long l = 1; l < n; l++) {
            if (isPrime2(l)) {
                primeNumners.add(l);
            }
        }

        System.out.println("Total prime numbers generated : " + primeNumners.size());
        System.out.println("Ended : " + new Date());
    }

    private static boolean isPrime(long l) {
        if (l == 0 || l == 1)
            return false;

        for (long i = 2; i <= Math.sqrt(l); i++) {
            if (l % i == 0)
                return false;
        }

        return true;
    }

    private static boolean isPrime2(long l) {
        if (l == 0 || l == 1)
            return false;

        for (long i = 2; i < l; i++) {
            if (l % i == 0)
                return false;
        }

        return true;
    }
}
