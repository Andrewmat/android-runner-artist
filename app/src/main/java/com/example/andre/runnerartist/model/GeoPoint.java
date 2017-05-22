package com.example.andre.runnerartist.model;

import android.content.ContentValues;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class GeoPoint implements ContentValueAble, Serializable, Cloneable {

    private static final long serialVersionUID = 161822477814879819L;
    private Long id;
    private Double lat, lng;
    private Long drawingId;
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
    public Long getDrawingId() {
        return drawingId;
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
    public void setDrawingId(Long drawingId) {
        this.drawingId = drawingId;
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
        setLng(lng);
        return this;
    }
    public GeoPoint withDrawingId(Long drawingId) {
        setDrawingId(drawingId);
        return this;
    }

    public LatLng asLatLng() {
        return new LatLng(getLat(), getLng());
    }

    @Override
    public GeoPoint clone() {
        return new GeoPoint(getLat(), getLng())
                .withId(getId())
                .withDrawingId(getDrawingId());
    }

    @Override
    public ContentValues asContentValues() {
        return asContentValues(null);
    }
    public ContentValues asContentValues(Integer index) {
        ContentValues cval = new ContentValues();
        cval.put("latitude", getLat());
        cval.put("longitude", getLng());
        if (index != null) {
            cval.put("ind", index);
        }
        cval.put("drawing_id", getDrawingId());
        return cval;
    }
}
