package com.example.peterphchen.speechrecognizerservice;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Switch enableSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        enableSwitch = findViewById(R.id.switch1);
        final SharedPreferences setting = getPreferences(0);
        enableSwitch.setChecked(setting.getBoolean("switch",false));
        enableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Intent intent = new Intent(MainActivity.this,SurveillanceService.class);
                    Log.d(TAG, "onCheckedChanged: Start Service");
                    startService(intent);
                }else {
                    Log.d(TAG, "onCheckedChanged: Shut down Service");
                    stopService(new Intent(MainActivity.this,SurveillanceService.class));
                }
                SharedPreferences.Editor editor = setting.edit();
                editor.putBoolean("switch",b);
                editor.commit();
            }
        });
    }

    private void requestPermission() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.PROCESS_OUTGOING_CALLS},999);
        }
    }
}
