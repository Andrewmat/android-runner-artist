package com.example.andre.runnerartist.mapper;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericMapper<T> implements Mapper<T> {
    @Override
    public List<T> mapList(Cursor c) {
        List<T> lst = new ArrayList<>(c.getCount());
        if (c.moveToFirst()) {
            do {
                lst.add(map(c));
            } while (c.moveToNext());
        }
        return lst;
    }
}
