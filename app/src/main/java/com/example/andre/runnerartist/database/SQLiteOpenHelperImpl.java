package com.example.andre.runnerartist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteOpenHelperImpl extends SQLiteOpenHelper {
    public static final String DB_NAME = "RUNNER_ARTIST_MAIN";
    public static final Integer DB_VERSION = 1;

    public SQLiteOpenHelperImpl(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table t_profile(" +
                "_id integer primary key autoincrement not null," +
                "name text" +
                ")");
        db.execSQL("create table t_drawing(" +
                "_id integer primary key autoincrement not null," +
                "profile_id integer not null," +
                "description text," +
                "cycle integer not null," +
                "start integer not null," +
                "finish integer not null" +
                ")");
        db.execSQL("create table t_point(" +
                "_id integer primary key autoincrement not null," +
                "latitude real not null," +
                "longitude real not null," +
                "ind integer not null ," +
                "drawing_id integer not null," +
                "foreign key(drawing_id) references t_drawing(_id)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
