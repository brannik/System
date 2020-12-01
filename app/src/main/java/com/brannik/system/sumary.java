package com.brannik.system;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        TextView tv = (TextView) inf.findViewById(R.id.txtHours);
        tv.setText("10");

        TextView tv2 = (TextView) inf.findViewById(R.id.txtDays);
        tv2.setText("3");

        TextView tv3 = (TextView) inf.findViewById(R.id.txtDev);
        tv3.setText(mParam1);

        TextView tv4 = (TextView) inf.findViewById(R.id.txtUsername);
        tv4.setText(GLOBE.getUsername());

        TextView tv5 = (TextView) inf.findViewById(R.id.txtNames);
        tv5.setText(GLOBE.getNames());

        TextView tv6 = (TextView) inf.findViewById(R.id.txtRank);
        String txt = String.valueOf(GLOBE.userRank());
        tv6.setText(txt);
        return inf;
    }

    @Override
    public void onClick(View v) {

    }
}