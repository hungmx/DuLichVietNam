package com.dhthuyloi.mxhung.dulichvietnam.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dhthuyloi.mxhung.dulichvietnam.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by MXHung on 1/6/2017.
 */

public class TestActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        onMapReady(mMap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        MarkerOptions location = new MarkerOptions();
        LatLng diadanh = new LatLng(21.007181,105.823288);
        CameraUpdate moveCamera = CameraUpdateFactory.newLatLngZoom(diadanh,15);
        mMap.animateCamera(moveCamera,3000,null);
        location.draggable(true);
        location.position(diadanh);
//        location.title(name);
        Marker marker = mMap.addMarker(location);
        marker.showInfoWindow();

        LatLng diadanh1 = new LatLng(21.021768,105.809742);

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.add(diadanh);
        polylineOptions.add(diadanh1);
        mMap.addPolyline(polylineOptions);
    }
}
