package com.example.andre.runnerartist.database;

import android.content.ContentValues;
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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class DatabaseExecutor {
    private SQLiteDatabase readableDb, writableDb;
    private Context ctx;

    public DatabaseExecutor(Context ctx) {
        this.ctx = ctx;
        readableDb = new SQLiteOpenHelperImpl(ctx).getReadableDatabase();
        writableDb = new SQLiteOpenHelperImpl(ctx).getWritableDatabase();
    }

    /**  ------------- PROFILES ------------- **/
    public Profile getProfileById(Long id) {
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
    public Profile insertProfile(Profile profile) {
        Long id = writableDb.insert("t_profile", null, new ProfileMapper().toContentValues(profile, false));
        return profile.withId(id);
    }
    public Integer saveProfile(Profile profile) {
        return writableDb.update("t_profile",
                new ProfileMapper().toContentValues(profile),
                "_id = " + profile.getId(), null);
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
        if (c.moveToFirst()) {
            return new DrawingMapper(ctx).map(c);
        } else {
            return null;
        }
    }
    public List<Drawing> getDrawingsFromProfile(Long profileId) {
        return getDrawings("profile_id = ?", new String[] { profileId.toString() });
    }
    public List<Drawing> getDrawings() {
        return getDrawings(null, null);
    }
    private List<Drawing> getDrawings(String selection, String[] selectionArgs) {
        return new DrawingMapper(ctx).mapList(getCursorDrawing(selection, selectionArgs));
    }
    public Drawing insertDrawing(Drawing drawing) {
        Long id = writableDb.insert("t_drawing", null, new DrawingMapper().toContentValues(drawing, false));
        for (GeoPoint p : drawing.getPath().getPoints()) {
            p.setDrawingId(id);
        }
        insertPath(drawing.getPath());
        return drawing;
    }
    public Integer saveDrawing(Drawing drawing) {
        return writableDb.update("t_profile",
                new DrawingMapper().toContentValues(drawing),
                "_id = " + drawing.getId(), null);
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
        return new PathMapper(ctx).map(getCursorGeoPointFromDrawing(drawingId));
    }
    public List<Long> insertPath(Path path) {
        List<Long> ids = new ArrayList<>(path.getPoints().size());
        ListIterator<GeoPoint> it = path.getPoints().listIterator();
        while (it.hasNext()) {
            Integer index = it.nextIndex();
            GeoPoint geoPoint = it.next();
            ContentValues cval = new GeoPointMapper().toContentValues(geoPoint, false);
            cval.put("ind", index);
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
}
