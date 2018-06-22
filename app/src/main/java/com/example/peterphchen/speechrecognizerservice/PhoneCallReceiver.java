package com.example.peterphchen.speechrecognizerservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;


public class PhoneCallReceiver extends BroadcastReceiver {
    private static final String TAG = "PhoneCallReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() == Intent.ACTION_NEW_OUTGOING_CALL){
            PackageManager manager = context.getPackageManager();
            try{
                Intent mainIntent = manager.getLaunchIntentForPackage(context.getPackageName());
                if(mainIntent == null)
                    Log.e(TAG, "onReceive: can't open mainthread");
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                mainIntent.setAction("Recognition");
                context.startActivity(mainIntent);
            }catch (Exception e){
                Log.e(TAG, "onReceive: can't open mainthread");
            }
        }
    }
}
