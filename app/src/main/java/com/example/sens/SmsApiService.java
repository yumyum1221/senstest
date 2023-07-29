package com.example.sens;

import android.os.Build;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SmsApiService {
    String access_key = BuildConfig.APPLICATION_CLIENT_ID;
    long timestamp = System.currentTimeMillis();
    String signature = getSignature(timestamp);

    @Headers({
            "Content-Type: application/x-www-form-urlencoded; charset=utf-8",
            "x-ncp-apigw-timestamp:  timestamp" , //현재시간을 가져와서 헤더추가
            "x-ncp-iam-access-key: " + access_key, //인증키의 access_key
            "x-ncp-apigw-signature-v2:" + signature
    })
    @FormUrlEncoded
    @POST("https://sens.apigw.ntruss.com/sms/v2/services/ncp:sms:kr:305347621568:tojung/messages")
    Call<SmsResponse> sendSms(
            @Field("type") String type,
            @Field("contentType") String contentType,
            @Field("to") String to,
            @Field("content") String content,
            @Field("countryCode") String countryCode
    );

    public static String getSignature(long timestamp) {
        String space = " "; // one space
        String newLine = "\n"; // new line
        String method = "POST"; // method
        String url = "/sms/v2/services/ncp:sms:kr:305347621568:tojung/messages";

        StringBuilder buffer = new StringBuilder();
        buffer.append(method);
        buffer.append(space);
        buffer.append(url);
        buffer.append(newLine);
        buffer.append(timestamp);
        buffer.append(newLine);
        buffer.append(access_key);
        System.out.println(buffer.toString());

        String secretKey = BuildConfig.APPLICATION_CLIENT_SECRET;
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] digest = mac.doFinal(buffer.toString().getBytes(StandardCharsets.UTF_8));
            return android.util.Base64.encodeToString(digest, android.util.Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }


        return null;
    }
}
