package com.brannik.system;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.brannik.system.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;

import static android.Manifest.permission_group.CAMERA;

public class MainActivity extends AppCompatActivity {
    public static Context appContext;
    public static Context getAppContext(){return appContext;}
    public static Intent i;

    @SuppressLint("HardwareIds")

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    public static void startUpdate(){

        wait(5000);
        Toast.makeText(appContext,"Отидете в папка DOWNLOADS на вашия телефон и инсталирайте update.apk!!!",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        appContext = getApplicationContext();
        verifyStoragePermissions(MainActivity.this);
        Globals GLOBE = new Globals(appContext);
        new LoginRequest().execute();
        new UpdateApp().execute();

        wait(1000);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(MainActivity.this, reciever.class));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }

        int check = GLOBE.userExsi();
        if(check == 1){
            // display data
            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);
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