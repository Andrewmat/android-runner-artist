package com.example.andre.runnerartist.mapper;

import android.content.Context;
import android.database.Cursor;

import com.example.andre.runnerartist.model.DrawingPath;

public class PathMapper extends GenericMapper<DrawingPath> {
    public PathMapper(Context ctx) {
        super(ctx);
    }
    @Override
    public DrawingPath map(Cursor c) {
        return new DrawingPath().withPoints(new GeoPointMapper().mapList(c));
    }
}
