package com.brannik.system;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;

import kotlinx.coroutines.channels.Send;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link sundays#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sundays extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<String> array = new ArrayList<>();
    private String[][] documents;
    private Integer documentsCount = 0;
    private Integer i =0;
    private Boolean allowToSend = true;

    Globals GLOBE = new Globals(MainActivity.getAppContext());
    Dialog myDialog;
    public sundays() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sundays.
     */
    // TODO: Rename and change types and number of parameters
    public static sundays newInstance(String param1, String param2) {
        sundays fragment = new sundays();
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
        View inf = inflater.inflate(R.layout.fragment_sundays, container, false);
        Button btnAdd = (Button) inf.findViewById(R.id.btnNewDocument);
        Button btnDel = (Button) inf.findViewById(R.id.btnDelete);
        Button btnFind = (Button) inf.findViewById(R.id.btnFindDocument);
        Button btnList = (Button) inf.findViewById(R.id.btnListDocuments);
        Button btnMode = (Button) inf.findViewById(R.id.btnDocumentEnter);

        EditText docNumber = (EditText) inf.findViewById(R.id.editDocumentNumber);

        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnFind.setOnClickListener(this);
        btnList.setOnClickListener(this);
        btnMode.setOnClickListener(this);
        myDialog = new Dialog(this.getContext());
        return inf;
    }
    public void sendItems(){
        ListView listView = getView().findViewById(R.id.listDocuments);
        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.getAppContext(),
                android.R.layout.simple_list_item_1,
                array);
        listView.setAdapter(arrayAdapter);
    }
    public void ShowPopup(){
        // get first element from documents array and display it
        i = 0;
        myDialog.setContentView(R.layout.popup);
        TextView text = (TextView) myDialog.findViewById(R.id.txtShowDocument);
        TextView txtCounter = (TextView) myDialog.findViewById(R.id.txtCounter);
        String temp = valueOf(i+1) + " от " +valueOf(documentsCount-1);
        txtCounter.setText(temp);
        text.setText("No -> " + documents[i][0]);
        Button btnSkip = myDialog.findViewById(R.id.btnSkip);
        Button btnClose = myDialog.findViewById(R.id.btnClose);
        Button btnEnter = myDialog.findViewById(R.id.btnEnter);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = i + 1;
                if(i<documentsCount-1){
                    text.setText("No -> " + documents[i][0]);
                    String temp = valueOf(i+1) + " от " +valueOf(documentsCount-1);
                    txtCounter.setText(temp);
                }else{
                    text.setText("Край !!!");
                }

            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i<documentsCount) {
                    SendRequest("CHECK_DOCUMENT", parseInt(documents[i][1]));
                    i = i + 1;
                    if (i < documentsCount - 1) {
                        text.setText("No -> " + documents[i][0]);
                        String temp = valueOf(i+1) + " от " +valueOf(documentsCount-1);
                        txtCounter.setText(temp);
                    } else {
                        text.setText("Край !!!");
                    }
                }

            }
        });
        myDialog.show();
    }
    @Override
    public void onClick(View v) {
        EditText textBox = getView().findViewById(R.id.editDocumentNumber);
        String value= textBox.getText().toString();
        int finalValue;
            switch (v.getId()) {
                case R.id.btnNewDocument:

                    if(value.matches("")){
                        Toast.makeText(MainActivity.getAppContext(), "Текстовото поле не може да е празно !!!", Toast.LENGTH_LONG).show();
                    }else {
                        finalValue= parseInt(value);
                        SendRequest("NEW_DOC", finalValue);
                        textBox.setText("");
                    }
                    //Log.d("DEBUG","ADD NEW DOCUMENT");
                    break;
                case R.id.btnDelete:
                    if(value.matches("")){
                        Toast.makeText(MainActivity.getAppContext(), "Текстовото поле не може да е празно !!!", Toast.LENGTH_LONG).show();
                    }else {
                        finalValue = parseInt(value);
                        SendRequest("DELETE_DOC", finalValue);
                        textBox.setText("");
                    }
                    //Log.d("DEBUG","DELETE DOCUMENT");
                    break;
                case R.id.btnFindDocument:
                    if(value.matches("")){
                        Toast.makeText(MainActivity.getAppContext(), "Текстовото поле не може да е празно !!!", Toast.LENGTH_LONG).show();
                    }else {
                        finalValue = parseInt(value);
                        SendRequest("FIND_DOC", finalValue);
                        textBox.setText("");
                    }
                    break;
                case R.id.btnListDocuments:
                    SendRequest("LIST_ALL",0);
                    textBox.setText("");
                    break;
                case R.id.btnDocumentEnter:
                    SendRequest("GET_ALL_ENTER_MODE",0);
                    textBox.setText("");
                    break;
            }

    }
    public int requestType = 0;
    private void SendRequest(String type,int data){
        // send volley request
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());

        int ID = GLOBE.getAccId();
        int SKLAD = GLOBE.getSklad();
        String url;
        switch(type){
            case "NEW_DOC":
                url = Globals.URL + "?request=add_new_document&data=" + data + "&acc_id=" + ID + "&sklad=" + SKLAD;
                requestType = 1;
                break;
            case "DELETE_DOC":
                url = Globals.URL + "?request=delete_document&data=" + data + "&acc_id=" + ID + "&sklad=" + SKLAD;
                requestType = 2;
                break;
            case "FIND_DOC":
                url = Globals.URL + "?request=find_document&data=" + data + "&acc_id=" + ID + "&sklad=" + SKLAD;
                requestType = 3;
                break;
            case "LIST_ALL":
                url = Globals.URL + "?request=list_document&data=" + data + "&acc_id=" + ID + "&sklad=" + SKLAD;
                requestType = 4;
                break;
            case "GET_ALL_ENTER_MODE":
                url = Globals.URL + "?request=entering_mode&acc_id=" + ID + "&sklad=" + SKLAD;
                requestType = 5;
                break;
            case "CHECK_DOCUMENT":
                url = Globals.URL + "?request=entering_mode_document&doc_id=" + data;
                requestType = 6;
                break;
            default:
                url = Globals.URL + "?request=test";
        }

        //String url ="http://app-api.servehttp.com/api.php?request=test";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.d("DEBUG"," -> " + response);
                        if(requestType == 1 || requestType == 2) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String notText = data.getString("RESPONSE");
                                    Toast.makeText(MainActivity.getAppContext(), notText, Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else if(requestType == 3){
                            array.clear();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0;i<jsonArray.length();i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String docNumber = data.getString("DOC_NUMBER");
                                    String docOwner = data.getString("OWNER");
                                    String docSklad = data.getString("SKLAD");
                                    String docStatus = data.getString("STATUS");
                                    String docDate = data.getString("DATE");
                                    String finalText = ">>  " + docNumber + " <На> " + docOwner + " <Склад> " + docSklad + " <Дата> " + docDate + " <Статус> " + docStatus;
                                    array.add(finalText);
                                }
                                sendItems();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else if(requestType == 4){
                            array.clear();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i=0;i<jsonArray.length();i++) {
                                    if(i==0){
                                        JSONObject data = jsonArray.getJSONObject(i);
                                        String nonEnteredDocs = data.getString("NON_ENTERED");
                                        String enteredDocs = data.getString("ENTERED");
                                        String totalDocs = data.getString("TOTAL");
                                        String finalText = "НЕ Отразени " + nonEnteredDocs + "         Отразени " + enteredDocs + "       Всички " + totalDocs;
                                        array.add(finalText);
                                    }else{
                                        JSONObject data = jsonArray.getJSONObject(i);
                                        String docNumber = data.getString("DOC_NUMBER");
                                        String docDate = data.getString("DOC_DATE");
                                        String docStatus = data.getString("DOC_STATUS");
                                        String finalText = ">>   " + docNumber + "       |  " + docDate + "    |     " + docStatus;
                                        array.add(finalText);
                                    }

                                }
                                sendItems();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else if(requestType == 5){
                            // get data and process request to popup window
                            // get results and push to arrDocuments

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                documents = new String[jsonArray.length()][2];
                                documentsCount = jsonArray.length();
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String doc_number = data.getString("DOC_NUM");
                                    String doc_id = data.getString("DOC_ID");
                                    //Toast.makeText(MainActivity.getAppContext(), notText, Toast.LENGTH_LONG).show();
                                    documents[i][0] = doc_number;
                                    documents[i][1] = doc_id;
                                }
                                Log.d("DEBUG",response);

                                ShowPopup();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }else if(requestType == 6){
                            // action when document is checked
                            if(i<documentsCount) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject data = jsonArray.getJSONObject(0);
                                    String result = data.getString("RESPONSE");
                                    if (result.matches("DONE")) {
                                        Toast.makeText(MainActivity.getAppContext(), "Бележката е отразена", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.getAppContext(), "Бележката НЕ е отразена", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.getAppContext(), response, Toast.LENGTH_LONG).show();
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

}