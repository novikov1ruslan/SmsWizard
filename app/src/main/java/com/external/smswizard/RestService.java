package com.external.smswizard;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.external.smswizard.model.ApplicationModel;

import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import roboguice.util.Ln;

public class RestService extends IntentService {
    public static final String ACTION_GET_TOKEN = "com.external.smswizard.intent.action.GET_TOKEN";
    public static final String ACTION_GET_OUTGOING_MESSAGES = "com.external.smswizard.intent.action.GET_OUTGOING_MESSAGES";
    public static final String ACTION_SET_INCOMING_MESSAGE = "com.external.smswizard.intent.action.SET_INCOMING_MESSAGE";

    public static final String EXTRA_EMAIL = "com.external.smswizard.EXTRA_EMAIL";
    public static final String EXTRA_PASSWORD = "com.external.smswizard.EXTRA_PASSWORD";
    public static final String EXTRA_TOKEN = "com.external.smswizard.EXTRA_TOKEN";
    public static final String EXTRA_MESSAGE_ID = "com.external.smswizard.EXTRA_MESSAGE_ID";


    SmsService service = new RestAdapter.Builder().setEndpoint("http://95.85.39.81:5000/api").build().create(SmsService.class);

    public static void login(Context context, String email, String password) {
        Ln.d("starting login for (%s, %s)", email, password);
        Intent intent = new Intent(context, RestService.class);
        intent.setAction(ACTION_GET_TOKEN);
        intent.putExtra(RestService.EXTRA_EMAIL, email);
        intent.putExtra(RestService.EXTRA_PASSWORD, password);
        context.startService(intent);
    }

    public static void getOutgoingMessages(Context context, String email, String password) {
        Ln.d("starting login for (%s, %s)", email, password);
        Intent intent = new Intent(context, RestService.class);
        intent.setAction(ACTION_GET_TOKEN);
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
            getToken(email, password);
        }
        else if (ACTION_GET_OUTGOING_MESSAGES.equals(action)) {
            String token = intent.getStringExtra(EXTRA_TOKEN);
            String email = intent.getStringExtra(EXTRA_EMAIL);
            doGetOutgoingMessages(token, email);
        }
        else if (ACTION_SET_INCOMING_MESSAGE.equals(action)) {
            String token = intent.getStringExtra(EXTRA_TOKEN);
            String messageId = intent.getStringExtra(EXTRA_MESSAGE_ID);
            doSetIncomingMessage(token, messageId);
        }
    }

    private void doSetIncomingMessage(String token, String messageId) {
        service.setIncomingMessage(token, messageId);
    }

    private void doGetOutgoingMessages(String token, String email) {
        try {
            List<Message> messages = service.getOutgoingMessages(token, email);
            Ln.d("messages=%s", messages);
            ApplicationModel applicationModel = new ApplicationModel(getApplicationContext());
            for (Message message: messages) {
                applicationModel.addMessage(message.id, message.phone);
            }

            EventBus.getDefault().post(token);
        } catch (RetrofitError error) {
            Ln.d(error.getMessage());
            EventBus.getDefault().post(new Token());
        }
    }

    private void getToken(String email, String password) {
        Ln.d("email=%s, password=%s", email, password);

        try {
            Token token = service.getToken(email, password);
            Ln.d("token=%s, saving to model and posting event", token);
            ApplicationModel applicationModel = new ApplicationModel(getApplicationContext());
            applicationModel.setToken(token.token);
            EventBus.getDefault().post(token);
        } catch (RetrofitError error) {
            Ln.d(error.getMessage());
            EventBus.getDefault().post(new Token());
        }
    }

    public class Token {
        public String token;

        @Override
        public String toString() {
            return "[token=" + token + "]";
        }
    }

    public class Message {
        public String id;
        public String phone;
        public String text;

        @Override
        public String toString() {
            return "[id=" + id + "; phone=" + phone + "; text=" + text + "]";
        }
    }

    public interface SmsService {
        @FormUrlEncoded
        @POST("/get_token")
        Token getToken(@Field("email") String email, @Field("password") String password);

        @POST("/get_outgoing_messages")
        List<Message> getOutgoingMessages(@Field("token") String token, @Field("email") String email);

        @POST("/set_incoming_message")
        String setIncomingMessage(@Field("token") String token, @Field("message_id") String messageId);

//        @GET("/users/{user}/repos")
//        String getRepos(@Path("user") String user);
    }
}
