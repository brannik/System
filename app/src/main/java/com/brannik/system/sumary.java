package com.brannik.system;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    public static sumary newInstance(String param1, String param2) {
        sumary fragment = new sumary();
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
    public void send_data(View v){
        String message = el.getText().toString();
        JSONObject json = new JSONObject();
        try {
            json.put("REQUEST_TYPE","MESSAGE");
            json.put("REQUEST_CONTENT",message);
            json.put("SENDER",mParam1);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new BackgroundTask().execute(json.toString());
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

        TextView tv3 = (TextView) inf.findViewById(R.id.txtUsername);
        tv3.setText(mParam1);

        el = (TextView) inf.findViewById(R.id.textView1);
        btn = (Button) inf.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                send_data(v);
                el.setText("");
            }
        });
        return inf;
    }

    @Override
    public void onClick(View v) {

    }
}