package com.example.andre.runnerartist.activity;


import android.support.v7.app.AppCompatActivity;

import com.example.andre.runnerartist.database.DatabaseAsyncExecutor;

public abstract class GenericAppCompatActivity extends AppCompatActivity {
    private DatabaseAsyncExecutor dbExecutor;
    protected DatabaseAsyncExecutor db() {
        if (dbExecutor == null) {
            dbExecutor = new DatabaseAsyncExecutor(this);
        }
        return dbExecutor;
    }
}
