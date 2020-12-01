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
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnRegister:
                    String name = fName.getText().toString();
                    name += " ";
                    name += sName.getText().toString();
                    Log.d("DEBUG","BUTTON REGISTER PRESSED");
                    registerUser(name);
                    break;
            }
        }
    };

    private void registerUser(String name){
        Log.d("DEBUG","ID -> " + Globals.getDevId() + " NAME -> " + name);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}