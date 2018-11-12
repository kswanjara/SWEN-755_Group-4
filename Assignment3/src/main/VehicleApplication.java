package main;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import DataGenerator.DataGenerator;
import DataGenerator.Constants;
import com.util.concurrent.Executors;
import com.util.concurrent.PriorityExecutorService;

public class VehicleApplication {

    public static double randomizeLatitude(double min) {
        return min + Math.random() * (Constants.MAX_LATITUDE - min);
    }

    public static PriorityExecutorService pool;

    public static double randomizeLongitude(double min) {
        return min + Math.random() * (Constants.MAX_LONGITUDE - min);
    }

    public static void main(String[] args) throws Exception {
//        ExecutorService pool = Executors.newFixedThreadPool(Constants.THREAD_POOL_SIZE);
        pool = Executors.newPriorityFixedThreadPool(Constants.THREAD_POOL_SIZE);

        // Time to perform tasks!
        for (int i = 0; i < 100; i++) {
            double minLatitudeLimit = Constants.MIN_LATITUDE;
            double maxLatitudeLimit = minLatitudeLimit;
            double minLongitudeLimit = Constants.MIN_LONGITUDE;
            double maxLongitudeLimit = minLongitudeLimit;
//            double minLatitudeLimit = randomizeLatitude(Constants.MIN_LATITUDE);
//            double maxLatitudeLimit = randomizeLatitude(minLatitudeLimit);
//            double minLongitudeLimit = randomizeLongitude(Constants.MIN_LONGITUDE);
//            double maxLongitudeLimit = randomizeLongitude(minLongitudeLimit);

            Runnable task = new DataGenerator(i, minLatitudeLimit, maxLatitudeLimit,
                    minLongitudeLimit, maxLongitudeLimit);
            pool.submit(task, 5);
        }

        for (int i = 1; i <= 10; i++) {
            pool.submit(new HighPriorityTask(i), Thread.MAX_PRIORITY);
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
