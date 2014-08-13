package com.external.smswizard;

import android.app.Application;

import java.util.List;

import retrofit.http.Field;

public class SmsWizard extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RestFactory.injectService(new SmsService() {
            @Override
            public RestService.Token getToken(@Field("email") String email, @Field("password") String password) {
                return new RestService.Token("dummy_token_232434251");
            }

            @Override
            public List<RestService.Message> getOutgoingMessages(@Field("token") String token, @Field("email") String email) {
                return null;
            }

            @Override
            public String setIncomingMessage(@Field("token") String token, @Field("message_id") String messageId) {
                return null;
            }
        });
    }
}
