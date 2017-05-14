package com.example.andre.runnerartist;


import android.support.v7.app.AppCompatActivity;

import com.example.andre.runnerartist.database.DatabaseExecutor;

public abstract class GenericActivity extends AppCompatActivity {
    private DatabaseExecutor dbExecutor;
    protected DatabaseExecutor db() {
        if (dbExecutor == null) {
            dbExecutor = new DatabaseExecutor(this);
        }
        return dbExecutor;
    }
}
