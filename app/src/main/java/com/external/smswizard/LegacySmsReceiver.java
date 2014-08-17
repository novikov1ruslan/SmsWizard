package com.external.smswizard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.external.smswizard.model.ApplicationModel;
import com.external.smswizard.model.Message;

import java.util.List;

import roboguice.util.Ln;

public class LegacySmsReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            try {
                ApplicationModel model = new ApplicationModel(context);

                Object[] pdus = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdus.length; i++) {
                    Ln.d("pdu=" + pdus[i]);
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String number = sms.getDisplayOriginatingAddress();
                    String incomingText = sms.getDisplayMessageBody();
                    Ln.d("senderNum: " + number + "; message: " + incomingText);
                    List<Message> messages = model.getMessagesByNumber(number);
                    if (!messages.isEmpty()) {
                        Message message = messages.get(0);
                        Ln.d("Starting service @ %s for message %s" + SystemClock.elapsedRealtime(), message);
                        Intent incomingMessageIntent = RestService.getIncomingMessageIntent(context, model.getToken(), model.getEmail(), message.id, incomingText);
                        incomingMessageIntent.putExtra(RestService.EXTRA_WAKEFUL, true);
                        startWakefulService(context, incomingMessageIntent);
                    }

                    return;
                }
            } catch (RuntimeException rte) {
                Ln.w(rte, "sms parse failed, reason: [%s]", rte.getCause());
            }
        }
    }
}
