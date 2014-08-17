package com.external.smswizard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.external.smswizard.model.ApplicationModel;

import roboguice.util.Ln;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isOn = new ApplicationModel(context.getApplicationContext()).isApplicationOn();
        Ln.d("isOn=" + isOn);
        if (isOn) {
            AlarmUtils.scheduleActivities(context);
        }
    }
}
