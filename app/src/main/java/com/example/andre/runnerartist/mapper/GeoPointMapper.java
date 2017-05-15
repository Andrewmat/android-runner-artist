package com.example.andre.runnerartist.mapper;

import android.database.Cursor;

import com.example.andre.runnerartist.model.GeoPoint;

public class GeoPointMapper extends GenericMapper<GeoPoint> {
    @Override
    public GeoPoint map(Cursor c) {
        return new GeoPoint(
                c.getDouble(c.getColumnIndex("xcoord")),
                c.getDouble(c.getColumnIndex("ycoord"))
        );
    }
}
