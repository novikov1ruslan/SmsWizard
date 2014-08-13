package com.external.smswizard;

import android.app.Application;

import com.external.smswizard.model.Message;
import com.external.smswizard.model.Token;

import java.util.LinkedList;
import java.util.List;

import retrofit.http.Field;

public class SmsWizard extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RestFactory.injectService(new SmsService() {
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
            public String setIncomingMessage(@Field("token") String token, @Field("message_id") String messageId) {
                return null;
            }
        });
    }
}
