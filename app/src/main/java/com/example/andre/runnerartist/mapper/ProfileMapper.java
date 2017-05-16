package com.example.andre.runnerartist.mapper;

import android.content.ContentValues;
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

    @Override
    public ContentValues toContentValues(Profile profile, Boolean withId) {
        ContentValues cval = new ContentValues();
        if (withId) {
            cval.put("_id", profile.getId());
        }
        cval.put("name", profile.getName());
        return cval;
    }
}
