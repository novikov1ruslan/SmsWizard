package com.external.smswizard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.external.smswizard.model.ApplicationModel;

import roboguice.util.Ln;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    private static long DELAY = 10000; // TODO: increase

    @Override
    public void onReceive(Context context, Intent intent) {
        ApplicationModel model = new ApplicationModel(context);
        String token = model.getToken();
        String email = model.getEmail();
        Ln.d("token=%s, email=%s", token, email);

        Ln.d("Starting service @ " + SystemClock.elapsedRealtime());
        Intent outgoingMessagesIntent = RestService.getOutgoingMessagesIntent(context, token, email);
        outgoingMessagesIntent.putExtra(RestService.EXTRA_WAKEFUL, true);
        startWakefulService(context, outgoingMessagesIntent);
    }

    public static void schedulePolling(Context context) {
        Ln.d("scheduling polling event every 10 seconds");
        scheduleAlarm(context, getPendingIntent(context));
    }

    private static void scheduleAlarm(Context context, PendingIntent pendingIntent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + DELAY, AlarmManager.INTERVAL_HOUR, pendingIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + DELAY, DELAY, pendingIntent);
    }

    public static void cancelPolling(Context context) {
        cancelAlarm(context, getPendingIntent(context));
        Ln.d("polling cancelled");
    }

    private static void cancelAlarm(Context context, PendingIntent pendingIntent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent); // will cancel all alarms whose intent matches this one
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent broadcast = new Intent(context, AlarmReceiver.class);
        return PendingIntent.getBroadcast(context, 0, broadcast, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
