package com.apps.avichay.myeasyapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchAsyncTask extends AsyncTask<Void, Void, String> {

    public Context context;
    public String query;
    public WorkOnJson workOnJson=null;
    private String apiKey="AIzaSyDsCQCd9qc79DvSqukqk154eaTPo4WGIeI"; //"AIzaSyB8-1JHEpq3UzdwIA4h0DykZtOXpe6_xFw";
    public ListAdapter listAdapter;
    public MyDBHelper myDBHelper;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... objects) {
        Log.e("doInBackground", "Started");
        JsonParser downloadFromWeb = new JsonParser();
        //String json = downloadFromWeb.makeHttpRequest("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="+query+"&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key="+apiKey, "get", null);
       // String json = downloadFromWeb.makeHttpRequest("https://maps.googleapis.com/maps/api/place/textsearch/json?input="+query+"&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key="+apiKey, "get", null);
        String json = null;
        try {
            json = downloadFromWeb.makeHttpRequest("https://maps.googleapis.com/maps/api/place/textsearch/json?input=" + URLEncoder.encode(query, "UTF-8") + "&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key="+apiKey, "get", null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e("SearchAsyncTask","json="+json);
        // we got the results
        if (workOnJson!=null) {
            workOnJson.weGotTheResult(json);
        }
        return json;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listAdapter.swapCursor(myDBHelper.getData());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        myDBHelper = new MyDBHelper(context);

    }
}