package com.example.andre.runnerartist.mapper;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.andre.runnerartist.model.Path;

public class PathMapper extends GenericMapper<Path> {
    public PathMapper() {
        super();
    }
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

    @Override
    public ContentValues toContentValues(Path path, Boolean withId) {
        return null;
    }
}
