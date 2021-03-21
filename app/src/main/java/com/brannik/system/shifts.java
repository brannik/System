package com.brannik.system;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.google.android.gms.common.internal.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.valueOf;
import static java.lang.Integer.parseInt;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link shifts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class shifts extends Fragment implements OnNavigationButtonClickedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CustomCalendar customCalendar;
    Dialog dateActions;
    HashMap<Integer,Object> dateHashMap = new HashMap<>();
    Dialog messageDialog;

    Globals globals = new Globals(MainActivity.getAppContext());

    public shifts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment shifts.
     */
    // TODO: Rename and change types and number of parameters
    public static shifts newInstance(String param1, String param2) {
        shifts fragment = new shifts();
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
        // Inflate the layout for this fragment
        View inf =  inflater.inflate(R.layout.fragment_shifts, container, false);
        customCalendar = inf.findViewById(R.id.custom_calendar);

        HashMap<Object, Property> descHashMap = new HashMap<>();
        dateActions = new Dialog(this.getContext());
        messageDialog = new Dialog(this.getContext());

        Property defaultProperty = new Property();
        defaultProperty.layoutResource = R.layout.calendar_deff;
        defaultProperty.dateTextViewResource = R.id.text_view;
        descHashMap.put("default",defaultProperty);

        Property dnes = new Property();
        dnes.layoutResource = R.layout.calendar_dnes;
        dnes.dateTextViewResource = R.id.text_view;
        descHashMap.put("dnes",dnes);

        Property pochivka = new Property();
        pochivka.layoutResource = R.layout.calendar_pochivka;
        pochivka.dateTextViewResource = R.id.text_view;
        descHashMap.put("pochivka",pochivka);

        Property vtora = new Property();
        vtora.layoutResource = R.layout.calendar_vtora_smqna;
        vtora.dateTextViewResource = R.id.text_view;
        descHashMap.put("vtora",vtora);

        Property nedelq = new Property();
        nedelq.layoutResource = R.layout.calendar_nedelq;
        nedelq.dateTextViewResource = R.id.text_view;
        descHashMap.put("nedelq",nedelq);

        Property dnesPochivka = new Property();
        dnesPochivka.layoutResource = R.layout.calendar_dnes_pochivka;
        dnesPochivka.dateTextViewResource = R.id.text_view;
        descHashMap.put("dnesPochivka",dnesPochivka);

        Property dnesVtora = new Property();
        dnesVtora.layoutResource = R.layout.calendar_vtora_dnes;
        dnesVtora.dateTextViewResource = R.id.text_view;
        descHashMap.put("dnesVtora",dnesVtora);

        Property dnesNedelq = new Property();
        dnesNedelq.layoutResource = R.layout.calendar_dnes_nedelq;
        dnesNedelq.dateTextViewResource = R.id.text_view;
        descHashMap.put("dnesNedelq",dnesNedelq);

        Property zaeto = new Property();
        zaeto.layoutResource = R.layout.calendar_zaeto;
        zaeto.dateTextViewResource = R.id.text_view;
        descHashMap.put("zaeto",zaeto);

        Property dnesZaeto = new Property();
        dnesZaeto.layoutResource = R.layout.calendar_dnes_zaeto;
        dnesZaeto.dateTextViewResource = R.id.text_view;
        descHashMap.put("dnesZaeto",dnesZaeto);

        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);

        customCalendar.setMapDescToProp(descHashMap);

        Calendar calendar = Calendar.getInstance();
        // call voley and process requests

        buildCalendar();
        buildRequestNotifications(inf);

        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {

                dateActions(selectedDate.get(Calendar.YEAR),selectedDate.get(Calendar.MONTH) + 1,selectedDate.get(Calendar.DAY_OF_MONTH));
            }
        });

        return inf;
    }



    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        Map<Integer,Object>[] arr = new Map[2];
        switch(newMonth.get(Calendar.MONTH)){
            case Calendar.APRIL:
                arr[0] = new HashMap<>();
                // get info for this month and populate it
                arr[0].put(3,"unavailable");
                arr[0].put(14,"vtora");
                arr[1] = null;
                break;
            case Calendar.MARCH:
                // get info for this month and populate it
                arr[0] = new HashMap<>();
                arr[0].put(13,"pochivka");
                break;
        }
        return arr;
    }

    private ArrayList<ArrayList<String>> arrSecondShift = new ArrayList<>();
    private ArrayList<ArrayList<String>> arrSunday = new ArrayList<>();
    private ArrayList<ArrayList<String>> arrRest = new ArrayList<>();

    Boolean freeSecondShift = false;
    Boolean secondShiftMine = false;
    Boolean freeSunday = false;
    Boolean sundayMine = false;
    Boolean freeRest = false;
    Boolean restMine = false;
    int reqType = 0;
    // 1 - zaqvka za svobodna nedelq
    // 2 - zaqvka za zaeta nedelq
    // 3 - zaqvka za svoboden pochiven den
    // 4 - zaqvka za zaet pochiven den
    // 5 - zaqvka za svobodna 2-ra smqna
    // 6 - zaqvka za zaeta vtora smqna
    private void checkDate(int year,int month,int day,int acc,int sklad){
        // voley da nameri datata

    }

    private void dateActions(int year,int month,int day){
        dateActions.setContentView(R.layout.date_actions);
        TextView textHeader = (TextView) dateActions.findViewById(R.id.current_date_header);
        TextView secondShLabel = (TextView) dateActions.findViewById(R.id.dateTextSecondSh);
        Button btnSecondShift = (Button) dateActions.findViewById(R.id.btnSecondShift);
        TextView restLabel = (TextView) dateActions.findViewById(R.id.dateTextRest);
        Button btnRest = (Button) dateActions.findViewById(R.id.btnRest);
        TextView sundayLabel = (TextView) dateActions.findViewById(R.id.dateTextSunday);
        Button btnSunday = (Button) dateActions.findViewById(R.id.btnSunday);

        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        // proveri dali e nedelq ili ne i promeni prozoreca !!!!!
        // MONTH -1 !!!!
        // proveri tazi data dali e svobodna ili ne
        int sklad = globals.getSklad();
        int acc_id = globals.getAccId();
        checkDate(year,month,day,acc_id,sklad);

        String text = year + "/" + month + "/" + day;
        textHeader.setText(text);
        Calendar cal = Calendar.getInstance();
        int tmp = month-1;
        cal.set(year,tmp,day);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek == Calendar.SUNDAY){
            // nedelq e
            secondShLabel.setText("На тази дата е неделя и няма 2-ра смяна.");
            btnSecondShift.setEnabled(false);
            if(freeSunday){ // ako nedelqta e svobodna
                sundayLabel.setText("Неделята е свободна.");
                btnSunday.setEnabled(true);
                reqType = 1;
            }else{
                if(sundayMine){
                    sundayLabel.setText("На тази дата ти си наработа.");
                    btnSunday.setEnabled(false);
                }else {
                    sundayLabel.setText("Неделята е заето от <@@@>");
                    btnSunday.setText("Искам смяна");
                    btnSunday.setEnabled(true);
                    reqType = 2;
                }
            }
        }else if(dayOfWeek == Calendar.SATURDAY){
            secondShLabel.setText("На тази дата е събота и няма 2-ра смяна.");
            btnSecondShift.setEnabled(false);
            sundayLabel.setText("В събота не е неделя :)");
            btnSunday.setEnabled(false);
            if(freeRest){ // ako nikoi ne pochiva
                restLabel.setText("Днес никой не почива.");
                btnRest.setEnabled(true);
                reqType = 3;
            }else{
                if(restMine){
                    restLabel.setText("На тази дата ти пичиваш.");
                    btnRest.setEnabled(false);
                }else {
                    restLabel.setText("Днес почива <@@@>");
                    btnRest.setText("Изкам смяна");
                    btnRest.setEnabled(true);
                    reqType = 4;
                }
            }
        }else{
            sundayLabel.setText("Не може да си неделя когато не е неделя :)");
            btnSunday.setEnabled(false);
            if(freeRest){ // ako nikoi ne pochiva
                restLabel.setText("Днес никой не почива.");
                btnRest.setEnabled(true);
                reqType = 3;
            }else{
                if(restMine){
                    restLabel.setText("На тази дата ти пичиваш.");
                    btnRest.setEnabled(false);
                }else {
                    restLabel.setText("Днес почива <@@@>");
                    btnRest.setText("Изкам смяна");
                    btnRest.setEnabled(true);
                    reqType = 4;
                }
            }

            if(freeSecondShift){
                secondShLabel.setText("Днес няма втора смяна.");
                btnSecondShift.setEnabled(true);
                reqType = 5;
            }else{
                if(secondShiftMine){
                    secondShLabel.setText("Днес ти си втора смяна.");
                    btnSecondShift.setEnabled(false);
                }else {
                    secondShLabel.setText("Днес втора смяна е <@@@>");
                    btnSecondShift.setText("Искам смяна");
                    btnSecondShift.setEnabled(true);
                    reqType = 6;
                }
            }
        }

        btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage(sendDateRequest(reqType,acc_id,sklad,year,month,day,null));
                dateActions.dismiss();
            }
        });

        btnSecondShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage(sendDateRequest(reqType,acc_id,sklad,year,month,day,null));
                dateActions.dismiss();
            }
        });

        btnSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage(sendDateRequest(reqType,acc_id,sklad,year,month,day,null));
                dateActions.dismiss();
            }
        });
        dateActions.show();
    }

    private String sendDateRequest(int type,int acc_id,int sklad,int year,int month,int day, @Nullable String data){
        String mess = "test message";
        // izprashane na zaqvkata za data

        return mess;
    }

    public void showMessage(String msg) {
        messageDialog.setContentView(R.layout.message_popup);
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

    private ArrayList<String> array = new ArrayList<>();

    private void buildRequestNotifications(View view){
        ListView listV = (ListView) view.findViewById(R.id.listRequests);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());
        int id = globals.getAccId();
        int sklad = globals.getSklad();
        String url = Globals.URL + "?request=get_requests&acc_id=" + id + "&sklad=" + sklad;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        array.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            Log.d("DEBUG","DEBUG->" + response);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                String user = data.getString("NOT_SENDER");
                                int type = data.getInt("NOT_TYPE");
                                String date = data.getString("NOT_DATE");
                                int dateId = data.getInt("NOT_DATE_ID");
                                int senderId = data.getInt("NOT_SENDER_ID");
                                int notId = data.getInt("NOT_ID");

                                String text = type + "##" + user + "##" + date + "##" + dateId + "##" + senderId + "##" + notId;
                                // format with separators ##
                                array.add(text);
                            }
                            listV.setAdapter(new MyCustomAdapter(array, view.getContext()) );
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

        // get your requests and add them to listview

    }

    public void doActions(){
        
    }

    public void builldMonth(int year,int month){
        // for previous and next buttons
    }

    public void buildCalendar(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int sklad = globals.getSklad();
        int accId = globals.getAccId();
        final Boolean[] check = {false};
        final int[] dummyDay = {0};

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        String date = dateFormat.format(calendar.getTime());

        String url = Globals.URL + "?request=get_calendar&year=" + year + "&month=" + month + "&sklad=" + sklad;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int daysCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                String day = data.getString("NUMBER");
                                String type = data.getString("TYPE");
                                String owner = data.getString("USER");


                                switch(parseInt(type)){
                                    case FLAGS.DATE_TYPE_S_SHIFT:
                                        if(parseInt(date) == parseInt(day)){
                                            if(parseInt(owner) == accId){
                                                dateHashMap.put(parseInt(day),"dnesVtora");
                                                check[0] = true;
                                            }else{
                                                dateHashMap.put(parseInt(day),"dnesZaeto");
                                            }
                                        }else{
                                            if(parseInt(owner) == accId){
                                                dateHashMap.put(parseInt(day),"vtora");
                                            }else{
                                                dateHashMap.put(parseInt(day),"zaeto");
                                            }
                                        }

                                        break;
                                    case FLAGS.DATE_TYPE_SUNDAY:
                                        if(parseInt(date) == parseInt(day)){
                                            check[0] = true;
                                            if(parseInt(owner) == accId){
                                                dateHashMap.put(parseInt(day),"dnesNedelq");
                                            }else{
                                                dateHashMap.put(parseInt(day),"dnesZaeto");
                                            }
                                        }else{
                                            if(parseInt(owner) == accId){
                                                dateHashMap.put(parseInt(day),"nedelq");
                                            }else{
                                                dateHashMap.put(parseInt(day),"zaeto");
                                            }
                                        }
                                        break;
                                    case FLAGS.DATE_TYPE_REST:
                                        if(parseInt(date) == parseInt(day)){
                                            check[0] = true;
                                            if(parseInt(owner) == accId){
                                                dateHashMap.put(parseInt(day),"dnesPochivka");
                                            }else{
                                                dateHashMap.put(parseInt(day),"dnesZaeto");
                                            }
                                        }else{
                                            if(parseInt(owner) == accId){
                                                dateHashMap.put(parseInt(day),"pochivka");
                                            }else{
                                                dateHashMap.put(parseInt(day),"zaeto");
                                            }
                                        }
                                        break;
                                }
                                //Log.d("DEBUG"," [DATA] " + day + " | " + type + " | " + owner);
                            }
                            if(check[0] == false){
                                dateHashMap.put(parseInt(date),"dnes");
                            }
                            customCalendar.setDate(calendar,dateHashMap);

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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

}
