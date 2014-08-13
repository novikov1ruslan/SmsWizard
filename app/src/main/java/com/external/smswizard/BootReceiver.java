package com.external.smswizard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.external.smswizard.model.ApplicationModel;

import roboguice.util.Ln;

public class BootReceiver extends BroadcastReceiver {
    private static final long DELAY = 10000;

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean hasToken = new ApplicationModel(context.getApplicationContext()).hasToken();
        Ln.d("hasToken=" + hasToken);
        if (hasToken) {
            AlarmReceiver.schedulePolling(context);
        }
    }
}
