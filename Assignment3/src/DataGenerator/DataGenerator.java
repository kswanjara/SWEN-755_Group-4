package DataGenerator;

public class DataGenerator implements Runnable {
    private double minLatitudeLimit;
    private double maxLatitudeLimit;
    private double minLongitudeLimit;
    private double maxLongitudeLimit;

    private int workerId;

    /**
     * Constructor
     */
    public DataGenerator(int workerId, double minLatitudeLimit, double maxLatitudeLimit,
                         double minLongitudeLimit, double maxLongitudeLimit) {
        this.workerId = workerId;

        this.minLatitudeLimit = minLatitudeLimit;
        this.maxLatitudeLimit = maxLatitudeLimit;
        this.minLongitudeLimit = minLongitudeLimit;
        this.maxLongitudeLimit = maxLongitudeLimit;
    }

    /**
     * Generates a coordinate.
     *
     * @return Coordinate
     */
    public Coordinate generateCoordinate() {
        double latitude = Constants.MIN_LATITUDE + Math.random() * (Constants.MAX_LATITUDE - Constants.MIN_LATITUDE);
        double longitude = Constants.MIN_LONGITUDE + Math.random() * (Constants.MAX_LONGITUDE - Constants.MIN_LONGITUDE);

        return new Coordinate(latitude, longitude);
    }

    public void run() {
        // Get the thread's id
        long threadId = Thread.currentThread().getId() % Constants.THREAD_POOL_SIZE + 1;

        // Keep generating a coordinate till it falls in the range
        Coordinate coordinate = generateCoordinate();

        while (!(coordinate.getLatitude() > 89.8 && coordinate.getLongitude() < 0.5)) {
            coordinate = generateCoordinate();
        }

        // We found one! Output the result.
        System.out.println("[Thread " + threadId + " | Worker " + workerId + "] Latitude: " + coordinate.getLatitude() + ", Longitude: " + coordinate.getLongitude());
    }
}
