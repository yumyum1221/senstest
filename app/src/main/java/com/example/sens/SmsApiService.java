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
import retrofit2.http.POST;

public interface SmsApiService {
    String access_key = "BuildConfig.APPLICATION_CLIENT_ID";
    long timestamp = System.currentTimeMillis();
    String signature = getSignature();

    @FormUrlEncoded
    @POST("https://sens.apigw.ntruss.com/sms/v2/services/ncp:sms:kr:305347621568:tojung/messages")
    Call<SmsResponse> sendSms(
            @Header("x-ncp-apigw-timestamp") long timestamp,
            @Header("x-ncp-iam-access-key") String access_key,
            @Header("x-ncp-apigw-signature-v2") String signature,
            @Field("type") String type,
            @Field("contentType") String contentType,
            @Field("to") String to,
            @Field("content") String content,
            @Field("countryCode") String countryCode
    );

    public static String getSignature() {
        String space = " "; // one space
        String newLine = "\n"; // new line
        String method = "POST"; // method
        String url = "/sms/v2/services/ncp:sms:kr:305347621568:tojung/messages"; // url (include query string)
        String timestamp = String.valueOf(System.currentTimeMillis()); // current timestamp (epoch)
        String accessKey = "BuildConfig.APPLICATION_CLIENT_ID"; // access key id (from portal or Sub Account)
        String secretKey = "BuildConfig.APPLICATION_CLIENT_SECRET";

        StringBuilder buffer = new StringBuilder();
        buffer.append(method);
        buffer.append(space);
        buffer.append(url);
        buffer.append(newLine);
        buffer.append(timestamp);
        buffer.append(newLine);
        buffer.append(accessKey);

        String message = buffer.toString();

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(signingKey);
            byte[] digest = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(digest);
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }
}