package com.brannik.system;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

public class updaterFTP extends AsyncTask<String,String,String>{
    Globals global = new Globals(MainActivity.getAppContext());
    Boolean status = false;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... strings) {
        String url = "http://app-api.redirectme.net/app/app-debug.apk";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Сваляне на ъпдейт !!!");
        request.setTitle("Версия : " + Globals.newVersion);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "app-update.apk");

        DownloadManager manager = (DownloadManager) MainActivity.getAppContext().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        return null;
    }


    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        global.setVersion(Globals.newVersion);
        Toast.makeText(MainActivity.getAppContext(),"Актуализацията завърши.Отворете папка DOWNLOADS и инсталирайте app-update.apk",Toast.LENGTH_LONG).show();
    }



}