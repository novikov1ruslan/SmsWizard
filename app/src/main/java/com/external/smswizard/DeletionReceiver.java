package com.external.smswizard;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.external.smswizard.model.ApplicationModel;

import roboguice.util.Ln;

public class DeletionReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ApplicationModel model = new ApplicationModel(context);

        // TODO: replace minute by week
        int deleted = model.deleteMessagesOlderThan(System.currentTimeMillis() - SmsWizard.MESSAGE_TTL);
        Ln.d("deleted=" + deleted);
    }

    public static void schedule(Context context) {
        Ln.d("scheduling polling event every 10 seconds");
        AlarmUtils.scheduleAlarm(context, getPendingIntent(context), SmsWizard.MESSAGE_TTL_MONITORING_PERIOD);
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
