package com.example.andre.runnerartist.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.andre.runnerartist.mapper.DrawingMapper;
import com.example.andre.runnerartist.model.Drawing;

import java.util.List;

public class DatabaseExecutor {
    private SQLiteDatabase readableDb;

    public DatabaseExecutor(Context ctx) {
        readableDb = new SQLiteOpenHelperImpl(ctx).getReadableDatabase();
    }
    public Cursor getCursorDrawings() {
        return getCursorDrawing(null, null);
    }
    public Cursor getCursorDrawingById(Integer id) {
        return getCursorDrawing("_id = ?", new String[] { id.toString() });
    }
    public Cursor getCursorDrawing(String selection, String[] selectionArgs) {
        return readableDb.query("t_drawing", new String[] {
                "_id",
                "profile_id",
                "description",
                "cycle",
                "start",
                "finish"
        }, selection, selectionArgs, null, null, null);
    }

    public Drawing getDrawingById(Integer id) {
        return new DrawingMapper().map(getCursorDrawing("_id = ?", new String[] { id.toString() }));
    }
    public List<Drawing> getDrawingsFromProfile(Integer profileId) {
        return getDrawings("profile_id = ?", new String[] { profileId.toString() });
    }
    public List<Drawing> getDrawings() {
        return getDrawings(null, null);
    }
    public List<Drawing> getDrawings(String selection, String[] selectionArgs) {
        return new DrawingMapper().mapList(getCursorDrawing(selection, selectionArgs));
    }
}
