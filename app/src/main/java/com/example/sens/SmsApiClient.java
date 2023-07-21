package com.example.sens;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SmsApiClient {
    private static final String BASE_URL = "https://sens.apigw.ntruss.com/sms/v2/";
    private static SmsApiClient instance;
    private SmsApiService smsApiService;

    private SmsApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        smsApiService = retrofit.create(SmsApiService.class);
    }

    public static synchronized SmsApiClient getInstance() {
        if (instance == null) {
            instance = new SmsApiClient();
        }
        return instance;
    }

    public Call<SmsResponse> sendSms(String from, String to, String content, String contentType, String countryCode) {
        return smsApiService.sendSms("sms", contentType, from, to, content, countryCode);
    }
}