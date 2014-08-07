package com.external.smswizard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.external.smswizard.model.ApplicationModel;

public class BootReceiver extends BroadcastReceiver {
    private static final long DELAY = 10000;
    private static final String ACTION = "";

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (new ApplicationModel(context.getApplicationContext()).hasToken()) {

        }
    }

    public static void schedule(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        am.cancel(pendingIntent); // will cancel all alarms whose intent matches this one
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + DELAY, DELAY, pendingIntent);
    }
}
