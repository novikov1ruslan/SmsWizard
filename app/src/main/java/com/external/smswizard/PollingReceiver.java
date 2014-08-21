package com.external.smswizard;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.external.smswizard.model.ApplicationModel;

import roboguice.util.Ln;

public class PollingReceiver extends WakefulBroadcastReceiver {

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

    public static void schedule(Context context) {
        Ln.d("scheduling polling event every 10 seconds");
        AlarmUtils.scheduleAlarm(context, getPendingIntent(context), SmsWizard.OUTGOING_MESSAGES_POLLING_PERIOD);
    }

    public static void cancel(Context context) {
        AlarmUtils.cancelAlarm(context, getPendingIntent(context));
        Ln.d("polling cancelled");
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent broadcast = new Intent(context, PollingReceiver.class);
        return PendingIntent.getBroadcast(context, 0, broadcast, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
