package com.brannik.system;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

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

public class Notifications extends Service {
    Context appContext = MainActivity.getAppContext();
    Intent notificationIntent = new Intent(appContext, MainActivity.class);

    @SuppressLint("StaticFieldLeak")
    Globals GLOBE = new Globals(MainActivity.getAppContext());


    public Notifications() {
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("DEBUG","SERVICE RUNNING");
        sendRequest();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DEBUG","SERVICE STOPED");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void sendRequest(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());
        int id = GLOBE.getAccId();
        String url = Globals.URL + "?request=notify&acc_id=" + id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            //Log.d("DEBUG","DEBUG->" + response);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                int action  = data.getInt("count");
                                GLOBE.setNotificationsCount(action);

                            }

                            int count = GLOBE.getNotificationsCount();
                            String title = "Внимание !!!";
                            String text = "Имате " + count + " нови известия.";
                            showNotification(appContext,title,text,notificationIntent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Log.d("DEBUG"," -> " + response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUG", "VOLLEY ERROR -> " + error);
            }


        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

    }

    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        long[] patern = {0,100,200,500,200,100,0};
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        Uri uri= Settings.System.DEFAULT_NOTIFICATION_URI;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.notification_icon,1)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setContentText("Some content")
                .setVibrate(patern)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(uri);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(notificationId, mBuilder.build());


    }
}