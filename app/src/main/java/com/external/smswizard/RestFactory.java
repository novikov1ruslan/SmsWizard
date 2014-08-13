package com.external.smswizard;

import retrofit.RestAdapter;

public class RestFactory {

    private static SmsService service = new RestAdapter.Builder().setEndpoint("http://95.85.39.81:5000/api").build().create(SmsService.class);

    public static void injectService(SmsService service) {
        RestFactory.service = service;
    }

    public static SmsService getService() {
        return service;
    }

}
