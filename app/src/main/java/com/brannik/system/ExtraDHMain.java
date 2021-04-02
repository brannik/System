package com.brannik.system;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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

import static java.lang.Integer.parseInt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExtraDHMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExtraDHMain extends Fragment {
    GlobalVariables globals = new GlobalVariables(MainActivity.getAppContext());
    Dialog messageDialog;
    Dialog addExtraDay;
    Dialog addExtraHour;
    Dialog dateOptions;
    Dialog confirmDelete;
    int id = globals.getAccId();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExtraDHMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment extra.
     */
    // TODO: Rename and change types and number of parameters
    public static ExtraDHMain newInstance(String param1, String param2) {
        ExtraDHMain fragment = new ExtraDHMain();
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
        View inf = inflater.inflate(R.layout.fragment_extra, container, false);
        // Inflate the layout for this fragment
        messageDialog = new Dialog(this.getContext());
        addExtraHour = new Dialog(this.getContext());
        addExtraDay = new Dialog(this.getContext());
        dateOptions = new Dialog(this.getContext());
        confirmDelete = new Dialog(this.getContext());

        CardView btnListAll = (CardView) inf.findViewById(R.id.btnListAllDates);
        CardView btnAddExtraHours = (CardView) inf.findViewById(R.id.btnAddExtraHours);
        CardView btnAddExtraDays = (CardView) inf.findViewById(R.id.btnAddExtraDays);
        CardView btnAddExtraResetAll = (CardView) inf.findViewById(R.id.btnAddExtraResetAll);

        TextView hours = (TextView) inf.findViewById(R.id.counterHours);
        TextView days = (TextView) inf.findViewById(R.id.countDays);
        hours.setText(globals.getExtraHours());
        days.setText(globals.getExtraDays());

        btnAddExtraDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExtraDayDialog();
            }
        });

        btnAddExtraHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExtraHourDialog();
            }
        });

        btnAddExtraResetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("Зануляване на всички налични");
            }
        });

        btnListAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDatesList(inf);
            }
        });
        return inf;
    }
    private void addExtraDayDialog(){
        addExtraDay.setContentView(R.layout.dialog_add_extra_day);
        DatePicker datePicker = addExtraDay.findViewById(R.id.datePickerDay);
        TextView currentDate = addExtraDay.findViewById(R.id.extraDaysCurrentDate);
        Button btnSaveDay = addExtraDay.findViewById(R.id.daysSaveBtn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    currentDate.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
                }
            });
        }

        btnSaveDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = currentDate.getText().toString();
                if(tmp.matches("")){
                    showMessage("Не е избрана дата!!!");
                }else{
                    sendData(id,1,1,tmp);
                    addExtraDay.dismiss();
                }
            }
        });

        addExtraDay.show();

    }

    private void addExtraHourDialog(){
        addExtraHour.setContentView(R.layout.dialog_add_extra_hours);
        DatePicker datePicker = addExtraHour.findViewById(R.id.datePicker);
        Button btnSave = addExtraHour.findViewById(R.id.hoursSaveBtn);
        EditText extraCount = addExtraHour.findViewById(R.id.hoursCountText);
        TextView currentDate = addExtraHour.findViewById(R.id.extraHoursCurrentDate);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    currentDate.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
                }
            });
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String count = extraCount.getText().toString();
                String date = currentDate.getText().toString();
                if(count.matches("") || date.matches("")){
                    showMessage("Не е зададено количество на часовете или не е избрана дата " + currentDate.getText());
                }else{
                    if(parseInt(count) == 0){
                        showMessage("0 часа е невалидно!!!");
                    }else{
                        if(parseInt(count) < 11){
                            sendData(id,2,parseInt(count),currentDate.getText().toString());
                            addExtraHour.dismiss();
                            //showMessage("Избрахте дата: " + currentDate.getText() + " и количество " + extraCount.getText());
                        }else{
                            showMessage("Не може да имаш повече от 10 извънредни часа за един ден! Написал си " + extraCount.getText() );
                        }
                    }


                }
            }
        });
        addExtraHour.show();
    }

    private void sendData(int acc_id, int type, Integer count,String date){
        // type 2 -> extra hours
        // do request
        // message with response
        // save data

        //showMessage(acc_id + " | " + type + " | " + count + " | " + date);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());
        String url = GlobalVariables.URL + "?request=save_new_extra_date&acc_id=" + acc_id + "&type=" + type + "&count=" + count + "&date=" + date;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String resp = null;
                            String err = null;
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                resp = data.getString("REQ_STATE");
                                err = data.getString("RESPONSE");

                                if(parseInt(resp) == 1){
                                    showMessage("Успешно регистрирахте дата " + date);
                                }else{
                                    showMessage("Грешка! -> " + err);
                                }
                            }
                            Log.d("DEBUG",response);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private final ArrayList<String> array = new ArrayList<>();

    public void buildDatesList(View view){
        ListView listV = (ListView) view.findViewById(R.id.listDates);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());

        int sklad = globals.getSklad();
        String url = GlobalVariables.URL + "?request=get_extra_dates&acc_id=" + id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        array.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            //Log.d("DEBUG","DEBUG->" + response);
                            globals.setNotCount(jsonArray.length());
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                String date_id = data.getString("DATE_ID");
                                String date_type = data.getString("DATE_TYPE");
                                String date_owner = data.getString("DATE_OWNER");
                                String date = data.getString("DATE");
                                String date_volume = data.getString("DATE_VOLUME");

                                String text = date_id + "##" + date_type + "##" + date_owner + "##" + date + "##" + date_volume;
                                // format with separators ##
                                array.add(text);
                            }
                            listV.setAdapter(new DatesOptionsAdaptor(array, view.getContext(),view ));
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);


    }

    public void showMessage(String msg) {
        messageDialog.setContentView(R.layout.dialog_message);
        TextView text = (TextView) messageDialog.findViewById(R.id.txtMessage);
        text.setText(msg);
        Button btnClose = (Button) messageDialog.findViewById(R.id.btnOk);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageDialog.dismiss();
            }
        });
        messageDialog.show();

    }
}