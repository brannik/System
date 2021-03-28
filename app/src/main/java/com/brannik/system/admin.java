package com.brannik.system;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link admin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class admin extends Fragment {
    Globals GLOBE = new Globals(MainActivity.getAppContext());
    AdminGetAccounts admin = new AdminGetAccounts();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public admin() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment admin.
     */
    // TODO: Rename and change types and number of parameters
    public static admin newInstance(String param1, String param2) {
        admin fragment = new admin();
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
        View inf = inflater.inflate(R.layout.fragment_admin, container, false);

        Spinner rank = (Spinner) inf.findViewById(R.id.spinnerAccRank);
        ArrayAdapter<String> rankAdapter = new ArrayAdapter<String>(inf.getContext(), android.R.layout.simple_spinner_dropdown_item,admin.getRankNames());
        rank.setAdapter(rankAdapter);


        Spinner sklad = (Spinner) inf.findViewById(R.id.spinnerAccSklad);
        ArrayAdapter<String> skladAdapter = new ArrayAdapter<String>(inf.getContext(), android.R.layout.simple_spinner_dropdown_item,admin.getSkladNames());
        sklad.setAdapter(skladAdapter);

        String[] test = admin.getAccounts();

        Spinner spinner=(Spinner) inf.findViewById(R.id.spinnerAccManagement);
        spinner.setAdapter(new ArrayAdapter<String>(inf.getContext(), android.R.layout.simple_spinner_dropdown_item,test));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Toast.makeText(inf.getContext(),"Selected " + test[arg2],Toast.LENGTH_SHORT).show();

            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        return inf;
    }

}