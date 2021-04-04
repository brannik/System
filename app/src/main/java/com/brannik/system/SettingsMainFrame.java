package com.brannik.system;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.parseInt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsMainFrame#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsMainFrame extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Dialog timePicker;
    Dialog messageDialog;
    Dialog downloadDialog;

    ProgressDialog mProgressDialog;

    public SettingsMainFrame() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment myAccount.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsMainFrame newInstance(String param1, String param2) {
        SettingsMainFrame fragment = new SettingsMainFrame();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inf = inflater.inflate(R.layout.fragment_layout_settings, container, false);
        // Inflate the layout for this fragment
        timePicker = new Dialog(this.getContext());
        messageDialog = new Dialog(this.getContext());
        downloadDialog = new Dialog(this.getContext());
        GlobalVariables globals = new GlobalVariables(MainActivity.getAppContext());

        TextView textFirst = (TextView) inf.findViewById(R.id.txtEditFirstTime);
        TextView textSecond = (TextView) inf.findViewById(R.id.txtEditSecondTime);

        Button btnUpdate = (Button) inf.findViewById(R.id.btnUpdateVersion);
        TextView txtUpdate = (TextView) inf.findViewById(R.id.txtVersion);
        checkVersionFromServer(globals, btnUpdate, txtUpdate,inf.getContext());
        textFirst.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pickTime(inf, 1);
                }
            }
        });

        textSecond.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pickTime(inf, 2);
                }
            }
        });


        return inf;
    }

    public void checkVersionFromServer(GlobalVariables global, Button btn, TextView txt,Context context) {

        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());
        String url = GlobalVariables.URL + "?request=update";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            //Log.d("DEBUG","DEBUG->" + response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                String newVersion = data.getString("last_version");
                                String newVersionInfo = data.getString("info");
                                global.setAppNewVersion(newVersion, newVersionInfo);

                            }
                            if (global.compareVersions()) {
                                btn.setText("Изтегли");
                                btn.setVisibility(View.VISIBLE);
                                btn.setPadding(15, 15, 15, 15);
                                txt.setText("Нова версия " + global.convertNewVersion());
                                txt.setTextColor(Color.parseColor("#bdbdbd"));
                                txt.setPadding(15, 15, 15, 15);
                                txt.setTextSize(20);
                            } else {
                                btn.setVisibility(View.INVISIBLE);
                                txt.setText("Това е последна версия " + global.convertCurrVersion());
                                btn.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mProgressDialog = new ProgressDialog(context);
                                mProgressDialog.setMessage("Изтегляне на версия " + global.convertNewVersionNoInfo() + ".apk");
                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                mProgressDialog.setCanceledOnTouchOutside(false);

                                mProgressDialog.setButton(ProgressDialog.BUTTON_NEUTRAL, "Инсталирай", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Toast.makeText(context,"Все още не работи",Toast.LENGTH_SHORT).show();

                                        //MainActivity.getAppContext().unregisterReceiver(this);
                                    }
                                });
                                mProgressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Затвори", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                mProgressDialog.setCancelable(true);

// execute this when the downloader must be fired
                                final DownloadTask downloadTask = new DownloadTask(context,global);
                                downloadTask.execute(GlobalVariables.FILE_URL);
                                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        downloadTask.cancel(true); //cancel the task
                                    }
                                });

                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUG", "VOLLEY ERROR -> " + error);
            }

        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }



    @SuppressLint("StaticFieldLeak")
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private final Context context;
        private PowerManager.WakeLock mWakeLock;
        private final GlobalVariables globals;
        private Button button;
        private Dialog finalDialog;
        public DownloadTask(Context context, GlobalVariables globals) {
            this.context = context;
            this.globals = globals;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            //mProgressDialog.dismiss();
            if(result != null){
                //Toast.makeText(context,, Toast.LENGTH_LONG).show();
                showMessage("Download error: "+result);
            }else{
                globals.setAppVersion(globals.getAppNewVersion());
                installApk();
                Log.d("DEBUG","Install started");
                mProgressDialog.dismiss();
                //showMessage("Изтеглянето завърши отидете в паметта на телефона/DOWNLOADS и инсталирайте новата версия - " + globals.convertNewVersionNoInfo());
            }
        }

        private void installApk() {
            try {
                String PATH = Objects.requireNonNull(MainActivity.getAppContext().getExternalFilesDir(null)).getAbsolutePath();
                File file = new File(PATH + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/app-update-" + globals.convertNewVersionNoInfo()+".apk");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= 24) {
                    Uri downloaded_apk = FileProvider.getUriForFile(MainActivity.getAppContext(), MainActivity.getAppContext().getApplicationContext().getPackageName() + ".provider", file);
                    intent.setDataAndType(downloaded_apk, "application/com.brannik.system");
                    List<ResolveInfo> resInfoList = MainActivity.getAppContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        MainActivity.getAppContext().grantUriPermission(MainActivity.getAppContext().getApplicationContext().getPackageName() + ".provider", downloaded_apk, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } else {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                    intent.setDataAndType(Uri.fromFile(file), "application/com.brannik.system");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/app-update-" + globals.convertNewVersionNoInfo()+".apk");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
    }

    public void pickTime(View view, int type) {

        timePicker.setContentView(R.layout.time_picker);
        TimePicker timPick = (TimePicker) timePicker.findViewById(R.id.TimeUI);
        Button OK = (Button) timePicker.findViewById(R.id.ButtonOKTime);
        Button CANCEL = (Button) timePicker.findViewById(R.id.ButtonCancelTime);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1) {
                    int time;
                    int min;
                    time = timPick.getHour();
                    min = timPick.getMinute();
                    TextView text = (TextView) view.findViewById(R.id.txtEditFirstTime);
                    String clock = time + ":" + min;
                    text.setText(clock);
                    text.clearFocus();
                } else {
                    int time;
                    int min;
                    time = timPick.getHour();
                    min = timPick.getMinute();
                    TextView text = (TextView) view.findViewById(R.id.txtEditSecondTime);
                    String clock = time + ":" + min;
                    text.setText(clock);
                    text.clearFocus();
                }
                timePicker.dismiss();
            }
        });

        CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.dismiss();
            }
        });

        timePicker.show();

    }


    public void showMessage(String msg) {
        messageDialog.setContentView(R.layout.dialog_message);
        TextView text = (TextView) messageDialog.findViewById(R.id.txtMessage);
        text.setText(msg);
        Button btnClose = (Button) messageDialog.findViewById(R.id.btnOk);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageDialog.dismiss();
            }
        });
        messageDialog.show();


    }
}