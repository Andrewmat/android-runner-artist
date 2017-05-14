package com.example.andre.runnerartist;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.Toast;

import com.example.andre.runnerartist.model.GeoPoint;
import com.example.andre.runnerartist.model.Path;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Button btnSaveLocation;

    private GoogleMap mMap;
    private Location location;
    private MarkerOptions markerOptions;
    private Path path;
    private Boolean autoSave;

    private final int LOCATION_PERMISSION_REQUEST_ID = 3002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        autoSave = false;

        btnSaveLocation = (Button) findViewById(R.id.btnSaveLocation);
        btnSaveLocation.setOnClickListener(v -> saveLocation());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        requestPermissionPosition();
        Location initialLocation = getCurrentPosition();
        if (initialLocation == null) {
            Toast.makeText(this, "A permissão de localização é necessária para o funcionamento", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(initialLocation.getLatitude(), initialLocation.getLongitude()))
                .zoom(15)
                .build()));

        location = initialLocation;
    }

    public void requestPermissionPosition() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_ID);
        }
    }

    public Location getCurrentPosition() {
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        try {
            mMap.setMyLocationEnabled(true);
            return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_ID: {
                if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    initPositionManager();
                }
                break;
            }
        }
    }

    @RequiresPermission(ACCESS_FINE_LOCATION)
    private void initPositionManager() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 30, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationChange(location);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            @Override
            public void onProviderEnabled(String provider) {

            }
            @Override
            public void onProviderDisabled(String provider) {
                finish();
            }
        });
    }

    public void locationChange(Location currLocation) {
        this.location = currLocation;
        LatLng latLng = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        if (markerOptions == null) {
            markerOptions = new MarkerOptions()
                    .title("Here");
        }
        markerOptions.position(latLng);
        if (autoSave) {
            saveLocation();
        }
    }

    public void saveLocation() {
        if (path == null) {
            path = new Path();
        }
        path.addPoint(new GeoPoint(location));
    }
}
