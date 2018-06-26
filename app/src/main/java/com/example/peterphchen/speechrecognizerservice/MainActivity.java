package com.example.peterphchen.speechrecognizerservice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Switch enableSwitch;
    private final static int OVERLAY_PERMISSION_CODE = 999;
    private PhoneCallReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        addOverlay();
        enableSwitch = findViewById(R.id.switch1);
        final SharedPreferences setting = getPreferences(0);
        enableSwitch.setChecked(setting.getBoolean("switch",false));
        enableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Intent intent = new Intent(MainActivity.this,SurveillanceService.class);
                    Log.d(TAG, "onCheckedChanged: Start Service");
                    startForegroundService(intent);
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
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission( this, Settings.ACTION_MANAGE_OVERLAY_PERMISSION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Manifest.permission.SEND_SMS,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},999);
        }
    }

    public void addOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "ACTION_MANAGE_OVERLAY_PERMISSION Permission Granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "ACTION_MANAGE_OVERLAY_PERMISSION Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
