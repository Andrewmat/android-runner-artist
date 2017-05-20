package com.example.andre.runnerartist.mapper;

import android.content.Context;
import android.database.Cursor;

import com.example.andre.runnerartist.model.Path;

public class PathMapper extends GenericMapper<Path> {
    public PathMapper(Context ctx) {
        super(ctx);
    }
    @Override
    public Path map(Cursor c) {
        return new Path().withPoints(new GeoPointMapper().mapList(c));
    }
}
