package com.apps.avichay.myeasyapplication;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("Registered")
public class ActivityMenuSettings extends AppCompatActivity {
    Button deleteFavoritesList,deleteResultsHistory,distanceType;
    MyDBFavorites myDBFavorites;
    MyDBHelper myDBHelper;
    int menuStage;
    public static boolean option1 = true;
    public static boolean isKM = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_settings);
        Log.e("CreateSettingsActivity" ,"started");
        deleteFavoritesList = findViewById(R.id.deleteAllFavorites);
        deleteResultsHistory = findViewById(R.id.deleteAllResults);
        distanceType = findViewById(R.id.distanceType);

        distanceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isKM!=false){
                    isKM = false;
                    Toast.makeText(getApplicationContext(), "km", Toast.LENGTH_SHORT).show();

                }

                else{
                    isKM=true;
                    Toast.makeText(getApplicationContext(), "Miles", Toast.LENGTH_SHORT).show();

                }
            }
        });

        deleteFavoritesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDBFavorites = new MyDBFavorites(ActivityMenuSettings.this);
                myDBFavorites.removeAllResults();
                Toast.makeText(getApplicationContext(), R.string.favorites_list_cleared,Toast.LENGTH_LONG).show();
            }
        });

        deleteResultsHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onOptionsItemSelected", "started");
                myDBHelper = new MyDBHelper(ActivityMenuSettings.this);
                myDBHelper.removeAllResults();
                Toast.makeText(getApplicationContext(), R.string.data_cleared,Toast.LENGTH_LONG).show();
                menuStage = 1;
            }
        });

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
            case R.id.goToMain:
             Intent intent = new Intent(ActivityMenuSettings.this,MainActivity.class);
             startActivity(intent);
                Toast.makeText(getApplicationContext(), R.string.search_info_cleared,Toast.LENGTH_LONG).show();
                break;
            case R.id.goToFavorites:
                option1 = false;
                Intent intent1 = new Intent(ActivityMenuSettings.this,MainActivity.class);
                startActivity(intent1);
                break;
            case R.layout.activity_menu_settings:
                Intent intent2 = new Intent(ActivityMenuSettings.this, ActivityMenuSettings.class);
                startActivity(intent2);
            case R.id.exit:
                Log.e("onOptionsItemSelected", "Exited");
                finish();
                break;
        }
        return true;
    }
    public void showAlertDialogButtonClicked(View view) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Show Locantion By:");
        builder.setMessage("choose your distance type:");

        // add a button
        builder.setPositiveButton("KM", null);
        builder.setNeutralButton("MILES",null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

