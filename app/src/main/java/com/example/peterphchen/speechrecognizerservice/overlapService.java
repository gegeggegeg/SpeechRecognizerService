package com.example.peterphchen.speechrecognizerservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class overlapService extends Service {

    private WindowManager wm;
    private ImageView smsBtn;
    private static final String TAG = "overlapService";
    private String phoneNumber;
    private FusedLocationProviderClient mLocationProvider;
    private WindowManager.LayoutParams params;
    Location myLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        // Send notification to user so service can continue in background
        wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "temp channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Service Activated")
                    .setContentText("The ForegroundService is activated. Do notice.").build();
            startForeground(1, notification);
        }

        //Set parameter for SMS button
        Log.d(TAG, "onCreate: Initialize WindowManager parameter");
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                20, -300,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.END;

        //Set SMS imageview widget and Click listener
        Log.d(TAG, "onCreate: Initialize SMS button widget");
        smsBtn = new ImageView(overlapService.this);
        smsBtn.setImageResource(R.mipmap.ic_mail);
        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Get current location and send SMS message");
                getDeviceLocation();
            }
        });

        try {
            Log.d(TAG, "onCreate: add MapBtn widget to Windowmanager");
            wm.addView(smsBtn, params);
        }catch (Exception e){
            Log.e(TAG, "onCreate: Error: " + e.getMessage() );
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        phoneNumber = intent.getExtras().getString("phone_number");
        return START_STICKY;
    }

    private void senSMS(double lng, double lat) {
        //Send SMS message with google map url
        SmsManager mgr = SmsManager.getDefault();
        String message = "https://www.google.com/maps/search/?api=1&query="+lat+","+lng;
        Log.d(TAG, "senSMS: Send SMS: "+ message);
        mgr.sendTextMessage(phoneNumber,null,message,null,null);
        Toast.makeText(this, "SMS have sent ", Toast.LENGTH_SHORT).show();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            wm.removeViewImmediate(smsBtn);
        }catch (Exception e){
            Log.e(TAG, "onDestroy: "+e.getMessage() );
        }
        Log.d(TAG, "onDestroy: Close this service");
    }

    private void getDeviceLocation(){
        mLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        try{
            final Task location  = mLocationProvider.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful() && task.getResult() != null) {
                        Log.d(TAG, "onComplete: found location");
                        Location currentlocation = (Location)task.getResult();
                        myLocation = currentlocation;
                        Log.d(TAG, "onComplete: currentlocation: "+myLocation.getLongitude()+", "+myLocation.getLatitude());
                        //Send SMS message
                        senSMS(myLocation.getLongitude(), myLocation.getLatitude());
                    }else {
                        Log.d(TAG, "onComplete: Can't found current location");
                        Toast.makeText(overlapService.this, "Can not find current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: "+e.getMessage() );
        }
    }
}