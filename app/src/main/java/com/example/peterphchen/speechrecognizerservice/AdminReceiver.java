package com.example.peterphchen.speechrecognizerservice;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class AdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
    public void onEnabled(Context context, Intent intent) {
    }
    public void onDisabled(Context context, Intent intent) {
    }
}
