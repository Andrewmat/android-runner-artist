package com.example.andre.runnerartist.mapper;

import android.database.Cursor;

import com.example.andre.runnerartist.model.Drawing;

public class DrawingMapper extends GenericMapper<Drawing> {
    @Override
    public Drawing map(Cursor c) {
        return new Drawing()
                .withDescription(c.getString(c.getColumnIndex("description")))
                .withCycle(c.getInt(c.getColumnIndex("cycle")) == 1)
                .withStartCreationTime(c.getLong(c.getColumnIndex("start")))
                .withFinishCreationTime(c.getLong(c.getColumnIndex("finish")));
    }
}
