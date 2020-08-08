package com.apps.avichay.myeasyapplication;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class JsonParser {

    String charset = "UTF-8";
    HttpURLConnection conn;
    DataOutputStream wr;
    StringBuilder result;
    URL urlObj;
    JSONObject jObj = null;
    StringBuilder sbParams;
    String paramsString;
    private static final String TAG = "JsonParser";

    public String makeHttpRequest(String url, String method,
                                  HashMap<String, String> params) {

        sbParams = new StringBuilder();
        int i = 0;

        if (method.equals("POST")) {
            // request method is POST
            try {
                urlObj = new URL(url);

                conn = (HttpURLConnection) urlObj.openConnection();

                conn.setDoOutput(true);

                conn.setRequestMethod("POST");

                conn.setRequestProperty("Accept-Charset", charset);

                conn.setReadTimeout(10000); //10 seconds
                conn.setConnectTimeout(15000); //15 seconds

                conn.connect();

                paramsString = sbParams.toString();

                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();
                wr.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (method.equals("GET")) {
             url += "?" + sbParams.toString();
        }

        try {
            urlObj = new URL(url);

            conn = (HttpURLConnection) urlObj.openConnection();

            conn.setDoOutput(false);

            conn.setRequestMethod("GET");

            conn.setRequestProperty("Accept-Charset", charset);

            conn.setConnectTimeout(15000);
// if its up then 15seconds catch the Exception.
            conn.connect();

        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            //Receive the response from the server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.e("JSON Parser", "result: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.disconnect();

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(result.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        } catch (Exception e){
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON Object
        return result == null ? "{ }" : result.toString();
    }
}

//{   "candidates" : [],   "error_message" : "This IP, site or mobile application is not authorized to use this API key. Request received from IP address 79.176.64.22, with empty referer",   "status" : "REQUEST_DENIED"}