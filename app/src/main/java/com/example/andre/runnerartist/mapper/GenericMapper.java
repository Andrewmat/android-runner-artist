package com.example.andre.runnerartist.mapper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.andre.runnerartist.database.DatabaseExecutor;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericMapper<T> implements Mapper<T> {
    private Context ctx;
    private DatabaseExecutor dbExecutor;
    protected GenericMapper(Context ctx) {
        this.ctx = ctx;
    }
    protected GenericMapper() {
        ctx = null;
    }
    protected DatabaseExecutor db() throws Exception {
        if (dbExecutor == null) {
            if (ctx == null) {
                throw new Exception("Mapper not able to execute queries");
            }
            dbExecutor = new DatabaseExecutor(ctx);
        }
        return dbExecutor;
    }
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
    public ContentValues toContentValues(T t) {
        return toContentValues(t, true);
    }
}
