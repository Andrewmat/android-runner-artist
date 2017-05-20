package com.example.andre.runnerartist.mapper;

import android.database.Cursor;

import com.example.andre.runnerartist.model.GeoPoint;

public class GeoPointMapper extends GenericMapper<GeoPoint> {
    public GeoPointMapper() {
        super();
    }
    @Override
    public GeoPoint map(Cursor c) {
        try {
            return new GeoPoint(
                    c.getDouble(c.getColumnIndex("latitude")),
                    c.getDouble(c.getColumnIndex("longitude")))
                    .withId(c.getLong(c.getColumnIndex("_id")))
                    .withDrawingId(c.getLong(c.getColumnIndex("drawing_id")));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
