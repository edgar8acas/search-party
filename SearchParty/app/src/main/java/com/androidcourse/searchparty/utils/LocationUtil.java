package com.androidcourse.searchparty.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LocationUtil {

    private double startingLat;
    private double startingLong;
    private double maxLat;
    private double maxLong;

    public LocationUtil() {
        startingLat = 46.118907;
        startingLong = -63.627271;
        maxLong = -62.995466;
        maxLat = 46.7405678;

    }

    public Location generateNewLocation() {
        double lat = ThreadLocalRandom.current().nextDouble(startingLat, maxLat );
        double lon = ThreadLocalRandom.current().nextDouble(startingLong, maxLong);
        return new Location(lat, lon);
    }

    public static class Location {
        private double Lat;
        private double Long;

        public Location(double lat, double aLong) {
            Lat = lat;
            Long = aLong;
        }

        public double getLat() {
            return Lat;
        }

        public void setLat(double lat) {
            Lat = lat;
        }

        public double getLong() {
            return Long;
        }

        public void setLong(double aLong) {
            Long = aLong;
        }
    }
}
