package com.example.andre.runnerartist.mapper;

import android.content.Context;
import android.database.Cursor;

import com.example.andre.runnerartist.model.Drawing;

public class DrawingMapper extends GenericMapper<Drawing> {
    public DrawingMapper(Context ctx) {
        super(ctx);
    }
    @Override
    public Drawing map(Cursor c) {
        try {
            return new Drawing()
                    .withDescription(c.getString(c.getColumnIndex("description")))
                    .withCycle(c.getInt(c.getColumnIndex("cycle")) == 1)
                    .withStartCreationTime(c.getLong(c.getColumnIndex("start")))
                    .withFinishCreationTime(c.getLong(c.getColumnIndex("finish")))
                    .withProfile(db().getProfileById(c.getInt(c.getColumnIndex("profile_id"))))
                    .withPath(db().getPathByDrawing(c.getInt(c.getColumnIndex("_id"))));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
