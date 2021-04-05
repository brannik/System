package com.brannik.system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WAKE_LOCK;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;


public class Welcome extends AppCompatActivity implements SampleCallback {
    GlobalVariables GLOBE = new GlobalVariables(MainActivity.getAppContext());
    private Boolean hasFinished = false;

    private final int REQUEST_PERMISSION = 1;
    private final String[] permissions = {
            Manifest.permission.CAMERA,
            WRITE_EXTERNAL_STORAGE,
            READ_PHONE_STATE,
            Manifest.permission.INSTALL_PACKAGES,
            Manifest.permission.INTERNET,
            FOREGROUND_SERVICE,
            WAKE_LOCK
    };

    private static int[] steps = {
            R.layout.fragment_step_start,
            R.layout.fragment_step_two,
            R.layout.fragment_step_tree,
            R.layout.fragment_step_four,
            R.layout.fragment_step_five,
            R.layout.fragment_step_permissions,
            R.layout.fragment_step_finish
    };
    private static int getView(int index){
        return steps[index];
    }

    private static Fragment[] classes = {
        new fragment_step_start(),
        new fragment_step_two(),
        new fragment_step_tree(),
        new fragment_step_four(),
        new fragment_step_five(),
        new fragment_step_permissions(),
        new fragment_step_finish()
    };

    public static Fragment getInstance(int i){
        return classes[i];
    }

    private final String[] hints = {
            "Добре дошъл",
            "Кратка разходка из меню Начало",
            "Кратка разходка из меню График",
            "Кратка разходка из меню Бележки",
            "Кратка разходка из меню Настройки",
            "Разрешения за това приложение и защо са нужни",
            "Край"
    };

    public Button btnNextStep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        final int[] progress = {0};
        setContentView(R.layout.activity_welcome);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnNextStep = (Button) findViewById(R.id.btnNextStep);
        TextView stepCounter = (TextView) findViewById(R.id.stepCounterText);
        TextView topHint = (TextView) findViewById(R.id.topHintText);

        progressBar.setMax(steps.length-1);

        FragmentManager fm = getSupportFragmentManager();
        RelativeLayout fl = (RelativeLayout) findViewById(R.id.fragment);
        fl.removeAllViews();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, getInstance(progress[0]));

        ft.commit();
        topHint.setText(hints[progress[0]]);
        progress[0] = progress[0] + 1;
        progressBar.setProgress(progress[0]);
        stepCounter.setText(progress[0] + " от " + (steps.length-1));
        btnNextStep.setEnabled(false);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progress[0] < steps.length-1) {
                    // set new fragment
                    RelativeLayout fl = (RelativeLayout) findViewById(R.id.fragment);
                    fl.removeAllViews();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment, getInstance(progress[0]));

                    ft.commit();
                    topHint.setText(hints[progress[0]]);
                        progress[0] = progress[0] + 1;
                        progressBar.setProgress(progress[0]);
                        stepCounter.setText(progress[0] + " от " + (steps.length-1));
                        btnNextStep.setEnabled(false);
                    if(progress[0] == steps.length-1){
                        btnNextStep.setText("Край");
                    }
                }
                if(hasFinished){
                    requestPermission();
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {

        if (keyCode == KeyEvent.KEYCODE_BACK)  //Override Keyback to do nothing in this case.
        {
            return true;
        }
        return super.onKeyDown(keyCode, event);  //-->All others key will work as usual
    }

    @Override
    public void onButtonClick() {
        btnNextStep.setEnabled(true);
    }

    @Override
    public void disableButton() {
        btnNextStep.setEnabled(false);
    }

    @Override
    public void onButtonClickFinish() {
        btnNextStep.setEnabled(true);
        hasFinished = true;
    }

    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            new AlertDialog.Builder(this)
                    .setTitle("Permision needed")
                    .setMessage("asd")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Welcome.this,permissions,REQUEST_PERMISSION);
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else{
            ActivityCompat.requestPermissions(this,permissions,REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(Welcome.this,"Granted",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Welcome.this,"Not granted ranted",Toast.LENGTH_SHORT).show();
            }
        }
    }
}