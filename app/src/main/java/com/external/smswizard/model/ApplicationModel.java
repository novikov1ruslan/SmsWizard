package com.external.smswizard.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import roboguice.util.Ln;

public class ApplicationModel {
    public static final String APPLICATION_ON = "APPLICATION_ON";
    private static final String TOKEN = "TOKEN";
    private static final String ILLEGAL_TOKEN = "";
    public static final String MODEL_NAME = "application_model";
    private static final String EMAIL = "EMAIL";

    private final SharedPreferences.Editor editor;
    private final SharedPreferences sharedPreferences;
    private DatabaseHelper helper;

    public ApplicationModel(Context context) {
        sharedPreferences = context.getSharedPreferences(MODEL_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        helper = new DatabaseHelper(context);
    }

    public void setApplicationOn() {
        editor.putBoolean(APPLICATION_ON, true).apply();
        Ln.d("app enabled");
    }

    public void setApplicationOff() {
        editor.putBoolean(APPLICATION_ON, false).apply();
        Ln.d("app disabled");
    }

    public void forgetToken() {
        editor.putString(TOKEN, ILLEGAL_TOKEN).apply();
        Ln.d("token forgotten");
    }

    public void setToken(String token) {
        if (TextUtils.isEmpty(token)) {
            throw new IllegalArgumentException();
        }

        Ln.d(token);

        editor.putString(TOKEN, token).apply();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN, null);
    }

    public boolean hasToken() {
        return !TextUtils.isEmpty(getToken());
    }

    public boolean isApplicationOn() {
        return sharedPreferences.getBoolean(APPLICATION_ON, false);
    }

    public void addMessage(String id, String phone) {
        Ln.d("id=%, phone=%s", id, phone);
        helper.addData(new Message(id, phone));
    }

    public void setEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            throw new IllegalArgumentException();
        }

        Ln.d(email);

        editor.putString(EMAIL, email).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, null);
    }

    @DatabaseTable(tableName = "messages")
    public class Message {
        @DatabaseField(id = true)
        private String id;

        @DatabaseField(canBeNull = false)
        private String number;

        public Message(String id, String number) {
            this.id = id;
            this.number = number;
        }

        @Override
        public String toString() {
            return "[id=" + id + "; number=" + number + "]";
        }
    }

}
