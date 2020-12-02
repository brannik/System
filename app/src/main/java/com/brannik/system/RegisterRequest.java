package com.brannik.system;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class RegisterRequest extends AsyncTask<String,String,String> {
    HttpURLConnection conn;
    URL url = null;
    @SuppressLint("StaticFieldLeak")
    Context context = MainActivity.getAppContext();
    @SuppressLint("StaticFieldLeak")
    Globals GLOBE = new Globals(context);

    @Override
    protected String doInBackground(String... strings) {
        try {
            // Enter URL address where your php file resides
            String id = Globals.getDevId();
            String f_name=Register.getFName();
            String s_name=Register.getSName();
            String username = Register.getUserName();
            url = new URL(GLOBE.URL + "?request=register&dev_id=" + id + "&f_name=" + f_name + "&s_name=" + s_name + "&username=" + username);

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
                    String action  = reader2.getString("result");
                    if(action.equals("ok")){
                        Register.send_error("Успешно регистриран потребител");
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }else if(action.equals("error")) {
                        Register.send_error("Не са позволени празни полета!!!");
                        //android.os.Process.killProcess(android.os.Process.myPid());
                    }else{
                        // register error -> display error in textArea
                        Register.send_error(result.toString());
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
