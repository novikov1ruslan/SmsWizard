package com.external.smswizard;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class PollingService extends IntentService {

    private static final String TAG = "PollingService";

    public PollingService() {
        super("PollingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Completed service @ " + SystemClock.elapsedRealtime());
        AlarmReceiver.completeWakefulIntent(intent);
    }

}
