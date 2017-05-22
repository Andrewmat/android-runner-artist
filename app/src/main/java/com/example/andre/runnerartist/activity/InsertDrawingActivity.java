package com.example.andre.runnerartist.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andre.runnerartist.R;
import com.example.andre.runnerartist.model.Drawing;
import com.example.andre.runnerartist.model.GeoPoint;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;

public class InsertDrawingActivity extends GenericMapActivity implements OnMapReadyCallback {

    private TextView txvInsertDrawingDistance;
    private EditText edtDescriptionInsertDrawing;
    private Button btnInsertDrawing;

    private GoogleMap mMap;

    private Drawing drawing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_drawing);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapInsertDrawing);
        mapFragment.getMapAsync(this);

        txvInsertDrawingDistance = (TextView) findViewById(R.id.txvInsertDrawingDistance);
        edtDescriptionInsertDrawing = (EditText) findViewById(R.id.edtDescriptionInsertDrawing);
        btnInsertDrawing = (Button) findViewById(R.id.btnInsertDrawing);

        Intent in = getIntent();
        drawing = (Drawing) in.getSerializableExtra("drawing");

        txvInsertDrawingDistance.setText(new DecimalFormat("#.##").format(drawing.getDrawingPath().distance()) + "km");

        btnInsertDrawing.setOnClickListener(v -> {
            db().insertDrawing(drawing
                    .withDescription(edtDescriptionInsertDrawing.getText().toString())
            );
            finish();
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        PolylineOptions polylineOptions = new PolylineOptions()
                .width(8)
                .color(Color.GREEN);
        centerOnPath(drawing.getDrawingPath(), mMap);
        for (GeoPoint p : drawing.getDrawingPath().getPoints()) {
            polylineOptions.add(p.asLatLng());
        }
        mMap.addPolyline(polylineOptions);
    }
}
