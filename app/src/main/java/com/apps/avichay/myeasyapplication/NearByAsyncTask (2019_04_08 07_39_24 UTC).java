package com.apps.avichay.myeasyapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NearByAsyncTask extends AsyncTask<Void, Void, String> {

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context context;
    public static double _lat, _lon;
    public String query;
    public String apiKey = "AIzaSyDsCQCd9qc79DvSqukqk154eaTPo4WGIeI";
    public WorkOnJson workOnJson = null;
    public ListAdapter listAdapter;
    public MyDBHelper myDBHelper;

    @Override
    protected String doInBackground(Void... voids){
        try {
            _lat = MainActivity._lat;
            _lon = MainActivity._lon;
            String apiTemplat = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+_lat+","+_lon+"&radius=1500&type=all&keyword=" + URLEncoder.encode(query, "UTF-8") + "&key="+apiKey;
            Log.e("doInBackground", "Started");
            JsonParser downloadFromWeb = new JsonParser();
            String json = downloadFromWeb.makeHttpRequest(apiTemplat, "get", null);
            Log.e("SearchAsyncTask", "json=" + json);

            // we got the results
            if (workOnJson != null) {
                workOnJson.weGotTheNearByResult(json);
            }
            return json;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "{}";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        myDBHelper = new MyDBHelper(context);

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listAdapter.swapCursor(myDBHelper.getData());
    }
}