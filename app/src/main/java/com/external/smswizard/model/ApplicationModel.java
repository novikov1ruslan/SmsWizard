package com.external.smswizard.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class ApplicationModel {
    public static final String APPLICATION_ON = "APPLICATION_ON";
    private static final String TOKEN = "TOKEN";
    private static final String ILLEGAL_TOKEN = "";
    public static final String MODEL_NAME = "application_model";

    private final SharedPreferences.Editor editor;
    private final SharedPreferences sharedPreferences;

    public ApplicationModel(Context context) {
        sharedPreferences = context.getSharedPreferences(MODEL_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setApplicationOn() {
        editor.putBoolean(APPLICATION_ON, true).apply();
    }

    public void setApplicationOff() {
        editor.putBoolean(APPLICATION_ON, false).apply();
    }

    public void forgetToken() {
        editor.putString(TOKEN, ILLEGAL_TOKEN).apply();
    }

    public void setToken(String token) {
        if (TextUtils.isEmpty(token)) {
            throw new IllegalArgumentException();
        }

        editor.putString(TOKEN, token).apply();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN, null);
    }

    public boolean hasToken() {
        return !TextUtils.isEmpty(getToken());
    }
}
