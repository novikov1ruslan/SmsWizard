package com.external.smswizard;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.external.smswizard.model.ApplicationModel;

import roboguice.util.Ln;

public class DeletionReceiver extends WakefulBroadcastReceiver {
    private static final long DELAY = 20000; // TODO: increase

    private static final long SECOND = 1000;
    private static final long MINUTE = SECOND * 60;
    private static final long HOUR = MINUTE * 60;
    private static final long DAY = HOUR * 24;
    private static final long WEEK = DAY * 7;

    @Override
    public void onReceive(Context context, Intent intent) {
        ApplicationModel model = new ApplicationModel(context);

        // TODO: replace minute by week
        int deleted = model.deleteMessagesOlderThan(System.currentTimeMillis() - MINUTE);
        Ln.d("deleted=" + deleted);
    }

    public static void schedule(Context context) {
        Ln.d("scheduling polling event every 10 seconds");
        AlarmUtils.scheduleAlarm(context, getPendingIntent(context), DELAY);
    }

    public static void cancel(Context context) {
        AlarmUtils.cancelAlarm(context, getPendingIntent(context));
        Ln.d("polling cancelled");
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent broadcast = new Intent(context, DeletionReceiver.class);
        return PendingIntent.getBroadcast(context, 0, broadcast, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
