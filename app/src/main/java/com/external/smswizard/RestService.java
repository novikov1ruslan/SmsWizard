package com.external.smswizard;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.SmsManager;

import com.external.smswizard.model.ApplicationModel;
import com.external.smswizard.model.Message;
import com.external.smswizard.model.Token;

import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.RetrofitError;
import roboguice.util.Ln;

public class RestService extends IntentService {
    public static final String ACTION_GET_TOKEN = "com.external.smswizard.intent.action.GET_TOKEN";
    public static final String ACTION_GET_OUTGOING_MESSAGES = "com.external.smswizard.intent.action.GET_OUTGOING_MESSAGES";
    public static final String ACTION_SET_INCOMING_MESSAGE = "com.external.smswizard.intent.action.SET_INCOMING_MESSAGE";

    public static final String EXTRA_EMAIL = "com.external.smswizard.EXTRA_EMAIL";
    public static final String EXTRA_PASSWORD = "com.external.smswizard.EXTRA_PASSWORD";
    public static final String EXTRA_TOKEN = "com.external.smswizard.EXTRA_TOKEN";
    public static final String EXTRA_MESSAGE_ID = "com.external.smswizard.EXTRA_MESSAGE_ID";
    public static final String EXTRA_MESSAGE_TEXT = "com.external.smswizard.EXTRA_MESSAGE_TEXT";


    public static final String EXTRA_WAKEFUL = "com.external.smswizard.EXTRA_WAKEFUL";


    SmsRetrofit service = RestFactory.getService();

    public static void login(Context context, String email, String password) {
        Ln.d("starting login for (%s, %s)", email, password);
        Intent intent = new Intent(context, RestService.class);
        intent.setAction(RestService.ACTION_GET_TOKEN);
        intent.putExtra(RestService.EXTRA_EMAIL, email);
        intent.putExtra(RestService.EXTRA_PASSWORD, password);
        context.startService(intent);
    }

    public RestService() {
        super("RestService");
    }

    public RestService(String name) {
        super(name);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (ACTION_GET_TOKEN.equals(action)) {
            String email = intent.getStringExtra(EXTRA_EMAIL);
            String password = intent.getStringExtra(EXTRA_PASSWORD);
            doGetToken(email, password);
        } else if (ACTION_GET_OUTGOING_MESSAGES.equals(action)) {
            String token = intent.getStringExtra(EXTRA_TOKEN);
            String email = intent.getStringExtra(EXTRA_EMAIL);
            doGetOutgoingMessages(token, email);
        } else if (ACTION_SET_INCOMING_MESSAGE.equals(action)) {
            String token = intent.getStringExtra(EXTRA_TOKEN);
            String email = intent.getStringExtra(EXTRA_EMAIL);
            String messageText = intent.getStringExtra(EXTRA_MESSAGE_TEXT);
            String messageId = intent.getStringExtra(EXTRA_MESSAGE_ID);
            doSetIncomingMessage(token, email, messageId, messageText);
        }

        if (intent.getBooleanExtra(EXTRA_WAKEFUL, false)) {
            Ln.d("Completed service @ " + SystemClock.elapsedRealtime());
            DeletionReceiver.completeWakefulIntent(intent);
        }
    }

    private void doSetIncomingMessage(String token, String email, String messageId, String text) {
        service.setIncomingMessage(token, email, messageId, text);
    }

    private void doGetOutgoingMessages(String token, String email) {
        try {
            List<Message> messages = service.getOutgoingMessages(token, email);
            Ln.d("messages=%s", messages);
            storeMessages(messages);
            // TODO: uncomment for production
//            sendSmsFor(messages);
        } catch (RetrofitError error) {
            Ln.w(error.getMessage());
        }
    }

    private void sendSmsFor(List<Message> messages) {
        SmsManager smsManager = SmsManager.getDefault();
        for (Message message : messages) {
            Ln.d("sending SMS for: " + message);
            smsManager.sendTextMessage(message.number, null, message.text, null, null);
        }
    }

    private void storeMessages(List<Message> messages) {
        ApplicationModel applicationModel = new ApplicationModel(getApplicationContext());
        for (Message message : messages) {
            if (applicationModel.getMessageForId(message.id) == null) {
                applicationModel.addMessage(message.id, message.number);
            }
            else {
                Ln.d("message id = %s already present", message.id);
            }
        }
    }

    private void doGetToken(String email, String password) {
        Ln.d("email=%s, password=%s", email, password);

        try {
            Token token = service.getToken(email, password);
            Ln.d("token=%s, saving to model and posting event", token);
            ApplicationModel applicationModel = new ApplicationModel(getApplicationContext());
            applicationModel.setToken(token.token);
            EventBus.getDefault().post(token);
        } catch (RetrofitError error) {
            Ln.w(error.getMessage());
            EventBus.getDefault().post((Token) null);
        }
    }

    public static Intent getOutgoingMessagesIntent(Context context, String token, String email) {
        Intent intent = new Intent(context, RestService.class);
        intent.setAction(RestService.ACTION_GET_OUTGOING_MESSAGES);
        intent.putExtra(RestService.EXTRA_TOKEN, token);
        intent.putExtra(RestService.EXTRA_EMAIL, email);
        return intent;
    }

    public static Intent getIncomingMessageIntent(Context context, String token, String email, String messageId, String messageText) {
        Intent intent = new Intent(context, RestService.class);
        intent.setAction(RestService.ACTION_SET_INCOMING_MESSAGE);
        intent.putExtra(RestService.EXTRA_TOKEN, token);
        intent.putExtra(RestService.EXTRA_EMAIL, email);
        intent.putExtra(RestService.EXTRA_MESSAGE_ID, messageId);
        intent.putExtra(RestService.EXTRA_MESSAGE_TEXT, messageText);
        return intent;
    }

}
