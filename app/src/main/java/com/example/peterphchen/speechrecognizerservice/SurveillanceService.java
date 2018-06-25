package com.example.peterphchen.speechrecognizerservice;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SurveillanceService extends Service {
    private static final String TAG = "SurveillanceService";

    private PhoneCallReceiver receiver;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: Service StartCommand\n");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        Log.d(TAG, "onStartCommand: register receiver\n");
        receiver = new PhoneCallReceiver();
        registerReceiver(receiver,filter);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onDestroy() {
        if(receiver != null) {
            Log.d(TAG, "onDestroy: unregister receiver\n");
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }
}
