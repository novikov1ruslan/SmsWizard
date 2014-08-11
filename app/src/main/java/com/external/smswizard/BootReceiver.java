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
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent broadcast = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, broadcast, PendingIntent.FLAG_CANCEL_CURRENT);
//        am.cancel(pendingIntent); // will cancel all alarms whose intent matches this one
//            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + DELAY, AlarmManager.INTERVAL_HOUR, pendingIntent);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + DELAY, DELAY, pendingIntent);
        }
    }
}
