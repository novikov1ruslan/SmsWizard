package com.external.smswizard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

public class AlarmUtils {

    private AlarmUtils() {}


    public static void scheduleAlarm(Context context, PendingIntent pendingIntent, long delay) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + DELAY, AlarmManager.INTERVAL_HOUR, pendingIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, delay, pendingIntent);
    }

    public static void cancelAlarm(Context context, PendingIntent pendingIntent) {
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).cancel(pendingIntent);
    }

    public static void scheduleActivities(Context context) {
        PollingReceiver.schedule(context);
        DeletionReceiver.schedule(context);
    }

    public static void cancelActivities(Context context) {
        PollingReceiver.cancel(context);
        DeletionReceiver.cancel(context);
    }
}
