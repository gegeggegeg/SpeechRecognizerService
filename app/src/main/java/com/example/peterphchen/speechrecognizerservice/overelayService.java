package com.example.peterphchen.speechrecognizerservice;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class overelayService extends Service {


    private WindowManager wm;
    private Button position;
    private static final String TAG = "overelayService";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onCreate: Initialize WindowManager parameter");
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Service Activated")
                    .setContentText("The ForegroundService is activated. Do notice.").build();

            startForeground(1, notification);
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                10,500,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.END;
        Log.d(TAG, "onCreate: Initialize Windowmanager");
        wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        Log.d(TAG, "onCreate: Initialize button widget");
        position = new Button(this);
        position.setText("Send postion");
        position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Button clicked");
                Toast.makeText(overelayService.this, "Send Map", Toast.LENGTH_SHORT).show();
            }
        });
        try {
            Log.d(TAG, "onCreate: Add button widget to windowmanager");
            wm.addView(position, params);
        }catch (NullPointerException e){
            Log.e(TAG, "onCreate: Error: "+ e.getMessage());
        }
        return START_STICKY_COMPATIBILITY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        wm.removeView(position);
        Log.d(TAG, "onDestroy: Close this service");
        super.onDestroy();
    }
}
