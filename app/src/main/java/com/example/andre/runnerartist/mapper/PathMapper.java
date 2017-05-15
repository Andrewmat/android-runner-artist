package com.example.andre.runnerartist.mapper;

import android.database.Cursor;

import com.example.andre.runnerartist.model.Path;

public class PathMapper extends GenericMapper<Path> {
    @Override
    public Path map(Cursor c) {
        Path p = new Path();
        GeoPointMapper mapper = new GeoPointMapper();
        if (c.moveToFirst()) {
            do {
                p.addPoint(mapper.map(c));
            } while (c.moveToNext());
        }
        return p;
    }
}
