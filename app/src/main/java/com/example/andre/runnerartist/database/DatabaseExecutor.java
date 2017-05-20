package com.example.andre.runnerartist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.andre.runnerartist.mapper.DrawingMapper;
import com.example.andre.runnerartist.mapper.PathMapper;
import com.example.andre.runnerartist.mapper.ProfileMapper;
import com.example.andre.runnerartist.misc.ConfigConstant;
import com.example.andre.runnerartist.model.Drawing;
import com.example.andre.runnerartist.model.GeoPoint;
import com.example.andre.runnerartist.model.Path;
import com.example.andre.runnerartist.model.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class DatabaseExecutor {
    private final SQLiteDatabase readableDb, writableDb;
    private final Context ctx;

    public DatabaseExecutor(Context ctx) {
        this.ctx = ctx;
        readableDb = new SQLiteOpenHelperImpl(ctx).getReadableDatabase();
        writableDb = new SQLiteOpenHelperImpl(ctx).getWritableDatabase();
    }

    /**  ------------- PROFILES ------------- **/
    public Profile getProfileById(Long id) {
        Cursor c = getCursorProfile("_id = ?", new String[] { id.toString() });
        Profile ret = null;
        if (c.moveToFirst()) {
            ret = new ProfileMapper().map(c);
            c.close();
        }
        return ret;
    }
    public List<Profile> getProfiles() {
        return getProfiles(null, null);
    }
    private List<Profile> getProfiles(String selection, String[] selectionArgs) {
        Cursor c = getCursorProfile(selection, selectionArgs);
        List<Profile> ret = new ProfileMapper().mapList(c);
        c.close();
        return ret;
    }
    public Profile insertProfile(Profile profile) {
        Long id = writableDb.insert("t_profile", null, profile.asContentValues());
        return profile.withId(id);
    }
    private Cursor getCursorProfile(String selection, String[] selectionArgs) {
        return readableDb.query("t_profile", new String[] {
                "_id",
                "name",
        }, selection, selectionArgs, null, null, null);
    }
    /**  ----------- END PROFILES ----------- **/

    /**  ------------- DRAWINGS ------------- **/
    public Drawing getDrawingById(Long id) {
        Cursor c = getCursorDrawing("_id = ?", new String[] { id.toString() });
        Drawing ret = null;
        if (c.moveToFirst()) {
            ret = new DrawingMapper(ctx).map(c);
            c.close();
        }
        return ret;
    }
    public List<Drawing> getDrawingsFromProfile(Long profileId) {
        return getDrawings("profile_id = ?", new String[] { profileId.toString() });
    }
    private List<Drawing> getDrawings(String selection, String[] selectionArgs) {
        Cursor c = getCursorDrawing(selection, selectionArgs);
        List<Drawing> ret = new DrawingMapper(ctx).mapList(c);
        c.close();
        return ret;
    }
    public Drawing insertDrawing(Drawing drawing) {
        Long id = writableDb.insert("t_drawing", null, drawing.asContentValues());
        for (GeoPoint p : drawing.getPath().getPoints()) {
            p.setDrawingId(id);
        }
        insertPath(drawing.getPath());
        return drawing;
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
    public Path getPathByDrawing(Long drawingId) {
        Cursor c = getCursorGeoPointFromDrawing(drawingId);
        Path ret = new PathMapper(ctx).map(c);
        c.close();
        return ret;
    }
    public List<Long> insertPath(Path path) {
        List<Long> ids = new ArrayList<>(path.getPoints().size());
        ListIterator<GeoPoint> it = path.getPoints().listIterator();
        while (it.hasNext()) {
            Integer index = it.nextIndex();
            GeoPoint geoPoint = it.next();
            ContentValues cval = geoPoint.asContentValues(index);
            ids.add(writableDb.insert("t_point", null, cval));
        }
        return ids;
    }
    private Cursor getCursorGeoPointFromDrawing(Long drawingId) {
        return getCursorGeoPoint("drawing_id = ?", new String[] { drawingId.toString() });
    }
    private Cursor getCursorGeoPoint(String selection, String[] selectionArgs) {
        return readableDb.query("t_point", new String[] {
                "_id",
                "latitude",
                "longitude",
                "drawing_id"
        }, selection, selectionArgs, null, null, "ind ASC");
    }
    /**  ------------ END POINTS ------------ **/

    /**  ------------- CONFIGS --------------  **/
    public String getUserConfig(ConfigConstant configName) {
        Cursor c = readableDb.query("t_userconfig",
                new String[] { "name", "value" },
                "name = ?",
                new String[] { configName.val() },
                null, null, null);
        String ret = null;
        if (c.moveToFirst()) {
            ret = c.getString(c.getColumnIndex("value"));
        }
        c.close();
        return ret;
    }
    public void setUserConfig(ConfigConstant configName, String value) {
        ContentValues cval = new ContentValues();
        cval.put("name", configName.val());
        cval.put("value", value);
        if (getUserConfig(configName) == null) {
            writableDb.insert("t_userconfig", null, cval);
        } else {
            writableDb.update("t_userconfig", cval, "name = ?", new String[] { configName.val() });
        }
    }
    /**  ----------- END CONFIGS ------------  **/
}
