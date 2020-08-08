package com.apps.avichay.myeasyapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements DataPassListener, LocationListener {
    public static double _lat, _lon;
    PowerConnectionReceiver receiver;
    FrameLayout frameLayout, fl2, fl1;
    MapsFragment fr;
    LocationManager locationManager;
    TextView textView;
    int orientation;
    EnternetConected enternetConected;
    android.support.v4.app.FragmentTransaction fragmentTransaction;

    public boolean isNetConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return manager != null && manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isAvailable() && manager.getActiveNetworkInfo().isConnected();
    }
    public boolean isGPSConnected(){
        LocationManager manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        return manager!=null&&manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enternetConected = new EnternetConected(this);
         isNetConnected();
         if(isNetConnected() && isGPSConnected()){

         }
         else {
             Toast.makeText(this, R.string.Network_or_GPS_dc, Toast.LENGTH_SHORT).show();
         }
         isGPSConnected();

        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                onLocationChanged(location);
            }
        }
        //todo add if for landscape
        fl2 = findViewById(R.id.search);
        fl1 = findViewById(R.id.map);
        frameLayout = findViewById(R.id.flMain);
        receiver = new PowerConnectionReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(receiver, intentFilter);
        orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            fl2.removeAllViews();
            fl1.removeAllViews();
            Log.e("onCreate:", "started: ");
            if (frameLayout != null) {
                frameLayout.removeAllViews();
            }
            Log.e("onCreate:", "after: ");
            FragmentA fragmentA = new FragmentA();
            MapsFragment mapsFragment = new MapsFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putDouble("lat", MainActivity._lat);
            args.putDouble("lon", MainActivity._lon);
            args.putString("title", getResources().getString(R.string.marker_tel_aviv));

            mapsFragment.setArguments(args);
            fragmentTransaction.replace(R.id.map, mapsFragment);
            fragmentTransaction.replace(R.id.search, fragmentA);
            fragmentTransaction.commit();
        } else {
            // In portrait
            frameLayout.removeAllViews();
            FragmentA fragmentA = new FragmentA();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flMain, fragmentA);
            fragmentTransaction.commit();
        }
//
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);
    }

    protected void onDestroy() {
        try {
            unregisterReceiver(receiver);
        } catch (Throwable t) {
        }
        super.onDestroy();
    }

    @Override
    public void passLocation(Double lat, Double lon, String title) {
        fr = new MapsFragment();
        Bundle args = new Bundle();
        args.putDouble("lat", lat);
        args.putDouble("lon", lon);
        args.putString("title", title);

        fr.setArguments(args);
        int orientation = getResources().getConfiguration().orientation;

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (!(orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            fragmentTransaction.replace(R.id.flMain, fr).addToBackStack(null);
        } else {
            fragmentTransaction.replace(R.id.map, fr);

        }
        fragmentTransaction.commit();


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("onConfigurationChanged", "Landscape");

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e("xasc", "Portrait");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            _lon = location.getLongitude();
            _lat = location.getLatitude();
        } else {
            _lat = 32.0853;
            _lon = 34.7818;
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.goToFavorites:
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // In landscape
                    fl2.removeAllViews();
                    fl1.removeAllViews();

                    PrefsFragment prefsFragment = new PrefsFragment();
                    MapsFragment mapsFragment = new MapsFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Bundle args = new Bundle();
                    args.putDouble("lat", 0.0);
                    args.putDouble("lon", 0.0);
                    args.putString("title", "Title");

                    mapsFragment.setArguments(args);
                    fragmentTransaction.replace(R.id.map, mapsFragment);
                    fragmentTransaction.replace(R.id.search, prefsFragment);
                    fragmentTransaction.commit();
                } else {
                    // In portrait
                    frameLayout.removeAllViews();
                    PrefsFragment prefsFragment = new PrefsFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.flMain, prefsFragment);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.goToMain:
                //   MainActivity.this
                orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // In landscape
                    fl2.removeAllViews();
                    fl1.removeAllViews();

                    FragmentA fragmentA = new FragmentA();
                    MapsFragment mapsFragment = new MapsFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    Bundle args = new Bundle();
                    args.putDouble("lat", 0.0);
                    args.putDouble("lon", 0.0);
                    args.putString("title", "Title");

                    mapsFragment.setArguments(args);
                    fragmentTransaction.replace(R.id.map, mapsFragment);
                    fragmentTransaction.replace(R.id.search, fragmentA);
                    fragmentTransaction.commit();
                } else {
                    // In portrait
                    frameLayout.removeAllViews();
                    FragmentA fragmentA = new FragmentA();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.flMain, fragmentA);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.menuSettings:
                Log.e("activity_menu_settings", "before");
                Intent intent = new Intent(MainActivity.this, ActivityMenuSettings.class);
                startActivity(intent);
                Log.e("activity_menu_settings", "after");

                break;
            case R.id.exit:
                Log.e("onOptionsItemSelected", "Exited");
                finish();
                break;
        }
        return true;
    }

}
