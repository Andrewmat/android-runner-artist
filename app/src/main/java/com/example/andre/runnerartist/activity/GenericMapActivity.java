package com.example.andre.runnerartist.activity;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.example.andre.runnerartist.database.DatabaseAsyncExecutor;
import com.example.andre.runnerartist.model.GeoPoint;
import com.example.andre.runnerartist.model.DrawingPath;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public abstract class GenericMapActivity extends FragmentActivity {

    private final int LOCATION_PERMISSION_REQUEST_ID = 3002;
    private Runnable whenGranted, whenDenied;

    private DatabaseAsyncExecutor dbExecutor;
    protected DatabaseAsyncExecutor db() {
        if (dbExecutor == null) {
            dbExecutor = new DatabaseAsyncExecutor(this);
        }
        return dbExecutor;
    }

    protected void requestMapsPermission(Runnable inGrantedPermission, Runnable inDeniedPermission) {
        whenGranted = inGrantedPermission;
        whenDenied = inDeniedPermission;
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_ID);
        } else {
            whenGranted.run();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_ID: {
                if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    whenGranted.run();
                } else {
                    whenDenied.run();
                }
                break;
            }
        }
    }

    public Boolean checkLocationPermissionGranted() {
        return true;
    }

    protected void centerOnPath(DrawingPath drawingPath, GoogleMap map) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (GeoPoint p : drawingPath.getPoints()) {
            builder.include(p.asLatLng());
        }
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 30));
    }
}
