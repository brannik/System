package com.brannik.system;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.brannik.system.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {
    public static Context appContext;
    public static Context getAppContext(){return appContext;}
    public static Intent i;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appContext = getApplicationContext();
        Globals GLOBE = new Globals(appContext);
        new LoginRequest().execute();

        wait(1000);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(MainActivity.this, reciever.class));

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