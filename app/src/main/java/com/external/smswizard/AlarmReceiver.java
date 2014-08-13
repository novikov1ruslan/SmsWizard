package com.external.smswizard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;

import roboguice.util.Ln;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    private static long DELAY = 10000; // TODO: increase

    @Override
    public void onReceive(Context context, Intent intent) {
//        RestService.getOutgoingMessages(context);
//        Intent service = new Intent(context, PollingService.class);
//
//        // Start the service, keeping the device awake while it is launching.
//        Ln.i("Starting service @ " + SystemClock.elapsedRealtime());
//        startWakefulService(context, service);

        String token = intent.getStringExtra(RestService.EXTRA_TOKEN);
        String email = intent.getStringExtra(RestService.EXTRA_EMAIL);
        Ln.d("token=%s, email=%s", token, email);

        Intent service = RestService.getOutgoingMessagesIntent(context, token, email);
        Ln.d("Starting service @ " + SystemClock.elapsedRealtime());
        startWakefulService(context, service);
    }

    public static void schedulePolling(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent broadcast = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, broadcast, PendingIntent.FLAG_CANCEL_CURRENT);
//            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + DELAY, AlarmManager.INTERVAL_HOUR, pendingIntent);
        Ln.d("scheduling polling event every 10 seconds");
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + DELAY, DELAY, pendingIntent);
    }

    public static void cancelPolling(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent broadcast = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, broadcast, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(pendingIntent); // will cancel all alarms whose intent matches this one
        Ln.d("polling cancelled");
    }
}
