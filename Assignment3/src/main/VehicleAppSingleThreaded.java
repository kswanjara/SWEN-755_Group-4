package main;

import DataGenerator.Constants;
import DataGenerator.Coordinate;

public class VehicleAppSingleThreaded {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            double minLatitudeLimit = Constants.MIN_LATITUDE;
            double maxLatitudeLimit = minLatitudeLimit;
            double minLongitudeLimit = Constants.MIN_LONGITUDE;
            double maxLongitudeLimit = minLongitudeLimit;

            Coordinate coordinate = generateCoordinate();

            while (!(coordinate.getLatitude() > 89.99 && coordinate.getLongitude() < 0.05)) {
                coordinate = generateCoordinate();
            }

            // We found one! Output the result.
            System.out.println("Latitude: " + coordinate.getLatitude() + ", Longitude: " + coordinate.getLongitude());
        }
    }

    public static Coordinate generateCoordinate() {
        double latitude = Constants.MIN_LATITUDE + Math.random() * (Constants.MAX_LATITUDE - Constants.MIN_LATITUDE);
        double longitude = Constants.MIN_LONGITUDE + Math.random() * (Constants.MAX_LONGITUDE - Constants.MIN_LONGITUDE);

        return new Coordinate(latitude, longitude);
    }
}
