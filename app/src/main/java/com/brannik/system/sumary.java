package com.brannik.system;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link sumary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sumary extends Fragment implements View.OnClickListener {
    TextView el;
    Button btn;

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
    public void send_data(View v){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View inf = inflater.inflate(R.layout.fragment_sumary, container, false);
        TextView hours = (TextView) inf.findViewById(R.id.txtHours);
        hours.setText("10");

        TextView days = (TextView) inf.findViewById(R.id.txtDays);
        days.setText("3");

        TextView dev_id = (TextView) inf.findViewById(R.id.txtDev);
        dev_id.setText(mParam1);

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
        return inf;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateApp:
                Log.d("DEBUG","start updating app");

                new updaterFTP().execute();
                break;
        }
    }
}