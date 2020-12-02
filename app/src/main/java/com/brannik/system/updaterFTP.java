package com.brannik.system;
import android.os.AsyncTask;

@SuppressWarnings("ALL")
public class updaterFTP extends AsyncTask<String,String,String>{

    Globals global = new Globals(MainActivity.getAppContext());


    @Override
    protected String doInBackground(String... strings) {

        // toDO download from ftp and install app

        return null;

    }



}