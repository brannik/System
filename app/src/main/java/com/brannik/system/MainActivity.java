package com.brannik.system;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import com.brannik.system.ui.main.SectionsPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;

import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    public static Context appContext;
    public static Context getAppContext(){return appContext;}
    public static boolean userExs=true;
    public static void setUserExs(boolean st){userExs = st;}
    public static boolean getUserExs(){return userExs;}
    public static String devId;
    public static String getDevId(){return devId;}
    public static Intent i;

    public static int userRank=2;
    public static void setUserRank(int i){userRank = i;}
    public static int getUserRank(){return userRank;}

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appContext = getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(MainActivity.this, reciever.class));


        devId = getUniquePsuedoID();

        //"149.62.204.110";
        String ip = getLocalIpAddress();
        // to do unique , request login with ip and devId and if acc exsist -> display tabs else display register page
        JSONObject json = new JSONObject();
        try {
            json.put("REQUEST_TYPE", "LOGIN");
            json.put("CLIENT", ip );
            json.put("DEVICE_ID", devId );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new BackgroundTask().execute(json.toString());
        new socketThread().start();
        wait(1000);

        if(getUserExs()){
            // display data
            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);

        }else{
            // display register form
            i = new Intent(MainActivity.this, Register.class);
            startActivity(i);

        }


    }

    @Override
    protected void onStop() {
        String ip = getLocalIpAddress();
        JSONObject json = new JSONObject();
        try {
            json.put("REQUEST_TYPE", "DISCONNECT");
            json.put("CLIENT", ip);
            json.put("DEVICE_ID", devId );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new BackgroundTask().execute(json.toString());
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getUniquePsuedoID() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // https://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // https://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
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