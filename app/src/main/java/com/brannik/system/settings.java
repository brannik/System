package com.brannik.system;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Dialog timePicker;

    public settings() {
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
    public static settings newInstance(String param1, String param2) {
        settings fragment = new settings();
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
        View inf = inflater.inflate(R.layout.fragment_settings, container, false);
        // Inflate the layout for this fragment
        timePicker = new Dialog(this.getContext());
        TextView textFirst = (TextView) inf.findViewById(R.id.txtEditFirstTime);
        TextView textSecond = (TextView) inf.findViewById(R.id.txtEditSecondTime);

        textFirst.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    pickTime(inf,1);
                }
            }
        });

        textSecond.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    pickTime(inf,2);
                }
            }
        });
        return inf;
    }

    public void pickTime(View view,int type) {

        timePicker.setContentView(R.layout.time_picker);
        TimePicker timPick = (TimePicker) timePicker.findViewById(R.id.TimeUI);
        Button OK = (Button) timePicker.findViewById(R.id.ButtonOKTime);
        Button CANCEL = (Button) timePicker.findViewById(R.id.ButtonCancelTime);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 1){
                    int time;
                    int min;
                    time = timPick.getHour();
                    min = timPick.getMinute();
                    TextView text = (TextView) view.findViewById(R.id.txtEditFirstTime);
                    String clock = time + ":" + min;
                    text.setText(clock);
                    text.clearFocus();
                }else{
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
}