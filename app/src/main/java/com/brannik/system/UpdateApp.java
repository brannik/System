package com.brannik.system;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@SuppressWarnings("ALL")
public class UpdateApp extends AsyncTask<String,String,String> {
    HttpURLConnection conn;
    URL url = null;
    Context appContext = MainActivity.getAppContext();

    Globals GLOBE = new Globals(MainActivity.getAppContext());

    @Override
    protected String doInBackground(String... strings) {
        try {
            // Enter URL address where your php file resides
            int id = GLOBE.getAccId();
            url = new URL(GLOBE.URL + "?request=update");

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.toString();
        }
        try {

            // Setup HttpURLConnection class to send and receive data from php
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(1000);
            conn.setConnectTimeout(1000);
            conn.setRequestMethod("GET");

            // setDoOutput to true as we recieve data from json file
            conn.setDoOutput(true);

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return e1.toString();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                try {
                    JSONObject reader2 = new JSONObject(String.valueOf(result));
                    int action  = reader2.getInt("last_version");
                    String infoText = reader2.getString("info");
                    GLOBE.setVersionInfo(infoText);
                    int appVersion = GLOBE.getCurrVersion();
                    if(action != appVersion){
                        GLOBE.setNeedUpdate(1);
                        GLOBE.newVersion = action;

                    }else{
                        GLOBE.setNeedUpdate(0);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Pass data to onPostExecute method
                return (result.toString());

            } else {

                return ("unsuccessful");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        } finally {
            conn.disconnect();
        }


    }

    // this method will interact with UI, display result sent from doInBackground method
    @Override
    protected void onPostExecute(String result) {

    }

}

