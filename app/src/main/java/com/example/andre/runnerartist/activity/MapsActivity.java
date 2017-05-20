package com.example.andre.runnerartist.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.andre.runnerartist.R;
import com.example.andre.runnerartist.database.DatabaseAsyncExecutor;
import com.example.andre.runnerartist.misc.RequestMapsPermissionActivity;
import com.example.andre.runnerartist.model.Drawing;
import com.example.andre.runnerartist.model.GeoPoint;
import com.example.andre.runnerartist.model.Path;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends RequestMapsPermissionActivity implements OnMapReadyCallback {

    private Button btnPinpoint;
    private Button btnSaveLocation;

    private GoogleMap mMap;
    private Location location;
    private PolylineOptions polylineOptions;

    private Drawing drawing;
    private Boolean autoSave;
    private Boolean isShowing;

    private DatabaseAsyncExecutor dbExecutor;
    private DatabaseAsyncExecutor db() {
        if (dbExecutor == null) {
            dbExecutor = new DatabaseAsyncExecutor(this);
        }
        return dbExecutor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnPinpoint = (Button) findViewById(R.id.btnPinpoint);
        btnSaveLocation = (Button) findViewById(R.id.btnSaveLocation);

        btnPinpoint.setOnClickListener(v -> saveLocation());
        btnSaveLocation.setOnClickListener(v -> {
            db().insertDrawing(drawing.withFinishCreationTime(System.currentTimeMillis()));
            finish();
        });
        Intent in = getIntent();
        if (in.getLongExtra("drawingId", -1) != -1L) {
            drawing = db().getDrawingById(in.getLongExtra("drawingId", -1));
            isShowing = true;
            btnPinpoint.setVisibility(View.INVISIBLE);
            btnSaveLocation.setVisibility(View.INVISIBLE);
        } else {
            isShowing = false;
            autoSave = in.getBooleanExtra("autosave", true);
            Long profileId = in.getLongExtra("profileId", 0);
            db().getProfileById(profileId, p -> {
                drawing = new Drawing()
                    .withPath(new Path())
                    .withProfile(p)
                    .withCycle(false)
                    .withStartCreationTime(System.currentTimeMillis());
                return null;
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        polylineOptions = new PolylineOptions()
                .width(8)
                .color(Color.BLUE);
        if (isShowing) {
            for (GeoPoint p : drawing.getPath().getPoints()) {
                polylineOptions.add(new LatLng(p.getLat(), p.getLng()));
            }
            GeoPoint pos = drawing.getPath().getPoints().get(0);
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                    .target(new LatLng(pos.getLat(), pos.getLng()))
                    .zoom(20)
                    .build()));
        } else {
            requestMapsPermission(this::initPositionManager, () -> {
                Toast.makeText(this, "A permissão de localização é necessária para o funcionamento", Toast.LENGTH_LONG).show();
                finish();
            });
        }
        mMap.addPolyline(polylineOptions);
    }

    private void initPositionManager() {
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            finish();
            return;
        }
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            finish();
            return;
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 3, new LocationListener() {
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

        Location initialLocation;
        try {
            mMap.setMyLocationEnabled(true);
            initialLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (initialLocation == null) {
                throw new SecurityException("Null return on last know location request");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            return;
        }
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(initialLocation.getLatitude(), initialLocation.getLongitude()))
                .zoom(20)
                .build()));

        location = initialLocation;
    }

    private void locationChange(Location currLocation) {
        this.location = currLocation;
        LatLng latLng = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        if (autoSave) {
            saveLocation();
        }
    }

    private void saveLocation() {
        drawing.getPath().addPoint(new GeoPoint(location));
        polylineOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));
        mMap.clear();
        mMap.addPolyline(polylineOptions);
    }
}
