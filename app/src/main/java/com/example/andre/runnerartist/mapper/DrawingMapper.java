package com.example.andre.runnerartist.mapper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.andre.runnerartist.model.Drawing;

public class DrawingMapper extends GenericMapper<Drawing> {
    public DrawingMapper(Context ctx) {
        super(ctx);
    }
    public DrawingMapper() {
        super();
    }
    @Override
    public Drawing map(Cursor c) {
        try {
            return new Drawing()
                    .withId(c.getLong(c.getColumnIndex("_id")))
                    .withDescription(c.getString(c.getColumnIndex("description")))
                    .withCycle(c.getInt(c.getColumnIndex("cycle")) == 1)
                    .withStartCreationTime(c.getLong(c.getColumnIndex("start")))
                    .withFinishCreationTime(c.getLong(c.getColumnIndex("finish")))
                    .withProfile(db().getProfileById(c.getLong(c.getColumnIndex("profile_id"))))
                    .withPath(db().getPathByDrawing(c.getLong(c.getColumnIndex("_id"))));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ContentValues toContentValues(Drawing drawing, Boolean withId) {
        ContentValues cval = new ContentValues();
        if (withId) {
            cval.put("_id", drawing.getId());
        }
        cval.put("description", drawing.getDescription());
        cval.put("cycle", drawing.getCycle());
        cval.put("start", drawing.getStartCreationTime());
        cval.put("finish", drawing.getFinishCreationTime());
        cval.put("profile_id", drawing.getProfile().getId());
        return cval;
    }
}
