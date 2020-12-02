package com.brannik.system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends AppCompatActivity {
    Button btnRegister;
    EditText fName;
    EditText sName;
    TextView txtDevId;
    EditText usrName;
    public static TextView errors;

    private  static String f_name;
    private static String s_name;
    private static String user_name;
    public static String getFName(){
        return f_name;
    }
    public static String getSName(){
        return s_name;
    }
    public static String getUserName(){
        return user_name;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        btnRegister = (Button) findViewById(R.id.btnRegister);
        fName = (EditText) findViewById(R.id.txtFName);
        sName = (EditText) findViewById(R.id.txtSName);
        btnRegister.setOnClickListener(listener);
        txtDevId = (TextView) findViewById(R.id.txtDevId);
        txtDevId.setText(Globals.getDevId());
        usrName = (EditText) findViewById(R.id.txtEditUsrName);
        errors = (TextView) findViewById(R.id.errors_text);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public static void send_error(String text){
        errors.setText(text);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnRegister:
                    new RegisterRequest().execute();

                    f_name = fName.getText().toString();
                    s_name = sName.getText().toString();
                    user_name = usrName.getText().toString();
                    break;
            }
        }
    };


}