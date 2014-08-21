package com.external.smswizard;

import android.app.Application;
import android.content.Context;

import com.external.smswizard.model.ApplicationModel;
import com.external.smswizard.model.Message;
import com.external.smswizard.model.Token;

import java.util.LinkedList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.http.Field;
import roboguice.util.Ln;

public class SmsWizard extends Application {

    private static final long SECOND = 1000;
    private static final long MINUTE = SECOND * 60;
    private static final long HOUR = MINUTE * 60;
    private static final long DAY = HOUR * 24;
    private static final long WEEK = DAY * 7;

    /**
     * Checking for messages oldness is done every so milliseconds {@link #MESSAGE_TTL}
     */
    public static final long MESSAGE_TTL_MONITORING_PERIOD = DAY;

    /**
     * Period of time (in millis) after which the message is considered to be old and should be deleted.
     */
    public static final long MESSAGE_TTL = WEEK;

    /**
     * Polling is done every so milliseconds.
     */
    public static final long OUTGOING_MESSAGES_POLLING_PERIOD = HOUR;

    private static SmsWizard instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SmsRetrofit smsRetrofit = new SmsRetrofit() {
            @Override
            public Token getToken(@Field("email") String email, @Field("password") String password) {
                return new Token("dummy_token_232434251");
            }

            @Override
            public List<Message> getOutgoingMessages(@Field("token") String token, @Field("email") String email) {
                List<Message> messages = new LinkedList<Message>();
                messages.add(new Message("id1", "number1"));
                messages.add(new Message("id2", "number2", "text2"));
                messages.add(new Message("id3", "number3"));
                return messages;
            }

            @Override
            public void setIncomingMessage(@Field("token") String token, @Field("email") String email,
                                           @Field("message_id") String messageId, @Field("text") String text) {
            }
        };

        smsRetrofit = new RestAdapter.Builder().setEndpoint("http://95.85.39.81:5000/api").build().create(SmsRetrofit.class);
        RestFactory.injectService(smsRetrofit);

        boolean isOn = new ApplicationModel(this).isApplicationOn();
        Ln.d("isOn=" + isOn);
        if (isOn) {
            AlarmUtils.scheduleActivities(this);
        }
    }

    public static void turnAppOff() {
        ApplicationModel model = new ApplicationModel(instance);
        model.setApplicationOff();
        AlarmUtils.cancelActivities(instance);
    }

    public static void turnAppOn() {
        ApplicationModel model = new ApplicationModel(instance);
        model.setApplicationOn();
        AlarmUtils.scheduleActivities(instance);
    }
}
