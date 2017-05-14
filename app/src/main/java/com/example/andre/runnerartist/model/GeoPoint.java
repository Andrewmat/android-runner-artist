package com.example.andre.runnerartist.model;

import android.location.Location;

public class GeoPoint extends Point {
    public GeoPoint(Double lat, Double lng) {
        super(lat, lng);
    }
    public GeoPoint(Location location) {
        super(location.getLatitude(), location.getLongitude());
    }
    public static Double distance(Point p1, Point p2) {
        // http://www.codecodex.com/wiki/Calculate_Distance_Between_Two_Points_on_a_Globe
        final Double EARTH_RADIUS = 3958.75;
        Double latDelta = Math.toRadians(p2.getX() - p1.getX());
        Double lngDelta = Math.toRadians(p2.getY() - p1.getY());
        Double a = Math.pow(Math.sin(latDelta / 2), 2) +
                   Math.cos(Math.toRadians(p1.getX())) * Math.cos(Math.toRadians(p2.getX())) *
                   Math.pow(Math.sin(lngDelta / 2), 2);
        Double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }
    @Override
    public Double distance(Point p) {
        return distance(this, p);
    }

    public Double getLat() {
        return getX();
    }
    public Double getLng() {
        return getY();
    }
    public void setLat(Double lat) {
        setX(lat);
    }
    public void setLng(Double lng) {
        setY(lng);
    }
    public Point withLat(Double lat) {
        return withX(lat);
    }
    public Point withLng(Double lng) {
        return withY(lng);
    }
}
