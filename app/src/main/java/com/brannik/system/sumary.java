package com.brannik.system;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link sumary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sumary extends Fragment implements View.OnClickListener {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;

    private ArrayList<String> array = new ArrayList<>();
    Globals GLOBE = new Globals(MainActivity.getAppContext());

    public sumary() {
        // Required empty public constructor

    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sumary.
     */
    // TODO: Rename and change types and number of parameters
    public static sumary newInstance(String param1, String param2,String param3,String param4) {
        sumary fragment = new sumary();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        fragment.setArguments(args);

        return fragment;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
        }



    }




    @SuppressWarnings("rawtypes")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View inf = inflater.inflate(R.layout.fragment_sumary, container, false);
        TextView hours = (TextView) inf.findViewById(R.id.txtHours);
        hours.setText("10");

        TextView days = (TextView) inf.findViewById(R.id.txtDays);
        days.setText("3");

        //TextView dev_id = (TextView) inf.findViewById(R.id.txtDev);
        //dev_id.setText(mParam1);

        TextView user_name = (TextView) inf.findViewById(R.id.txtUsername);
        user_name.setText(GLOBE.getUsername());

        TextView names = (TextView) inf.findViewById(R.id.txtNames);
        names.setText(GLOBE.getNames());

        TextView sklad = (TextView) inf.findViewById(R.id.txtSkladNumber);
        int indexSklad = GLOBE.getSklad();
        sklad.setText(GLOBE.getSkladByIndex(indexSklad));

        TextView rank = (TextView) inf.findViewById(R.id.txtRank);
        int index = GLOBE.userRank();
        rank.setText(GLOBE.getankByIndex(index));


        Button updateBtn = (Button) inf.findViewById(R.id.btnUpdateApp);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        TextView updNotify = (TextView) inf.findViewById(R.id.txtUpdateNotify);

        updateBtn.setOnClickListener(this);

        int newUpd = GLOBE.needUpdate();
        if(newUpd == 1){
            updateBtn.setVisibility(View.VISIBLE);
            updNotify.setText("Налична е нова версия !" );
        }else{
            updateBtn.setVisibility(View.GONE);
            updNotify.setText("Инсталирана е последната версия " + GLOBE.getCurrVersion());
        }


        // call function to prepare list
        prepareList();



        return inf;
    }

    private void prepareList(){
        // send volley request
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());
        int ID = GLOBE.getAccId();
        int SKLAD = GLOBE.getSklad();
        String url = Globals.URL + "?request=in_app_notify&acc_id=" + ID + "&sklad_id=" + SKLAD;
        //String url ="http://app-api.servehttp.com/api.php?request=test";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        array.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                String notText = data.getString("notification_text");
                                array.add(notText);
                            }
                            ListView listView = (ListView) getView().findViewById(R.id.listView);
                            ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.getAppContext(),
                                    android.R.layout.simple_list_item_1,
                                    array);
                            listView.setAdapter(arrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        Log.d("DEBUG"," -> " + response);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateApp:
                Log.d("DEBUG","start updating app");
                Button updBtn = (Button) v.findViewById(R.id.btnUpdateApp);
                updBtn.setEnabled(false);
                new updaterFTP().execute();
                break;
        }
    }


}