package com.brannik.system;

import android.os.AsyncTask;
import android.widget.Toast;

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
public class LoginRequest extends AsyncTask<String,String,String> {
    HttpURLConnection conn;
    URL url = null;

    Globals GLOBE = new Globals(MainActivity.getAppContext());

    @Override
    protected String doInBackground(String... strings) {
        try {
            // Enter URL address where your php file resides
            String id = Globals.getDevId();
            url = new URL("http://192.168.0.101/api.php?request=login&dev_id=" + id);

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
                    String action  = reader2.getString("action");

                    if(action.equals("login")){
                        String username,fname,sname;
                        int rank,userId,notiMsg,notiRequest,active;

                        JSONObject sys  = reader2.getJSONObject("account");
                        username = sys.getString("username");
                        fname = sys.getString("name");
                        sname = sys.getString("sur_name");

                        rank = sys.getInt("rank");
                        userId = sys.getInt("user_id");
                        notiMsg = sys.getInt("noti_msg");
                        notiRequest = sys.getInt("noti_req");
                        active = sys.getInt("active");
                        GLOBE.setUserExs(1);
                        GLOBE.setCredintials(userId,username,fname,sname,rank,notiMsg,notiRequest,active);

                    }else{
                        GLOBE.setUserExs(0);

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
        Toast.makeText(MainActivity.getAppContext(), result.toString(), Toast.LENGTH_LONG).show();

    }

}
