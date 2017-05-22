package com.example.andre.runnerartist.database;

import android.content.Context;
import android.os.AsyncTask;

import com.example.andre.runnerartist.misc.ConfigConstant;
import com.example.andre.runnerartist.model.Drawing;
import com.example.andre.runnerartist.model.DrawingPath;
import com.example.andre.runnerartist.model.Profile;

import java.util.List;
import com.google.common.base.Function;

public class DatabaseAsyncExecutor extends DatabaseExecutor {
    public DatabaseAsyncExecutor(Context ctx) {
        super(ctx);
    }

    /**  ------------- PROFILES --------------  **/
    public void getProfiles(Function<List<Profile>, Void> callback) {
        new AsyncTaskImpl<List<Profile>>()
                .task(v -> getProfiles())
                .callback(callback)
                .execute();
    }
    public void getProfileById(Long id, Function<Profile, Void> callback) {
        new AsyncTaskImpl<Profile>()
                .task(v -> getProfileById(id))
                .callback(callback)
                .execute();
    }
    public void insertProfile(Profile profile, Function<Profile, Void> callback) {
        new AsyncTaskImpl<Profile>()
                .task(v -> insertProfile(profile))
                .callback(callback)
                .execute();
    }
    /**  ----------- END PROFILES ------------  **/

    /**  ------------- DRAWINGS --------------  **/
    public void getDrawingById(Long id, Function<Drawing, Void> callback) {
        new AsyncTaskImpl<Drawing>()
                .task(v -> getDrawingById(id))
                .callback(callback)
                .execute();
    }
    public void getDrawingsFromProfile(Long profileId, Function<List<Drawing>, Void> callback) {
        new AsyncTaskImpl<List<Drawing>>()
                .task(v -> getDrawingsFromProfile(profileId))
                .callback(callback)
                .execute();
    }
    public void deleteDrawing(Long id, Function<Void, Void> callback) {
        new AsyncTaskImpl<Void>()
                .task(v -> {
                    deleteDrawing(id);
                    return null;
                })
                .callback(callback)
                .execute();
    }
    public void insertDrawing(Drawing drawing, Function<Drawing, Void> callback) {
        new AsyncTaskImpl<Drawing>()
                .task(v -> insertDrawing(drawing))
                .callback(callback)
                .execute();
    }
    /**  ----------- END DRAWINGS ------------  **/

    /**  -------------- POINTS --------------  **/
    public void getPathByDrawing(Long drawingId, Function<DrawingPath, Void> callback) {
        new AsyncTaskImpl<DrawingPath>()
                .task(v -> getPathByDrawing(drawingId))
                .callback(callback)
                .execute();
    }
    public void insertPath(DrawingPath drawingPath, Function<List<Long>, Void> callback) {
        new AsyncTaskImpl<List<Long>>()
                .task(v -> insertPath(drawingPath))
                .callback(callback)
                .execute();
    }
    /**  ------------ END POINTS ------------  **/

    /**  ------------- CONFIGS --------------  **/
    public void getUserConfig(ConfigConstant configName, Function<String, Void> callback) {
        new AsyncTaskImpl<String>()
                .task(v -> getUserConfig(configName))
                .callback(callback)
                .execute();
    }
    public void setUserConfig(ConfigConstant configName, String configValue, Function<Void, Void> callback) {
        new AsyncTaskImpl<Void>()
                .task(v -> {
                    setUserConfig(configName, configValue);
                    return null;
                })
                .callback(callback)
                .execute();
    }
    /**  ----------- END CONFIGS ------------  **/


    private class AsyncTaskImpl<R> extends AsyncTask<Void, Void, R> {
        private Function<Void, R> _task;
        private Function<R, Void> _callback;
        AsyncTaskImpl<R> callback(Function<R, Void> callbackFunction) {
            _callback = callbackFunction;
            return this;
        }
        AsyncTaskImpl<R> task(Function<Void, R> taskFunction) {
            _task = taskFunction;
            return this;
        }
        @Override
        protected R doInBackground(Void... params) {
            return _task.apply(null);
        }
        @Override
        protected void onPostExecute(R result) {
            _callback.apply(result);
        }
    }
}
