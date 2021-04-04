package com.brannik.system;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
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
import static java.lang.Integer.valueOf;

public class DatesOptionsAdaptor extends BaseAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private final Context context;
    private final View view;
    GlobalVariables globals = new GlobalVariables(MainActivity.getAppContext());
    Dialog messageDialog;
    Dialog actionsDialog;
    public DatesOptionsAdaptor(ArrayList<String> list, Context context, View view) {
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
            view = inflater.inflate(R.layout.custom_listview_extras, null);
        }

        TextView tvContact= (TextView)view.findViewById(R.id.txtDateText);

        ArrayList<String> listS = new ArrayList<String>(Arrays.asList(list.get(position).split("##")));
        if(parseInt(listS.get(1)) == 2){ // ako e izvunredni dni
            String message = listS.get(4) + " часа на дата " + listS.get(3);
            tvContact.setText(message);
        }
        else if(parseInt(listS.get(1)) == 1){ // ako e izvunredni chasove
            String message = "1 ден на дата " + listS.get(3);
            tvContact.setText(message);
        }




        CardView options= (CardView)view.findViewById(R.id.btnOptions);

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show options dialog for current record and do request after this
                ArrayList<String> currData = new ArrayList<String>(Arrays.asList(list.get(position).split("##")));
                //String msg = "ID [ " + currData.get(0) + " ] TYPE[ " + currData.get(1) + " ] OWNER[ " + currData.get(2) + " ] DATE[ " + currData.get(3) + " ] VOLUME[ " + currData.get(4) + " ]" ;
                buildActionsDialog(parseInt(currData.get(0)),parseInt(currData.get(1)),parseInt(currData.get(2)),currData.get(3),parseInt(currData.get(4)));
                //showMessage(msg);
                //Toast.makeText(v.getContext(),msg,Toast.LENGTH_SHORT).show();
                //doRequest(type,dateId,sender,not_id,nameS,message,dateStr);
            }
        });

        messageDialog = new Dialog(view.getContext());
        actionsDialog = new Dialog(view.getContext());
        return view;
    }
    private Integer typeOfUsage = 0;
    private int enteredVolume = 0;
    private void buildActionsDialog(Integer id, Integer type,Integer owner,String date,Integer volume){
        actionsDialog.setContentView(R.layout.dialog_date_actions);
        TextView txt = (TextView) actionsDialog.findViewById(R.id.dateText);
        TextView vol = (TextView) actionsDialog.findViewById(R.id.currVolume);
        SwitchCompat btnSwitch = (SwitchCompat) actionsDialog.findViewById(R.id.btnSwitch);
        CardView full = (CardView) actionsDialog.findViewById(R.id.carInvisibleFull);
        Button btnSave = (Button) actionsDialog.findViewById(R.id.btnSaveChosen);
        EditText editTxt = (EditText) actionsDialog.findViewById(R.id.volumeEntered);



        txt.setText(date);
        String typ;
        if(type == 1){
            typ = " ден";
        }else{
            if(volume > 1){
                typ = " часа";
            }else {
                typ = " час";
            }

        }
        String temp = "Налични - " + volume + typ;
        vol.setText(temp);

        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btnSwitch.setText("Да");
                    typeOfUsage = 1;
                    full.setVisibility(View.VISIBLE);
                    btnSave.setEnabled(false);
                }else{
                    typeOfUsage = 2;
                    btnSwitch.setText("Не");
                    btnSave.setEnabled(true);
                    full.setVisibility(View.GONE);
                }
            }
        });

        editTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sT = s.toString();
                if(sT.matches("")){
                    btnSave.setEnabled(false);
                }else{
                    btnSave.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String sT = s.toString();
                if(sT.matches("")){
                    btnSave.setEnabled(false);
                }else{
                    btnSave.setEnabled(true);
                }
            }
        });
        //String temp = id + "\n" + type + "\n" + owner + "\n" + date + "\n" + volume;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 2){
                    // chasove
                    if(typeOfUsage == 1){
                        if(editTxt.isEnabled()){
                            enteredVolume = parseInt(editTxt.getText().toString());
                        }
                        if(enteredVolume > volume){
                            showMessage("Не може да ползваш повече часове от колкото имаш. Налични са " + volume + " часа.");
                        }else if(enteredVolume == 0){
                            showMessage("Не може да ползваш 0 часа въведи корекно число!");
                        }else{
                            showMessage("Ще използваш " + enteredVolume + " часа от " + volume + " за дата " + date);
                        }
                    }else{
                        showMessage("Ще използваш всички " + volume + " часа за дата " + date);
                    }
                }else{
                    // dni
                    showMessage("Ще използваш ДНИ");
                }
            }
        });
        actionsDialog.show();
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

            @Override
            protected void finalize() throws Throwable {
                super.finalize();

            }
        });
        messageDialog.show();

    }

}
