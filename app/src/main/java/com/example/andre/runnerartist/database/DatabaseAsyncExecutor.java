package com.example.andre.runnerartist.database;

import android.content.Context;
import android.os.AsyncTask;

import com.example.andre.runnerartist.model.Drawing;
import com.example.andre.runnerartist.model.Path;
import com.example.andre.runnerartist.model.Profile;

import java.util.List;
import com.google.common.base.Function;

public class DatabaseAsyncExecutor extends DatabaseExecutor {
    public DatabaseAsyncExecutor(Context ctx) {
        super(ctx);
    }

    /**  ------------- PROFILES --------------  **/
    public void getProfiles(Function<List<Profile>, Void> callback) {
        new AsyncTaskImpl<List<Profile>>() {
            @Override
            protected List<Profile> doInBackground(Void... params) {
                return getProfiles();
            }
        }.callback(callback).execute();
    }
    public void insertProfile(Profile profile, Function<Profile, Void> callback) {
        new AsyncTaskImpl<Profile>() {
            @Override
            protected Profile doInBackground(Void... params) {
                return insertProfile(profile);
            }
        }.callback(callback).execute();
    }
    /**  ----------- END PROFILES ------------  **/

    /**  ------------- DRAWINGS --------------  **/
    public void getDrawingById(Long id, Function<Drawing, Void> callback) {
        new AsyncTaskImpl<Drawing>() {
            @Override
            protected Drawing doInBackground(Void... params) {
                return getDrawingById(id);
            }
        }.callback(callback).execute();
    }
    public void getDrawingsFromProfile(Long profileId, Function<List<Drawing>, Void> callback) {
        new AsyncTaskImpl<List<Drawing>>() {
            @Override
            protected List<Drawing> doInBackground(Void... params) {
                return getDrawingsFromProfile(profileId);
            }
        }.callback(callback).execute();
    }
    public void insertDrawing(Drawing drawing, Function<Drawing, Void> callback) {
        new AsyncTaskImpl<Drawing>() {
            @Override
            protected Drawing doInBackground(Void... params) {
                return insertDrawing(drawing);
            }
        }.callback(callback).execute();
    }
    /**  ----------- END DRAWINGS ------------  **/

    /**  -------------- POINTS --------------  **/
    public void getPathByDrawing(Long drawingId, Function<Path, Void> callback) {
        new AsyncTaskImpl<Path>() {
            @Override
            protected Path doInBackground(Void... params) {
                return getPathByDrawing(drawingId);
            }
        }.callback(callback).execute();
    }
    public void insertPath(Path path, Function<List<Long>, Void> callback) {
        new AsyncTaskImpl<List<Long>>() {
            @Override
            protected List<Long> doInBackground(Void... params) {
                return insertPath(path);
            }
        }.callback(callback).execute();
    }
    /**  ------------ END POINTS ------------  **/

    /**  ------------- CONFIGS --------------  **/
    public void getUserConfig(String name, Function<String, Void> callback) {
        new AsyncTaskImpl<String>() {
            @Override
            protected String doInBackground(Void... params) {
                return getUserConfig(name);
            }
        }.callback(callback).execute();
    }
    public void setUserConfig(String name, String value, Function<Void, Void> callback) {
        new AsyncTaskImpl<Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                setUserConfig(name, value);
                return null;
            }
        }.callback(callback).execute();
    }
    /**  ----------- END CONFIGS ------------  **/


    private abstract class AsyncTaskImpl<R> extends AsyncTask<Void, Void, R> {
        private Function<R, Void> _callback;
        public AsyncTaskImpl<R> callback(Function<R, Void> callback) {
            this._callback = callback;
            return this;
        }
        @Override
        protected void onPostExecute(R result) {
            _callback.apply(result);
        }
    }
}
