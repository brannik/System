package com.brannik.system;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class updaterFTP extends AsyncTask<String,String,String>{
    Globals global = new Globals(MainActivity.getAppContext());
    Boolean status = false;
    Dialog messageDialog;
    View v;
    public updaterFTP(View v) {
        this.v = v;
        messageDialog = new Dialog(v.getContext());
    }

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
        String filename = "app-update-V" + Globals.newVersion + ".apk";
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

        DownloadManager manager = (DownloadManager) MainActivity.getAppContext().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        return null;
    }


    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        global.setVersion(Globals.newVersion);
        int newVersion = Globals.newVersion;
        String msg = "Актуализацията завърши.Отворете папка DOWNLOADS и инсталирайте app-update-V" + newVersion +".apk";
        showMessage(msg);
    }

    public void showMessage(String msg) {
        messageDialog.setContentView(R.layout.message_popup);
        TextView text = (TextView) messageDialog.findViewById(R.id.txtMessage);
        text.setText(msg);
        Button btnClose = (Button) messageDialog.findViewById(R.id.btnOk);
        TextView verInfo = (TextView) messageDialog.findViewById(R.id.textVersionInfo);
        String versionInfo = global.getCurrVersionInfo();
        if(!versionInfo.equals("none")){
            verInfo.setVisibility(View.VISIBLE);
            verInfo.setText(versionInfo);
        }
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageDialog.dismiss();
                System.exit(0);
            }
        });
        messageDialog.show();

    }


}