package com.brannik.system;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.brannik.system.ui.main.SectionsPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static android.Manifest.permission_group.CAMERA;
import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {
    public static Context appContext;
    public static Context getAppContext(){return appContext;}
    public static Intent i;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("HardwareIds")
    public int unchecked = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {




        appContext = getApplicationContext();
        //verifyStoragePermissions(MainActivity.this);
        Globals GLOBE = new Globals(appContext);
        new LoginRequest().execute();
        new UpdateApp().execute();

        wait(1000);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(MainActivity.this, reciever.class));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                        Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE,
                        Manifest.permission.INTERNET,
                        Manifest.permission_group.PHONE,
                        Manifest.permission.FOREGROUND_SERVICE
                }, 1);
            }
        }

        int lastDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int t = lastDay - today;

        int check = GLOBE.userExsi();
        if(check == 1){
            // display data
            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);
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
                            tabs.getTabAt(i).getOrCreateBadge().setMaxCharacterCount(4);
                            tabs.getTabAt(i).getOrCreateBadge().setNumber(unchecked);
                            tabs.getTabAt(i).getOrCreateBadge().setBackgroundColor(Color.RED);
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


        }else if(check == 0){
            // display register form
            i = new Intent(MainActivity.this, Register.class);
            startActivity(i);
        }else{
            Log.d("DEBUG","check error");
        }

    }
    private String[] neededPermissions = new String[]{CAMERA};
    private boolean checkPermission() {

        ArrayList<String> permissionsNotGranted = new ArrayList<>();

        for (String permission : neededPermissions) {

            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

                permissionsNotGranted.add(permission);

            }

        }

        if (!permissionsNotGranted.isEmpty()) {

            boolean shouldShowAlert = false;

            for (String permission : permissionsNotGranted) {

                shouldShowAlert = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);

            }

            if (shouldShowAlert) {

                showPermissionAlert(permissionsNotGranted.toArray(new String[0]));

            } else {

                requestPermissions(permissionsNotGranted.toArray(new String[0]));

            }

            return false;

        }

        return true;

    }



    private void showPermissionAlert(final String[] permissions) {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setCancelable(true);

        alertBuilder.setTitle("Permission Required");

        alertBuilder.setMessage("Camea permission is required to move forward.");

        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                requestPermissions(permissions);

            }

        });

        AlertDialog alert = alertBuilder.create();

        alert.show();

    }



    private void requestPermissions(String[] permissions) {

        ActivityCompat.requestPermissions(MainActivity.this, permissions, 1001);

    }



    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1001) {

            for (int result : grantResults) {

                if (result == PackageManager.PERMISSION_DENIED) {

                    Toast.makeText(MainActivity.this, "This permission is required", Toast.LENGTH_LONG).show();

                    checkPermission();

                    return;

                }

            }

            /* Code after permission granted */

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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