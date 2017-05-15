package com.example.andre.runnerartist.control;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public abstract class RequestMapsPermissionActivity extends FragmentActivity {

    private final int LOCATION_PERMISSION_REQUEST_ID = 3002;
    private Runnable whenGranted, whenDenied;

    public void requestMapsPermission(Runnable inGrantedPermission, Runnable inDeniedPermission) {
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
}
