package com.example.peterphchen.speechrecognizerservice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;


public class PhoneCallReceiver extends BroadcastReceiver {
    private static final String TAG = "PhoneCallReceiver";
    private String incoming_number;
    private int prev_state;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Receive Called");
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new CustomPhoneStateListener(context),PhoneStateListener.LISTEN_CALL_STATE);
        Bundle bundle = intent.getExtras();
        String phoneNr = bundle.getString("incoming_number");
        try {
            if (bundle.getString(TelephonyManager.EXTRA_STATE).equals("IDLE")) {
                Intent actionIntent = new Intent();
                actionIntent.setClassName(context.getPackageName(), overlapService.class.getName());
                Log.d(TAG, "onReceive: stop service");
                context.stopService(actionIntent);
            }
        }catch (Exception e){
            Log.e(TAG, "onReceive: Error:"+e.getMessage() );
        }
        Log.d(TAG, "onReceive: state: " +bundle.getString(TelephonyManager.EXTRA_STATE));
        Log.d(TAG, "onReceive: prev_state: " +prev_state);
    }
    public class CustomPhoneStateListener extends PhoneStateListener{
        private static final String TAG = "CustomPhoneStateListener";
        private Context ctxt;

        public CustomPhoneStateListener(Context context) {
            ctxt = context;
        }

        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            if(phoneNumber != null && phoneNumber.length() >0){
                Intent actionIntent = new Intent();
                actionIntent.setClassName(ctxt.getPackageName(),overlapService.class.getName());
                //actionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                incoming_number = phoneNumber;
                switch (state){
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d(TAG, "onCallStateChanged: Call state ringing");
                        prev_state = state;
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.d(TAG, "onCallStateChanged: Call state offhook");
                        Log.d(TAG, "onCallStateChanged: Start overlayService");
                        actionIntent.putExtra("phone_number",phoneNumber);
                        ctxt.startForegroundService(actionIntent);
                        prev_state = state;
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d(TAG, "onCallStateChanged: prev_state: "+prev_state);
                        Log.d(TAG, "onCallStateChanged: Call state idle");
                        Log.d(TAG, "onCallStateChanged: Stop Service");
                        ctxt.stopService(actionIntent);
                        prev_state = state;
                        break;
                }
            }
        }
    }
}
