package com.example.sens;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SmsApiService {
    @FormUrlEncoded
    @POST("/sms/v2/services/ncp:sms:kr:271028460775:chatgpt/messages")
    Call<SmsResponse> sendSms(
            @Field("from") String from,
            @Field("to") String to,
            @Field("content") String content
    );
}

