package com.external.smswizard;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.external.smswizard.model.ApplicationModel;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.EventBusException;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public class RestService extends IntentService {
    private static final String TAG = "RestService";
    public static final String EXTRA_EMAIL = "com.external.smswizard.EXTRA_EMAIL";
    public static final String EXTRA_PASSWORD = "com.external.smswizard.EXTRA_PASSWORD";

    SmsService service = new RestAdapter.Builder().setEndpoint("http://95.85.39.81:5000/api").build().create(SmsService.class);

    public RestService(String name) {
        super(name);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String email = intent.getStringExtra(EXTRA_EMAIL);
        String password  = intent.getStringExtra(EXTRA_PASSWORD);

        try {
            Token token = service.getToken(email, password);
            Log.d(TAG, "token=" + token);
            ApplicationModel applicationModel = new ApplicationModel(getApplicationContext());
            applicationModel.setToken(token.token);
            EventBus.getDefault().post(token);
        } catch (RetrofitError error) {
            Log.d(TAG, error.getMessage());
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
}
