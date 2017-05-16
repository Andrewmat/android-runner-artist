package com.example.andre.runnerartist;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Button;
import android.widget.Toast;

import com.example.andre.runnerartist.control.RequestMapsPermissionActivity;
import com.example.andre.runnerartist.database.DatabaseExecutor;
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

    private DatabaseExecutor dbExecutor;
    protected DatabaseExecutor db() {
        if (dbExecutor == null) {
            dbExecutor = new DatabaseExecutor(this);
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
        Intent in = getIntent();
        autoSave = in.getBooleanExtra("autosave", true);
        Long profileId = in.getLongExtra("profileId", 0);

        btnPinpoint = (Button) findViewById(R.id.btnPinpoint);
        btnSaveLocation = (Button) findViewById(R.id.btnSaveLocation);

        btnPinpoint.setOnClickListener(v -> {
            saveLocation();
        });
        btnSaveLocation.setOnClickListener(v -> {
            db().insertDrawing(drawing.withFinishCreationTime(System.currentTimeMillis()));
            finish();
        });
        drawing = new Drawing()
                .withPath(new Path())
                .withProfile(db().getProfileById(profileId))
                .withCycle(false)
                .withStartCreationTime(System.currentTimeMillis());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        polylineOptions = new PolylineOptions();
        mMap.addPolyline(polylineOptions);

        requestMapsPermission(this::initPositionManager, () -> {
            Toast.makeText(this, "A permissão de localização é necessária para o funcionamento", Toast.LENGTH_LONG).show();
            finish();
        });
    }

    private void initPositionManager() {
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        Location initialLocation;
        try {
            mMap.setMyLocationEnabled(true);
            initialLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            return;
        }
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(initialLocation.getLatitude(), initialLocation.getLongitude()))
                .zoom(20)
                .build()));

        location = initialLocation;
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
        if (autoSave) {
            saveLocation();
        }
    }

    public void saveLocation() {
        drawing.getPath().addPoint(new GeoPoint(location));
        polylineOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));
    }
}
