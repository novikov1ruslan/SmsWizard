package com.external.smswizard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import roboguice.util.Ln;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Ln.i("device booted");
//        boolean isOn = new ApplicationModel(context.getApplicationContext()).isApplicationOn();
//        Ln.d("isOn=" + isOn);
//        if (isOn) {
//            AlarmUtils.scheduleActivities(context);
//        }
    }
}
