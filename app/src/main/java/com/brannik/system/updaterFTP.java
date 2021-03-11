package com.brannik.system;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

@SuppressWarnings("ALL")
public class updaterFTP extends AsyncTask<String,String,String>{
    Globals global = new Globals(MainActivity.getAppContext());
    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... strings) {

        return null;
    }

    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        global.setVersion(global.newVersion);
        // run install of new app and close this
        MainActivity.startUpdate();

    }


}