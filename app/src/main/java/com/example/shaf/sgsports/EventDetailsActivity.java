package com.example.shaf.sgsports;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap mMap;
    Bitmap mapImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.mapview);
//        mapFragment.getMapAsync(this);


        mapView = findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng ntu = new LatLng(-1.3483, 103.6831);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ntu,4));
        mMap.addMarker(new MarkerOptions().position(ntu).title("NTU Singapore"));

//        UiSettings uiSettings = mMap.getUiSettings();
//        uiSettings.setCompassEnabled(true);
//        uiSettings.setZoomControlsEnabled(true);
//        uiSettings.setRotateGesturesEnabled(true);
//        uiSettings.setZoomGesturesEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ntu,4));
    }



}
