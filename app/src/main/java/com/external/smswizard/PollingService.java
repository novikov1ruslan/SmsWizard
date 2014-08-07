package com.external.smswizard;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public class PollingService extends IntentService {

    private static final String TAG = "PollingService";

    public class Token {
        public String token;
    }

    public interface SmsService {
        @FormUrlEncoded
        @POST("/get_token")
        Token getToken(@Field("email") String email, @Field("password") String password);

        @POST("/get_outgoing_messages")
        String getOutgoingMessages(@Field("token") String token, @Field("email") String email);

        @POST("/set_incoming_message")
        String setIncomingMessage(@Field("token") String token, @Field("message_id") String messageId);

        @GET("/users/{user}/repos")
        String getRepos(@Path("user") String user);
    }

    public PollingService() {
        super("PollingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "Completed service @ " + SystemClock.elapsedRealtime());
        AlarmReceiver.completeWakefulIntent(intent);
    }

}
