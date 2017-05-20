package com.example.andre.runnerartist.mapper;

import android.database.Cursor;

import com.example.andre.runnerartist.model.Profile;

public class ProfileMapper extends GenericMapper<Profile> {
    public ProfileMapper() {
        super();
    }
    @Override
    public Profile map(Cursor c) {
        return new Profile()
                .withId(c.getLong(c.getColumnIndex("_id")))
                .withName(c.getString(c.getColumnIndex("name")));
    }
}
