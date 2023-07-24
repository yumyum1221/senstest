package com.example.sens;
import static com.example.sens.SmsApiService.sensApiKey;

import android.os.Build;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Retrofit을 사용하여 SENS API와 통신하는 코드가 포함된 클래스입니다.
//SENS API에 요청을 보내고 응답을 받기 위한 Retrofit 인스턴스를 생성합니다.
//getInstance() 메서드로 싱글톤 패턴을 이용하여 Retrofit 인스턴스를 반환합니다.

public class SmsApiClient {
    private String serviceId = BuildConfig.SENS_API_KEY;
    private static final String BASE_URL = "https://sens.apigw.ntruss.com/sms/v2/services/{serviceId}/messages/";
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