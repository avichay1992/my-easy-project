package com.apps.avichay.myeasyapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class FragmentA extends Fragment implements WorkOnJson {
    private static final String TAG = "FragmentA";
    DataPassListener dataPassListener;
    AlertDialog.Builder alert;
    Button searchByLocation;
    Button searchByKey;
    String searchText;
    ListAdapter adapter;
    MyDBHelper myDBHelper;
    MyDBFavorites myDBFavorites;
    ListView listView;
    long item;
    String option1, option2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);
        searchByKey = view.findViewById(R.id.searchByKey);
        myDBFavorites = new MyDBFavorites(getActivity());
        myDBHelper = new MyDBHelper(getActivity());
        dataPassListener = (DataPassListener) getActivity();
        listView = view.findViewById(R.id.listLocations);
        adapter = new ListAdapter(getActivity(), myDBHelper.getData());
        alert = new AlertDialog.Builder(getActivity());
        listView.setAdapter(adapter);
        searchByKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText edittext = new EditText(getActivity());
                alert.setMessage(R.string.sKey_dialog_msg);
                alert.setTitle(R.string.sKey_title);

                alert.setView(edittext);

                alert.setPositiveButton(R.string.sKey_search, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        myDBHelper.removeAllResults();

                        searchText = edittext.getText().toString();//.trim().replace(" ","%20");
                        Log.e(TAG, "onClick: " + searchText);

                        SearchAsyncTask task = new SearchAsyncTask();
                        task.query = searchText;
                        task.context = getActivity();
                        task.workOnJson = FragmentA.this;
                        task.listAdapter = adapter;
                        task.execute();
                        adapter.swapCursor(myDBHelper.getData());

                    }
                });
                alert.show();

            }
        });
        searchByLocation = view.findViewById(R.id.searchByLocation);
        searchByLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText edittext = new EditText(getActivity());
                alert.setMessage(R.string.search_dialog);
                alert.setTitle(R.string.search_title);

                alert.setView(edittext);

                alert.setPositiveButton(R.string.search, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        myDBHelper.removeAllResults();

                        searchText = edittext.getText().toString();
                        Log.e(TAG, "onClick: " + searchText);

                        NearByAsyncTask task = new NearByAsyncTask();
                        task.query = searchText;
                        task.context = getActivity();
                        task.workOnJson = FragmentA.this;
                        task.listAdapter = adapter;
                        task.execute();
                        adapter.swapCursor(myDBHelper.getData());

                    }
                });
                alert.show();


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = myDBHelper.getDataSpecific(id);
                cursor.moveToFirst();
                int _lon = cursor.getColumnIndex("lon");
                int _lat = cursor.getColumnIndex("lat");
                int _title = cursor.getColumnIndex("location");
                double lon = cursor.getDouble(_lon);
                double lat = cursor.getDouble(_lat);
                String title = cursor.getString(_title);
                dataPassListener.passLocation(lat, lon, title);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice);
                item = id;
                arrayAdapter.add(getString(R.string.share));
                arrayAdapter.add(getString(R.string.add_to_favorites));
                option1 = getString(R.string.share);
                option2 = getString(R.string.add_to_favorites);

                builderSingle.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    //the action for the negative input by user
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //dismiss
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    //"combining" the builder with the adapter.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String opt1 = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(getContext());

                        builderInner.setMessage(opt1);
                        if (option1 == opt1) {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            Cursor cursor = myDBHelper.getDataSpecific(item);
                            cursor.moveToFirst();
                            int _address = cursor.getColumnIndex("address");
                            int _uri = cursor.getColumnIndex("uri");
                            int _title = cursor.getColumnIndex("search");
                            String title = cursor.getString(_title);
                            String address = cursor.getString(_address);
                            String uri = cursor.getString(_uri);
                            String send = title + " " + " " + address + " " + uri;
                            sendIntent.putExtra(Intent.EXTRA_TEXT, send);
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                            Toast.makeText(getContext(), R.string.shared_location, Toast.LENGTH_SHORT).show();
                        } else if (option2 == opt1) {
                            Cursor cursor = myDBHelper.getDataSpecific(item);
                            cursor.moveToFirst();
                            int _lon = cursor.getColumnIndex("lon");
                            int _lat = cursor.getColumnIndex("lat");
                            int _loc = cursor.getColumnIndex("location");
                            int _address = cursor.getColumnIndex("address");
                            int _length = cursor.getColumnIndex("length");
                            int _uri = cursor.getColumnIndex("uri");
                            int _title = cursor.getColumnIndex("search");
                            double lon = cursor.getDouble(_lon);
                            double lat = cursor.getDouble(_lat);
                            String length = cursor.getString(_length);
                            String title = cursor.getString(_title);
                            String name = cursor.getString(_loc);
                            String address = cursor.getString(_address);
                            String photo = cursor.getString(_uri);
                            myDBFavorites.addMyFLocations(title, name, address, photo, lat, lon, length);
                            Toast.makeText(getContext(), R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builderSingle.show();

                return true;
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void weGotTheResult(String s) {
        String name = "";
        String address = "";
        JSONArray uriImage;
        String length = "";
        String photo = "";
        double _length = 0.0;
        double lat;
        double lon;
        MyDBHelper myDBHelper = new MyDBHelper(getActivity().getBaseContext());
        try {
            JSONObject result = new JSONObject(s);
            JSONArray array = result.getJSONArray("results");

            if (array.length() == 0)
                showNoResultsToast();
            else {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    name = obj.getString("name");
                    address = obj.getString("formatted_address");
                    uriImage = obj.getJSONArray("photos");
                    JSONObject geometry = obj.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    lat = location.getDouble("lat");
                    lon = location.getDouble("lng");
                    _length = distance(MainActivity._lat, MainActivity._lon, lat, lon);

                    if (ActivityMenuSettings.isKM != true) {
                        _length = _length * 0.621371;
                        _length = Double.parseDouble(new DecimalFormat("#####.##").format(_length));
                        length = _length + " Miles";
                    } else {
                        _length = Double.parseDouble(new DecimalFormat("#####.##").format(_length));
                        length = _length + " Km";
                    }
                    for (int j = 0; j < uriImage.length(); j++) {
                        JSONObject obj2 = uriImage.getJSONObject(j);
                        photo = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
                        photo += obj2.getString("photo_reference") + "&key=" + "AIzaSyDsCQCd9qc79DvSqukqk154eaTPo4WGIeI";
                    }

                    myDBHelper.addMyLocations(searchText, name, address, photo, lat, lon, length);

                    Log.e("FragmentA", "s=" + s + name + " " + address);
                }
            }
        } catch (Exception e) {
            Log.e("weGotTheResult", e.toString());
        }

    }

    @Override
    public void weGotTheNearByResult(String s) {
        String name = "";
        String address = "";
        JSONArray uriImage;
        String length = "";
        String photo = "";
        double _length = 0.0;
        double lat;
        double lon;
        MyDBHelper myDBHelper = new MyDBHelper(getActivity().getBaseContext());
        try {
            JSONObject result = new JSONObject(s);
            JSONArray array = result.getJSONArray("results");

            if (array.length() == 0)
                showNoResultsToast();
            else {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    name = obj.getString("name");
                    address = obj.getString("vicinity");
                    uriImage = obj.getJSONArray("photos");
                    JSONObject geometry = obj.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    lat = location.getDouble("lat");
                    lon = location.getDouble("lng");
                    _length = distance(MainActivity._lat, MainActivity._lon, lat, lon);
                    if (ActivityMenuSettings.isKM != true) {

                        _length = _length * 0.621371;
                        _length = Double.parseDouble(new DecimalFormat("#####.##").format(_length));
                        length = _length + " Miles";
                    } else {
                        _length = Double.parseDouble(new DecimalFormat("#####.##").format(_length));
                        length = _length + " Km";
                    }
                    for (int j = 0; j < uriImage.length(); j++) {
                        JSONObject obj2 = uriImage.getJSONObject(j);
                        photo = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
                        photo += obj2.getString("photo_reference") + "&key=" + "AIzaSyDsCQCd9qc79DvSqukqk154eaTPo4WGIeI";
                    }
                    myDBHelper.addMyLocations(searchText, name, address, photo, lat, lon, length);

                    Log.e("FragmentA", "s=" + s + name + " " + address);
                }
            }

        } catch (Exception e) {
            Log.e("weGotTheResult", e.toString());
        }

    }

    private void showNoResultsToast() {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), R.string.zero_results_from_json,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private double distance(double myLat, double myLng, double placeLat, double placeLng) {

        double radiusLat1 = Math.PI * myLat / 180;
        double radiusLat2 = Math.PI * placeLat / 180;

        double delta = myLng - placeLng;

        double radiusDelta = Math.PI * delta / 180;
        double distance = Math.sin(radiusLat1) * Math.sin(radiusLat2) + Math.cos(radiusLat1) * Math.cos(radiusLat2) * Math.cos(radiusDelta);

        if (distance > 1) {
            distance = 1;
        }

        distance = Math.acos(distance);

        distance = distance * 180 / Math.PI;
        distance = distance * 60 * 1.1515;
        distance = distance * 1.609344;

        return distance;

    }


}
