package com.example.andre.runnerartist.model;

import android.location.Location;

public class GeoPoint {
    private Long id;
    private Double lat, lng;
    private Drawing drawing;
    public GeoPoint(Double lat, Double lng) {
        setLat(lat);
        setLng(lng);
    }
    public GeoPoint(Location location) {
        this(location.getLatitude(), location.getLongitude());
    }

    public static Double distance(GeoPoint p1, GeoPoint p2) {
        // http://www.codecodex.com/wiki/Calculate_Distance_Between_Two_Points_on_a_Globe
        final Double EARTH_RADIUS = 3958.75;
        Double latDelta = Math.toRadians(p2.getLat() - p1.getLat());
        Double lngDelta = Math.toRadians(p2.getLng() - p1.getLng());
        Double a = Math.pow(Math.sin(latDelta / 2), 2) +
                   Math.cos(Math.toRadians(p1.getLat())) * Math.cos(Math.toRadians(p2.getLat())) *
                   Math.pow(Math.sin(lngDelta / 2), 2);
        Double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }
    public Double distance(GeoPoint p) {
        return distance(this, p);
    }

    public Long getId() {
        return id;
    }
    public Double getLat() {
        return lat;
    }
    public Double getLng() {
        return lng;
    }
    public Drawing getDrawing() {
        return drawing;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setLat(Double lat) {
        this.lat = lat;
    }
    public void setLng(Double lng) {
        this.lng = lng;
    }
    public void setDrawing(Drawing drawing) {
        this.drawing = drawing;
    }
    public GeoPoint withId(Long id) {
        setId(id);
        return this;
    }
    public GeoPoint withLat(Double lat) {
        setLat(lat);
        return this;
    }
    public GeoPoint withLng(Double lng) {
        setLat(lng);
        return this;
    }
    public GeoPoint withDrawing(Drawing drawing) {
        setDrawing(drawing);
        return this;
    }

}
