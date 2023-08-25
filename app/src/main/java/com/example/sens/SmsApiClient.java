package com.example.sens;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SmsApiClient {
    private static final String BASE_URL = "https://sens.apigw.ntruss.com/sms/v2/services/ncp:sms:kr:305347621568:tojung/";
    private static SmsApiService smsApiService;

    private SmsApiClient() {
    }

    public static SmsApiService getInstance() {
        if (smsApiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            smsApiService = retrofit.create(SmsApiService.class);
        }
        return smsApiService;
    }
}