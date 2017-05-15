package com.example.andre.runnerartist.mapper;

import android.database.Cursor;

import com.example.andre.runnerartist.model.Profile;

public class ProfileMapper extends GenericMapper<Profile> {
    @Override
    public Profile map(Cursor c) {
        return new Profile()
                .withName(c.getString(c.getColumnIndex("name")));
    }
}
