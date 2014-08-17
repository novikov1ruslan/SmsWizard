package com.external.smswizard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import roboguice.util.Ln;

public class AlarmUtils {

    private AlarmUtils() {
    }


    public static void scheduleAlarm(Context context, PendingIntent pendingIntent, long delay) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + DELAY, AlarmManager.INTERVAL_HOUR, pendingIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, delay, pendingIntent);
    }

    public static void cancelAlarm(Context context, PendingIntent pendingIntent) {
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).cancel(pendingIntent);
    }

    public static void scheduleActivities(Context context) {
        Ln.d("enabling SMS receiver, scheduling polling and deletion tasks");
        setSmsReceiverEnabled(context, PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
        PollingReceiver.schedule(context);
        DeletionReceiver.schedule(context);
    }

    private static void setSmsReceiverEnabled(Context context, int flag) {
        ComponentName component = new ComponentName(context, LegacySmsReceiver.class);
        context.getPackageManager().setComponentEnabledSetting(component, flag, PackageManager.DONT_KILL_APP);
    }

    public static void cancelActivities(Context context) {
        Ln.d("disabling SMS receiver, canceling polling and deletion tasks");
        setSmsReceiverEnabled(context, PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
        PollingReceiver.cancel(context);
        DeletionReceiver.cancel(context);
    }
}
