package com.example.peterphchen.speechrecognizerservice;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
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
    private Switch enableMap;
    private Switch enableRecord;
    private final static int OVERLAY_PERMISSION_CODE = 999;
    private final static int ADMIN_PERMISSION_CODE = 998;
    private DevicePolicyManager mDPM;
    private ComponentName mAdminName;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        addOverlay();
        requestAdmin();

        final SharedPreferences setting = getSharedPreferences("Setting",MODE_PRIVATE);
        enableSwitch = findViewById(R.id.switch1);
        enableSwitch.setChecked(setting.getBoolean("switch",false));
        editor = setting.edit();

        enableMap = findViewById(R.id.mapswtich);
        enableMap.setEnabled(enableSwitch.isChecked());
        enableMap.setChecked(setting.getBoolean("mapEnable",false));
        enableMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean enablem) {
                editor.putBoolean("mapEnable", enablem);
                editor.commit();
            }
        });

        enableRecord = findViewById(R.id.recordswitch);
        enableRecord.setEnabled(enableSwitch.isChecked());
        enableRecord.setChecked(setting.getBoolean("recordEnable",false));
        enableRecord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean enabler) {
                editor.putBoolean("recordEnable",enabler);
                editor.commit();
            }
        });



        enableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Intent intent = new Intent(MainActivity.this,SurveillanceService.class);
                    Log.d(TAG, "onCheckedChanged: Start Service");
                    startService(intent);
                    enableMap.setEnabled(true);
                    enableRecord.setEnabled(true);
                }else {
                    Log.d(TAG, "onCheckedChanged: Shut down Service");
                    stopService(new Intent(MainActivity.this,SurveillanceService.class));
                    Log.d(TAG, "onCheckedChanged: Disable map enable switch");
                    enableMap.setChecked(false);
                    editor.putBoolean("mapEnable",false);
                    enableMap.setEnabled(false);
                    enableRecord.setEnabled(false);
                    enableRecord.setChecked(false);
                    editor.putBoolean("recordEnable",false);
                }
                editor.putBoolean("switch",b);
                editor.commit();
            }
        });
    }

    private void requestAdmin() {
        try {
            Log.d(TAG, "requestAdmin: get System service");
            mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
            mAdminName = new ComponentName(this, AdminReceiver.class);
            Log.d(TAG, "requestAdmin: add ComponentName");
            if(!mDPM.isAdminActive(mAdminName)) {
                Log.d(TAG, "requestAdmin: Ask for Admin");
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to secure your application.");
                startActivityForResult(intent, ADMIN_PERMISSION_CODE);
            }

        }catch (Exception e){
            Log.e(TAG, "requestAdmin: "+e.getMessage());
        }
    }

    private void requestPermission() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission( this, Settings.ACTION_MANAGE_OVERLAY_PERMISSION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Manifest.permission.SEND_SMS,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                    ,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}
                    ,999);
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
        }else if(requestCode == ADMIN_PERMISSION_CODE){
            Log.d(TAG, "onActivityResult: Admin auth is ok");
        }
    }
}
