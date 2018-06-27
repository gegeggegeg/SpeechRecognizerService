package com.example.peterphchen.speechrecognizerservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SurveillanceService extends Service {
    private static final String TAG = "SurveillanceService";
    private PhoneCallReceiver receiver;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: Service StartCommand\n");
        // Send notification to user so service can continue in background
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_02";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "temp channel2",
                    NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Service Activated")
                    .setContentText("The ForegroundService is activated. Do notice.").build();
            startForeground(1, notification);
        }

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
        super.onDestroy();
        Log.d(TAG, "onDestroy: unregister receiver\n");
        unregisterReceiver(receiver);
    }
}
