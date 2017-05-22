package com.example.andre.runnerartist.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.andre.runnerartist.R;
import com.example.andre.runnerartist.model.Drawing;
import com.example.andre.runnerartist.model.GeoPoint;
import com.example.andre.runnerartist.model.DrawingPath;
import com.example.andre.runnerartist.model.Profile;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends GenericMapActivity implements OnMapReadyCallback {
    private final Context ctx = this;

    private Button btnPinpoint;
    private Button btnSaveLocation;
    private LinearLayout cntMapsButtons;

    private GoogleMap mMap;
    private Location location;
    private LocationManager lm;
    private PolylineOptions polylineOptions;

    private Drawing drawing;
    private Boolean autoSave;
    private Boolean isShowing;

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
        cntMapsButtons = (LinearLayout) findViewById(R.id.cntMapsButtons);

        btnPinpoint.setOnClickListener(v -> saveLocation());
        btnSaveLocation.setOnClickListener(v -> {
            if (drawing.getDrawingPath().getPoints() != null && !drawing.getDrawingPath().getPoints().isEmpty()) {
                Intent intent = new Intent(this, InsertDrawingActivity.class);
                intent.putExtra("drawing", drawing.withFinishCreationTime(System.currentTimeMillis()));
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Um ponto precisa ser alocado para salvar", Toast.LENGTH_SHORT).show();
            }
        });
        Intent in = getIntent();
        if (in.getSerializableExtra("drawing") != null) {
            drawing = (Drawing) in.getSerializableExtra("drawing");
            isShowing = true;
            cntMapsButtons.setVisibility(View.GONE);
        } else {
            isShowing = false;
            autoSave = in.getBooleanExtra("autosave", true);
            Profile profile = (Profile) in.getSerializableExtra("profile");
            drawing = new Drawing()
                    .withPath(new DrawingPath())
                    .withProfile(profile)
                    .withCycle(false)
                    .withStartCreationTime(System.currentTimeMillis());
            if (autoSave) {
                btnPinpoint.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        polylineOptions = new PolylineOptions()
                .width(8)
                .color(Color.BLUE);
        if (isShowing) {
            for (GeoPoint p : drawing.getDrawingPath().getPoints()) {
                polylineOptions.add(new LatLng(p.getLat(), p.getLng()));
            }
            mMap.setOnMapLoadedCallback(() -> centerOnPath(drawing.getDrawingPath(), mMap));
        } else {
            requestMapsPermission(
                    // on acceptance
                    this::initPositionManager,
                    // on denial
                    () -> {
                        Toast.makeText(this, "A permissão de localização é necessária para o funcionamento", Toast.LENGTH_LONG).show();
                        finish();
                    }
            );
        }
        mMap.addPolyline(polylineOptions);
    }

    private void initPositionManager() {
        lm = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            finish();
            return;
        }
        final AlertDialog alertDisabledGPS = new AlertDialog.Builder(ctx)
                .setTitle("Aviso")
                .setMessage("Habilite seu GPS para continuar")
                .create();

        final MapsActivity self = this;
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 3, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (self.location == null) {
                    // first time
                    initLocation(location);
                } else {
                    self.location = location;
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
                if (autoSave) {
                    saveLocation();
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                if (status == LocationProvider.OUT_OF_SERVICE) {
                    alertDisabledGPS.show();
                } else if (status == LocationProvider.AVAILABLE) {
                    alertDisabledGPS.dismiss();
                }
            }
            @Override
            public void onProviderEnabled(String provider) {
                alertDisabledGPS.dismiss();
            }
            @Override
            public void onProviderDisabled(String provider) {
                alertDisabledGPS.show();
            }
        });

        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alertDisabledGPS.show();
        } else {
            Location initialLocation;
            mMap.setMyLocationEnabled(true);
            lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            initialLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (initialLocation != null) {
                initLocation(initialLocation);
            }
        }
        if (!autoSave) {
            mMap.setOnMapClickListener(latLng -> saveLocation());
        }
    }
    private void initLocation(Location location) {
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(20)
                .build()));

        this.location = location;
    }

    private void saveLocation() {
        if (location != null) {
            drawing.getDrawingPath().addPoint(new GeoPoint(location));
            polylineOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));
            mMap.clear();
            mMap.addPolyline(polylineOptions);
        } else {
            Toast.makeText(this, "Desculpe, ainda não temos sua localização", Toast.LENGTH_LONG).show();
        }
    }
}
