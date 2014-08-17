package com.external.smswizard;

import retrofit.RestAdapter;

public class RestFactory {

    private static SmsRetrofit service = new RestAdapter.Builder().setEndpoint("http://95.85.39.81:5000/api").build().create(SmsRetrofit.class);

    public static void injectService(SmsRetrofit service) {
        RestFactory.service = service;
    }

    public static SmsRetrofit getService() {
        return service;
    }

}
