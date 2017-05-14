package com.example.andre.runnerartist.mapper;

import android.database.Cursor;

import com.example.andre.runnerartist.model.Path;

public class PathMapper extends GenericMapper<Path> {
    private PointMapper pointMapper;

    public PathMapper() {
        pointMapper = new GeoPointMapper();
    }
    public PathMapper(PointMapper pointMapper) {
        this.pointMapper = pointMapper;
    }

    @Override
    public Path map(Cursor c) {
        Path p = new Path();
        if (c.moveToFirst()) {
            do {
                p.addPoint(pointMapper.map(c));
            } while (c.moveToNext());
        }
        return p;
    }
}
