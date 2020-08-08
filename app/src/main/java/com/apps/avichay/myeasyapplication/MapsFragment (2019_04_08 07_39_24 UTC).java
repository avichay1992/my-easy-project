package com.apps.avichay.myeasyapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment implements OnMapReadyCallback {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    Double lat;
    Double lon;
    String title;

    boolean useSavedLocation=false;
    Double saved_lat;
    Double saved_lon;
    String saved_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v=inflater.inflate(R.layout.fragment_b, container, false);
        return v;
    }



    @Override
    public void onStart() {
        super.onStart();
        Log.e("MapsFragment","start");
        Bundle args = getArguments();
        if (args != null) {
            Log.e("FragmentB_onStart: ", "");

        }
        lat =args.getDouble("lat",MainActivity._lat);
        lon =args.getDouble("lon",MainActivity._lon);
        title = args.getString("title","");
        showDefaultLocation();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        Log.e("onMapReady","useSavedLocation="+useSavedLocation);
        if (useSavedLocation) {
            lat=saved_lat;
            lon=saved_lon;
            title=saved_title;
            useSavedLocation=false;
            showDefaultLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showDefaultLocation();
                    enableMyLocationIfPermitted();
                } else {
                    showDefaultLocation();
                }
                return;
            }
        }
    }
    private void enableMyLocationIfPermitted() {
        Log.e("enableMyLocation","start");
        if (ContextCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
//Log.e("enableMyLocation","Lat="+mMap.getMyLocation().getLatitude());
        }
    }
    private void showDefaultLocation() {

        //Toast.makeText(this, "Location permission not granted, showing default location", Toast.LENGTH_SHORT).show();
        LatLng location = new LatLng(lat, lon);
//Tel Aviv Latitude and longitude coordinates are: 32.109333, 34.855499.
        if (mMap!=null) {
            Log.e("showDefaultLocation","nMap OK");
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            mMap.addMarker(new MarkerOptions().position(location).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
        } else {
            Log.e("showDefaultLocation","nMap null");
            useSavedLocation=true;
            saved_lat=lat;
            saved_lon=lon;
            saved_title=title;
        }
    }
}
