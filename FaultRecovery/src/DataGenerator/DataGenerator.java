package DataGenerator;

import java.io.IOException;
import java.util.Properties;

public class DataGenerator {

    private static Properties props;

    public static void main(String[] args) {

    }

    /**
     * This method loads the properties of application.
     */
    private static void loadProperties() {
        try {
            props = new Properties();
            props.load(DataGenerator.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean getCoordinates() {
        double minLat = -90.00;
        double maxLat = 90.00;
        double latitude = minLat + (double) (Math.random() * ((maxLat - minLat) + 1));

        double minLon = 0.00;
        double maxLon = 180.00;
        double longitude = minLon + (double) (Math.random() * ((maxLon - minLon) + 1));

        if (latitude > 89.8 && longitude < 0.2) {
            System.out.println("Error in critical process");
            return false;
        } else {
            System.out.println("Working fine!");
        }

        return true;
    }
}
