package com.external.smswizard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.telephony.SmsMessage;

import roboguice.util.Ln;

public class LegacySmsReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            try {
                byte[][] pdus = (byte[][]) bundle.get("pdus");

                for (int i = 0; i < pdus.length; i++) {
                    SmsMessage sms = SmsMessage.createFromPdu(pdus[i]);
                    String number = sms.getDisplayOriginatingAddress();

                    String text = sms.getDisplayMessageBody();
                    Ln.d("senderNum: " + number + "; message: " + text);
                    Ln.d("Starting service @ " + SystemClock.elapsedRealtime());
                    Intent outgoingMessagesIntent = RestService.set  getOutgoingMessagesIntent(context, token, email);
                    outgoingMessagesIntent.putExtra(RestService.EXTRA_WAKEFUL, true);
                    startWakefulService(context, outgoingMessagesIntent);
                }
            } catch (RuntimeException rte) {
                Ln.w("sms receive failed, reason: [%s]", rte.getCause());
            }
        }
    }
}
