package com.example.andre.runnerartist.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.andre.runnerartist.mapper.DrawingMapper;
import com.example.andre.runnerartist.mapper.GeoPointMapper;
import com.example.andre.runnerartist.mapper.PathMapper;
import com.example.andre.runnerartist.mapper.ProfileMapper;
import com.example.andre.runnerartist.model.Drawing;
import com.example.andre.runnerartist.model.GeoPoint;
import com.example.andre.runnerartist.model.Path;
import com.example.andre.runnerartist.model.Profile;

import java.util.List;

public class DatabaseExecutor {
    private SQLiteDatabase readableDb;
    private Context ctx;

    public DatabaseExecutor(Context ctx) {
        this.ctx = ctx;
        readableDb = new SQLiteOpenHelperImpl(ctx).getReadableDatabase();
    }

    /**  ------------- PROFILES ------------- **/
    private Cursor getCursorProfile(String selection, String[] selectionArgs) {
        return readableDb.query("t_profile", new String[] {
                "_id",
                "name",
        }, selection, selectionArgs, null, null, null);
    }

    public Profile getProfileById(Integer id) {
        Cursor c = getCursorProfile("_id = ?", new String[] { id.toString() });
        if (c.moveToFirst()) {
            return new ProfileMapper().map(c);
        } else {
            return null;
        }
    }
    public List<Profile> getProfiles() {
        return getProfiles(null, null);
    }
    public List<Profile> getProfiles(String selection, String[] selectionArgs) {
        return new ProfileMapper().mapList(getCursorProfile(selection, selectionArgs));
    }
    /**  ----------- END PROFILES ----------- **/

    /**  ------------- DRAWINGS ------------- **/
    public Drawing getDrawingById(Integer id) {
        Cursor c = getCursorDrawing("_id = ?", new String[] { id.toString() });
        if (c.moveToFirst()) {
            return new DrawingMapper(ctx).map(c);
        } else {
            return null;
        }
    }
    public List<Drawing> getDrawingsFromProfile(Integer profileId) {
        return getDrawings("profile_id = ?", new String[] { profileId.toString() });
    }
    public List<Drawing> getDrawings() {
        return getDrawings(null, null);
    }
    private List<Drawing> getDrawings(String selection, String[] selectionArgs) {
        return new DrawingMapper(ctx).mapList(getCursorDrawing(selection, selectionArgs));
    }
    private Cursor getCursorDrawing(String selection, String[] selectionArgs) {
        return readableDb.query("t_drawing", new String[] {
                "_id",
                "profile_id",
                "description",
                "cycle",
                "start",
                "finish"
        }, selection, selectionArgs, null, null, null);
    }
    /**  ----------- END DRAWINGS ----------- **/

    /**  -------------- POINTS -------------- **/
    public Path getPathByDrawing(Integer drawingId) {
        return new PathMapper().map(getCursorGeoPointFromDrawing(drawingId));
    }
    private Cursor getCursorGeoPointFromDrawing(Integer drawingId) {
        return getCursorGeoPoint("drawing_id = ?", new String[] { drawingId.toString() });
    }
    private Cursor getCursorGeoPoint(String selection, String[] selectionArgs) {
        return readableDb.query("t_point", new String[] {
                "_id",
                "xcoord",
                "ycoord",
                "drawing_id"
        }, selection, selectionArgs, null, "ind ASC", null);
    }
    /**  ------------ END POINTS ------------ **/
}
