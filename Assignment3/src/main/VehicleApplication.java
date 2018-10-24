package main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import DataGenerator.DataGenerator;
import DataGenerator.Constants;

public class VehicleApplication {

    public static double randomizeLatitude(double min) {
        return min + Math.random() * (Constants.MAX_LATITUDE - min);
    }

    public static double randomizeLongitude(double min) {
        return min + Math.random() * (Constants.MAX_LONGITUDE - min);
    }

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(Constants.THREAD_POOL_SIZE);

        // Time to perform tasks!
        for (int i = 0; i < 100; i++) {
            double minLatitudeLimit = randomizeLatitude(Constants.MIN_LATITUDE);
            double maxLatitudeLimit = randomizeLatitude(minLatitudeLimit);
            double minLongitudeLimit = randomizeLongitude(Constants.MIN_LONGITUDE);
            double maxLongitudeLimit = randomizeLongitude(minLongitudeLimit);

            Runnable task = new DataGenerator(i, minLatitudeLimit, maxLatitudeLimit,
                                                minLongitudeLimit, maxLongitudeLimit);
            pool.execute(task);
        }

        // We have executed all the tasks!
        pool.shutdown();

        // Wait for all the tasks to complete.
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception ex) {
            System.out.println("Something went wrong.");
        } finally {
            System.out.println("Done!");
        }
    }
}
