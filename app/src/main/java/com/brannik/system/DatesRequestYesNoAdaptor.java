package com.brannik.system;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

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
import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class DatesRequestYesNoAdaptor extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private final Context context;
    private final View view;
    GlobalVariables globals = new GlobalVariables(MainActivity.getAppContext());
    Dialog messageDialog;
    public DatesRequestYesNoAdaptor(ArrayList<String> list, Context context, View view) {
        this.list = list;
        this.context = context;
        this.view = view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String message = null;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_listview_requests, null);
        }

        TextView tvContact= (TextView)view.findViewById(R.id.tvContact);

        ArrayList<String> listS = new ArrayList<String>(Arrays.asList(list.get(position).split("##")));
        if(parseInt(listS.get(0)) == 1){ // vtora request
            String msg = "???????????? ???? ?????????????? ???? ?????????? ?????????? ???? ???????? - " + listS.get(2) + " ???? " + listS.get(1);
            tvContact.setText(msg);
            message = "?????????????? ???? ?????????? ??????????";
        }else if(parseInt(listS.get(0)) == 2){ // nedelq request
            String msg = "???????????? ???? ?????????????? ???? ???????????? ???? ???????? - " + listS.get(2) + " ???? " + listS.get(1);
            tvContact.setText(msg);
            message = "?????????????? ???? ????????????";
        }else if(parseInt(listS.get(0)) == 3){ // pochivka request
            String msg = "???????????? ???? ?????????????? ???? ?????????????? ???? ???????? - " + listS.get(2) + " ???? " + listS.get(1);
            tvContact.setText(msg);
            message = "?????????????? ???? ??????????????";
        }




        CardView accept= (CardView)view.findViewById(R.id.btnAccept);
        CardView decline = (CardView) view.findViewById(R.id.btnDecline);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> currData = new ArrayList<String>(Arrays.asList(list.get(position).split("##")));
                int type = 1;
                int dateId = parseInt(currData.get(3));
                int sender = parseInt(currData.get(4));
                int not_id = parseInt(currData.get(5));
                String nameS = currData.get(1);
                String dateStr = currData.get(2);
                int notCount = globals.getNotCount();
                globals.setNotCount(notCount - 1);
                String msg = "?????????????? ???????????? ???? ???????????????????? " + currData.get(1) + " ???? ???????? " + currData.get(2);
                showMessage(msg);
                //Toast.makeText(v.getContext(),msg,Toast.LENGTH_SHORT).show();
                doRequest(type,dateId,sender,not_id,nameS,message,dateStr);
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> currData = new ArrayList<String>(Arrays.asList(list.get(position).split("##")));
                int type = 2;
                int dateId = parseInt(currData.get(3));
                int sender = parseInt(currData.get(4));
                int not_id = parseInt(currData.get(5));
                String nameS = currData.get(1);
                String dateStr = currData.get(2);
                int notCount = globals.getNotCount();
                globals.setNotCount(notCount - 1);
                String msg = "?????????????????? ???????????? ???? ???????????????????? " + currData.get(1) + " ???? ???????? " + currData.get(2);
                showMessage(msg);
                //Toast.makeText(v.getContext(),msg,Toast.LENGTH_SHORT).show();
                doRequest(type,dateId,sender,not_id,nameS,message,dateStr);
            }
        });
        messageDialog = new Dialog(view.getContext());
        return view;
    }

    public void showMessage(String msg) {
        messageDialog.setContentView(R.layout.dialog_message);
        TextView text = (TextView) messageDialog.findViewById(R.id.txtMessage);
        text.setText(msg);
        Button btnClose = (Button) messageDialog.findViewById(R.id.btnOk);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 new ShiftsMainFrame().buildRequestNotifications(view);
                messageDialog.dismiss();
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();

            }
        });
        messageDialog.show();

    }

    private void doRequest(int type,int dateId,int sender,int notifyId,String names,String message,String dateString){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());
        String url= null;
        String name = globals.getNames();
        int accId = globals.getAccId();
        switch(type){
            case 1: // accept
                url = GlobalVariables.URL + "?request=accept_request&date_id=" + dateId + "&sender=" + sender + "&notify_id=" + notifyId + "&my_acc=" + accId + "&names=" + name + "&message=" + message + "&dateStr=" + dateString;
                break;
            case 2: // decline
                url = GlobalVariables.URL + "?request=decline_request&date_id=" + dateId + "&sender=" + sender + "&notify_id=" + notifyId + "&my_acc=" + accId + "&names=" + name + "&message=" + message + "&dateStr=" + dateString;
                break;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG","DEBUG->" + response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                String RESPONSE = data.getString("RESULT");
                                if(parseInt(RESPONSE) == 1){
                                    //String msg = "?????????????? ???????????? !!!";
                                    //showMessage(msg);
                                }else if(parseInt(RESPONSE) == 0){
                                    //String msg = "?????????????????? ???????????? !!!";
                                    //showMessage(msg);
                                }else{
                                    showMessage("SQL ????????????");
                                }
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

}