package com.external.smswizard;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;

import roboguice.util.Ln;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, PollingService.class);

        // Start the service, keeping the device awake while it is launching.
        Ln.i("Starting service @ " + SystemClock.elapsedRealtime());
        startWakefulService(context, service);
    }
}
