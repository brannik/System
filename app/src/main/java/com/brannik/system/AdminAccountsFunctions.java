package com.brannik.system;


import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

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
import java.util.List;

import static java.lang.Integer.parseInt;

public class AdminAccountsFunctions {
    Dialog messageDialog;
    private final String[][] ranks={
            {"1","Гост"},
            {"2","Потребител"},
            {"3","Администратор"}
    };
    private final String[][] skladNames= {
            {"0","Няма"},
            {"1","Първи"},
            {"2","Втори"},
            {"3","Мострена"},
            {"4","Четвърти"},
            {"5","Победа"},
            {"6","Клетки"}
    };



    public String[] getRankNames(){
        String[] names = new String[ranks.length];
        for(int i=0;i<ranks.length;i++){
            names[i] = ranks[i][1];
        }
        return names;
    }

    public String[] getSkladNames(){
        String[] skladNamesText = new String[skladNames.length];
        for(int i=0;i<skladNames.length;i++){
            skladNamesText[i] = skladNames[i][1];
        }
        return skladNamesText;
    }

    public String getRankIdByName(String name){
        String id="";
        for(int i=0;i<ranks.length;i++){
            if (ranks[i][1].equals(name)) {
                id = ranks[i][0];
                break;
            }
        }
        return id;
    }

    public String getSkladIdByName(String name){
        String id="";
        for(int i=0;i<skladNames.length;i++){
            if(skladNames[i][1].equals(name)){
                id=skladNames[i][0];
                break;
            }
        }
        return id;
    }

    public void buildAccAdminSection(View view,Spinner spinner,@Nullable Integer sklad){
        messageDialog = new Dialog(view.getContext());
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());
        String url = GlobalVariables.URL + "?request=adminGetAccounts&sklad="+sklad;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            List<AdminAccountsAdaptor> userList = new ArrayList<>();
                            AdminAccountsAdaptor user;
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                String acc_id = data.getString("acc_id");
                                String username = data.getString("username");
                                String name = data.getString("name");
                                String s_name = data.getString("s_name");
                                String rank = data.getString("rank");
                                String sklad = data.getString("sklad");
                                user = new AdminAccountsAdaptor(acc_id,username,name,s_name,rank,sklad);
                                userList.add(user);

                            }

                            ArrayAdapter<AdminAccountsAdaptor> adapter = new ArrayAdapter<AdminAccountsAdaptor>(view.getContext(),
                                    android.R.layout.simple_spinner_item, userList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    AdminAccountsAdaptor user = (AdminAccountsAdaptor) parent.getSelectedItem();
                                    displayUserData(user);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                                public void getSelectedUser(View v) {
                                    AdminAccountsAdaptor user = (AdminAccountsAdaptor) spinner.getSelectedItem();
                                    displayUserData(user);
                                }
                                private void displayUserData(AdminAccountsAdaptor user) {
                                    String name = user.getName();
                                    String rank = user.getRank();
                                    String currSklad = user.getSklad();
                                    String s_name = user.getS_name();
                                    String userName = user.getUsername();
                                    String user_id = user.getId();

                                    EditText userFName = (EditText) view.findViewById(R.id.adminUserSettingsName);
                                    EditText userSName = (EditText) view.findViewById(R.id.adminUserSettingsSName);
                                    EditText userUsername = (EditText) view.findViewById(R.id.adminUserSettingsUsername);
                                    userFName.setText(name);
                                    userSName.setText(s_name);
                                    userUsername.setText(userName);

                                    Spinner rankSpinner = (Spinner) view.findViewById(R.id.spinnerAccRank);
                                    ArrayAdapter<String> rankAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item,getRankNames());
                                    rankSpinner.setAdapter(rankAdapter);
                                    rankSpinner.setSelection(parseInt(rank));

                                    Spinner skladSpinner = (Spinner) view.findViewById(R.id.spinnerAccSklad);
                                    ArrayAdapter<String> skladAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item,getSkladNames());
                                    skladSpinner.setAdapter(skladAdapter);
                                    skladSpinner.setSelection(parseInt(currSklad));

                                    Button btnAdminAccSave = (Button)  view.findViewById(R.id.btnAdminAccSaveAccount);
                                    Button btnAdminAccDelete = (Button) view.findViewById(R.id.btnAdminAccDeleteAccount);

                                    btnAdminAccSave.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String[] data = new String[6];
                                            data[0] = user_id;
                                            data[1] = userUsername.getText().toString();
                                            data[2] = userFName.getText().toString();
                                            data[3] = userSName.getText().toString();
                                            String rank_id = getRankIdByName(rankSpinner.getSelectedItem().toString());
                                            data[4] = rank_id;
                                            String sklad_id = getSkladIdByName(skladSpinner.getSelectedItem().toString());
                                            data[5] = sklad_id;
                                            //Toast.makeText(MainActivity.getAppContext(),data[1] + " " + data[0],Toast.LENGTH_SHORT).show();
                                            sendAction(1,data);
                                        }
                                    });

                                    btnAdminAccDelete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String[] data = new String[1];
                                            data[0] = user_id;
                                            //Toast.makeText(MainActivity.getAppContext(),data[1] + " " + data[0],Toast.LENGTH_SHORT).show();
                                            sendAction(2,data);
                                        }
                                    });

                                }
                                private void sendAction(int action,String[] data){
                                    RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());
                                    String url;
                                    if(action == 1){
                                        url = GlobalVariables.URL + "?request=adminEditAccount&acc_id=" + data[0] + "&username=" + data[1] + "&f_name=" + data[2] + "&s_name=" + data[3] + "&rank=" + data[4] + "&sklad=" + data[5];
                                    }else{
                                        url = GlobalVariables.URL + "?request=adminDeleteAccount&acc_id=" + data[0];
                                    }

                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    showMessage(response);
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

                                // outer volley !!!!!!!!!!!!!!!
                            });


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
