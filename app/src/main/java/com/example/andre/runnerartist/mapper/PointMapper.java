package com.example.andre.runnerartist.mapper;

import android.database.Cursor;

import com.example.andre.runnerartist.model.Point;

import java.util.ArrayList;
import java.util.List;

public class PointMapper extends GenericMapper<Point> {
    @Override
    public Point map(Cursor c) {
        return new Point(
                c.getDouble(c.getColumnIndex("xcoord")),
                c.getDouble(c.getColumnIndex("ycoord"))
        );
    }

    @Override
    public List<Point> mapList(Cursor c) {
        List<Point> lst = new ArrayList<>(c.getCount());
        if (c.moveToFirst()) {
            do {
                lst.add(map(c));
            } while (c.moveToNext());
        }
        return lst;
    }
}
