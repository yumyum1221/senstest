package com.example.sens;

import android.os.Build;

import java.nio.charset.StandardCharsets;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import android.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SmsApiService {
    String access_key = BuildConfig.APPLICATION_CLIENT_ID;
    long timestamp = System.currentTimeMillis();
    String signature = getSignature();

    @FormUrlEncoded
    @POST("/sms/v2/services/ncp:sms:kr:305347621568:tojung/messages")
    Call<SmsResponse> sendSms(
            @Header("x-ncp-apigw-timestamp") long timestamp,
            @Header("x-ncp-iam-access-key") String access_key,
            @Header("x-ncp-apigw-signature-v2") String signature,
            @Field("type") String type, //sms
            @Field("contentType") String contentType, //ad(광고)인지 comm(일반)인지
            @Field("to") String to, //수신번호
            @Field("content") String content, //메시지 내용
            @Field("countryCode") String countryCode //국가전화번호
    );

    public static String getSignature() {
        String space = " "; // one space //
        String newLine = "\n"; // new line
        String method = "POST"; // method
        String url = "/sms/v2/services/ncp:sms:kr:305347621568:tojung/messages"; // url (include query string)
        String timestamp = String.valueOf(System.currentTimeMillis()); // current timestamp (epoch)
        String accessKey = BuildConfig.APPLICATION_CLIENT_ID; // access key id (from portal or Sub Account)
        String secretKey = BuildConfig.APPLICATION_CLIENT_SECRET;

        String message = new StringBuilder()
            .append(method)
            .append(space)
            .append(url)
            .append(newLine)
            .append(timestamp)
            .append(newLine)
            .append(accessKey)
            .toString();
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            byte[] base64Bytes = Base64.encode(rawHmac, Base64.DEFAULT);
            String encodeBase64String = new String(base64Bytes, StandardCharsets.UTF_8);


            return encodeBase64String;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;


    }
}