package com.brannik.system;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.brannik.system.ui.main.SectionsPagerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.REQUEST_INSTALL_PACKAGES;
import static android.Manifest.permission.WAKE_LOCK;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class MainActivity extends AppCompatActivity{
    private static final int CAMERA_PERMISSION = 100;
    public static Context appContext;
    public static Context getAppContext(){return appContext;}
    public static Intent i;
    GlobalVariables GLOBE;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("HardwareIds")
    private void generateTocken(GlobalVariables globe){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MainActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String updatedToken = instanceIdResult.getToken();
                Log.e("DEBUG","NEW TOKEN "+ updatedToken);
                globe.StoreToken(updatedToken);
                int accid = globe.getAccId();
                sendToken(accid,updatedToken);
            }
        });
    }

    private void sendToken(int accid,String token){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());
        String url = GlobalVariables.URL + "?request=update_token&acc_id=" + accid + "&token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                String total = data.getString("RESPONSE");
                                Log.d("DEBUG","TOKEN RESPONSE >>>" + total);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUG", "TOKEN VOLLEY ERROR -> " + error);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        appContext = getApplicationContext();
        //verifyStoragePermissions(MainActivity.this);
        GLOBE = new GlobalVariables(appContext);
        new LoginRequest().execute();

        Intent serviceIntent = new Intent(this, FBNotificationsReceiver.class);
        MainActivity.getAppContext().startService(serviceIntent);


        wait(1000);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.CAMERA,
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                    Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE,
                    Manifest.permission.INTERNET,
                    Manifest.permission_group.PHONE,
                    WAKE_LOCK,
                    FOREGROUND_SERVICE
            }, 1);
        }

        int lastDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int t = lastDay - today;

        int check = GLOBE.userExsi();
        if(check == 1){
            generateTocken(GLOBE);
            // display data
            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);

            tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

            int count = tabs.getTabCount();

            int[] ICONS = new int[]{
                    R.drawable.icon_home,
                    R.drawable.icon_grafik,
                    R.drawable.icon_documents,
                    R.drawable.icon_extra,
                    R.drawable.icon_settings,
                    R.drawable.icon_admin
            };
            int unchecked = GLOBE.getUnchecked();
            int notifications = GLOBE.getNotCount();
            for(int i=0;i<count;i++){
                tabs.getTabAt(i).setIcon(ICONS[i]);
                if(i == 0) {
                    if(t <= 6){
                        if(unchecked > 0) {
                            tabs.getTabAt(i).getOrCreateBadge().setMaxCharacterCount(2);
                            tabs.getTabAt(i).getOrCreateBadge().setNumber(1);
                            tabs.getTabAt(i).getOrCreateBadge().setBackgroundColor(Color.RED);
                        }
                    }
                }
                if(i == 2){
                        if(unchecked > 0) {

                            if(t < 7) {
                                tabs.getTabAt(i).getOrCreateBadge().setMaxCharacterCount(4);
                                tabs.getTabAt(i).getOrCreateBadge().setNumber(unchecked);
                                tabs.getTabAt(i).getOrCreateBadge().setBackgroundColor(Color.RED);
                            }

                        }
                }
                if(i==1){
                    if(notifications > 0) {
                        tabs.getTabAt(i).getOrCreateBadge().setMaxCharacterCount(2);
                        tabs.getTabAt(i).getOrCreateBadge().setNumber(notifications);
                        tabs.getTabAt(i).getOrCreateBadge().setBackgroundColor(Color.RED);
                    }
                }

            }

            //addNotification();

            Boolean chekFRun = GLOBE.firstRun();
            if(chekFRun){
                // on first run
                if(ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission_group.CAMERA) + ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.INTERNET) + ContextCompat.checkSelfPermission(MainActivity.this,
                        READ_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(MainActivity.this,
                        READ_PHONE_STATE) + ContextCompat.checkSelfPermission(MainActivity.this,
                        FOREGROUND_SERVICE) + ContextCompat.checkSelfPermission(MainActivity.this,
                        WRITE_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(MainActivity.this,
                        REQUEST_INSTALL_PACKAGES) + ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission_group.CALENDAR) +ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.INSTALL_PACKAGES) != PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission_group.CAMERA) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    Manifest.permission.INTERNET) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    READ_EXTERNAL_STORAGE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    READ_PHONE_STATE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    FOREGROUND_SERVICE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    WRITE_EXTERNAL_STORAGE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    REQUEST_INSTALL_PACKAGES) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    Manifest.permission.INSTALL_PACKAGES) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    Manifest.permission_group.CALENDAR)){

                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{
                                        CAMERA,
                                        READ_EXTERNAL_STORAGE,
                                        READ_PHONE_STATE,
                                        FOREGROUND_SERVICE,
                                        WRITE_EXTERNAL_STORAGE,
                                        REQUEST_INSTALL_PACKAGES,
                                        Manifest.permission.INTERNET,
                                        Manifest.permission.INSTALL_PACKAGES,
                                        Manifest.permission_group.CALENDAR
                                },
                                CAMERA_PERMISSION
                        );
                    }

                }
            }else{
                Toast.makeText(MainActivity.appContext,"Ne sa nujni permissions",Toast.LENGTH_SHORT).show();
            }


        }else if(check == 0){
            // display register form
            i = new Intent(MainActivity.this, Register.class);
            startActivity(i);
        }else{
            Log.d("DEBUG","check error");
        }

    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

}