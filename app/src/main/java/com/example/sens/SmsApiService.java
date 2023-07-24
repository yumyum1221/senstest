package com.example.sens;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

//SENS API의 엔드포인트와 요청 파라미터를 정의하는 인터페이스입니다.
//sendSms() 메서드를 정의하여 SMS 발송에 필요한 정보를 요청 파라미터로 전달합니다.

public interface SmsApiService {
    String sensApiKey = BuildConfig.SENS_API_KEY;
    @Headers({
            "Content-Type: application/x-www-form-urlencoded; charset=utf-8",
            "x-ncp-apigw-timestamp: {timestamp}",
            "x-ncp-iam-access-key: {sensApiKey}",
            "x-ncp-apigw-signature-v2: {signature}"
    })
    @FormUrlEncoded
    @POST("https://sens.apigw.ntruss.com/sms/v2/services/{sensApiKey}/messages")
    Call<SmsResponse> sendSms(
            @Field("type") String type,
            @Field("contentType") String contentType,
            @Field("to") String to,
            @Field("content") String content,
            @Field("countryCode") String countryCode
    );
}


