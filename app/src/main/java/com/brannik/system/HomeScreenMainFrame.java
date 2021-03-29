package com.brannik.system;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Integer.parseInt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeScreenMainFrame#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeScreenMainFrame extends Fragment implements View.OnClickListener {


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

    private final ArrayList<String> array = new ArrayList<>();
    GlobalVariables GLOBE = new GlobalVariables(MainActivity.getAppContext());

    public HomeScreenMainFrame() {
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
    public static HomeScreenMainFrame newInstance(String param1, String param2, String param3, String param4) {
        HomeScreenMainFrame fragment = new HomeScreenMainFrame();
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


    TextView docCount;
    FrameLayout warningFrame;
    TextView warningText;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View inf = inflater.inflate(R.layout.fragment_layout_home, container, false);
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


        Calendar calendar = Calendar.getInstance();
        int id = GLOBE.getAccId();
        int skladd = GLOBE.getSklad();
        int month = calendar.get(Calendar.MONTH);
        month+=1;
        checks(id,skladd,month,inf);

        // call function to prepare list
        prepareList(inf);



        return inf;
    }

    private void checks(int acc_id,int sklad,int month,View view){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());
        String url = GlobalVariables.URL + "?request=get_doc_count&month=" + month + "&acc_id=" + acc_id + "&sklad=" + sklad;
        docCount = (TextView) view.findViewById(R.id.txtCount);
        warningFrame = (FrameLayout) view.findViewById(R.id.LineEight);
        warningFrame.setVisibility(View.INVISIBLE);
        warningText = (TextView) view.findViewById(R.id.txtWarning);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                String total = data.getString("COUNT_TOTAL");
                                String unchecked = data.getString("COUNT_UNCHECKED");
                                Calendar calend = Calendar.getInstance();
                                int lastDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
                                int today = calend.get(Calendar.DAY_OF_MONTH);
                                int t = lastDay - today;
                                if(t <= 6){
                                    if(parseInt(unchecked) > 0) {
                                        warningFrame.setVisibility(View.VISIBLE);
                                        String msg = "Внимание имате " + unchecked + " неотразени бележки и " + t + " дни до края на месеца !!!";
                                        warningText.setVisibility(View.VISIBLE);
                                        warningText.setText(msg);
                                    }
                                }
                                docCount.setText(total);
                                //Log.d("DEBUG","COUNTER_>" + response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    private void prepareList(View view){
        // send volley request
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());
        int ID = GLOBE.getAccId();
        int SKLAD = GLOBE.getSklad();
        String url = GlobalVariables.URL + "?request=in_app_notify&acc_id=" + ID + "&sklad_id=" + SKLAD;
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

                            listView.setAdapter(new HomeScreenNotificationsAdaptor(array, view.getContext()) );
                            //listView.setAdapter(arrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Log.d("DEBUG"," -> " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUG", "VOLLEY ERROR -> " + error);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {

    }
}