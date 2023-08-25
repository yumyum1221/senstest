package com.example.sens;
import java.io.UnsupportedEncodingException;
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

    public static String makeSignature(String url,
                                       String timestamp,
                                       String method,
                                       String accessKey,
                                       String secretKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";                    // one space
        String newLine = "\n";                 // new line


        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey;
        String encodeBase64String;
        try {

            signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            encodeBase64String = e.toString();
        }

        return encodeBase64String;
    }

}