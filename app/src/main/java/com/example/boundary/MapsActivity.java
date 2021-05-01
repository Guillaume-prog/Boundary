package com.example.boundary;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder mGeocoder;

    private LatLng homePos;

    // Views
    private EditText addrText;
    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mGeocoder = new Geocoder(this);

        mMap.setMinZoomPreference(9.0f);
        mMap.setMaxZoomPreference(18.0f);

        addrText = findViewById(R.id.addreText);
        updateBtn = findViewById(R.id.updateBtn);

        addrText.setText(
                sharedPreferences.getString("addr", "Palais de l'Elysée, 75008 Paris")
        );

        updateBtn.setOnClickListener(v -> {
            main();
        });

        if (Permission.requestPermissions(this))
            addLocationFeature();

        main();
    }

    @SuppressLint("MissingPermission")
    private void addLocationFeature() {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private void main() {
        mMap.clear();

        String addr = addrText.getText().toString();
        addrText.setText(addr);

        editor.putString("addr", addr);
        editor.commit();

        homePos = getLatLng(addr);

        drawMarker(homePos);
        drawCircle(homePos, 10);
        zoomTo(homePos, 10.5f);
    }

    /* Map functions */

    private void zoomTo(LatLng pos, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, zoom));
    }

    private void drawMarker(LatLng pos) {
        Permission.statusCheck(this);

        MarkerOptions mo = new MarkerOptions().position(pos);
        mMap.addMarker(mo);
    }

    private void drawCircle(LatLng pos, double radius) {
        CircleOptions co = new CircleOptions().center(pos).radius(radius * 1000);
        mMap.addCircle(co);
    }

    private LatLng getLatLng(String address) {
        LatLng pos = new LatLng(0, 0);
        try {
            Address a = mGeocoder.getFromLocationName(address, 1).get(0);
            pos = new LatLng(a.getLatitude(), a.getLongitude());
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            return pos;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(Permission.accepted(requestCode, grantResults))
            addLocationFeature();
    }
}