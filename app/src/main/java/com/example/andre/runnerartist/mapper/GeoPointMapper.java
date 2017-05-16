package com.example.andre.runnerartist.mapper;

import android.content.ContentValues;
import android.content.Context;
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

    @Override
    public ContentValues toContentValues(GeoPoint geoPoint, Boolean withId) {
        ContentValues cval = new ContentValues();
        if (withId) {
            cval.put("_id", geoPoint.getId());
        }
        cval.put("latitude", geoPoint.getLat());
        cval.put("longitude", geoPoint.getLng());
        cval.put("drawing_id", geoPoint.getDrawingId());
        return cval;
    }
}
