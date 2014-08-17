package com.external.smswizard;

import android.app.Application;
import android.content.Context;

import com.external.smswizard.model.ApplicationModel;
import com.external.smswizard.model.Message;
import com.external.smswizard.model.Token;

import java.util.LinkedList;
import java.util.List;

import retrofit.http.Field;

public class SmsWizard extends Application {

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
//        RestFactory.injectService(smsRetrofit);
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
