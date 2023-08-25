package com.example.sens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private EditText phoneEditText, messageEditText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneEditText = findViewById(R.id.phoneEditText);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneEditText.getText().toString();
                String message = messageEditText.getText().toString();

                if (phoneNumber.isEmpty() || message.isEmpty()) {
                    Toast.makeText(MainActivity.this, "전화번호와 문자 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendSMS(phoneNumber, message);
                            Log.d("SMS","완료");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }
    private void sendSMS(String phoneNumber, String message) throws JSONException {
        String hostNameUrl = "https://sens.apigw.ntruss.com";     		// 호스트 URL
        String requestUrl= "/sms/v2/services/";                   		// 요청 URL
        String requestUrlType = "/messages";                      		// 요청 URL
        String accessKey = BuildConfig.APPLICATION_CLIENT_ID;           // 네이버 클라우드 플랫폼 회원에게 발급되는 개인 인증키
        String secretKey = BuildConfig.APPLICATION_CLIENT_SECRET;       // 2차 인증을 위해 서비스마다 할당되는 service secret key
        String serviceId = BuildConfig.SERVICE_ID;                      // 프로젝트에 할당된 SMS 서비스 ID
        String method = "POST";                 //요청 메소드
        String timestamp = Long.toString(System.currentTimeMillis());
        requestUrl += serviceId + requestUrlType;
        String apiUrl = hostNameUrl + requestUrl;

        //Json을 활용한 body data 생성

        JSONObject bodyJson = new JSONObject();
        JSONObject toJson = new JSONObject();
        JSONArray toArr = new JSONArray();

        toJson.put("content",message); //메시지내용
        toJson.put("to",phoneNumber); //수신 전화번호
        toArr.put(toJson);

        bodyJson.put("type","SMS"); //메시지 유형
        bodyJson.put("contentType","COMM"); //메시지 내용 타입
        bodyJson.put("content",message); //메시지 내용
        bodyJson.put("from","01022392701");
        bodyJson.put("messages", toArr); //수신자 정보(JSON 배열 구성)

        String body = bodyJson.toString();

        System.out.println(body);

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("content-type","application/json");
            con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
            con.setRequestProperty("x-ncp-iam-access-key", accessKey);
            con.setRequestProperty("x-ncp-apigw-signature-v2", SmsApiService.makeSignature(requestUrl, timestamp, method, accessKey, secretKey));
            con.setRequestMethod(method);
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());

            wr.write(body.getBytes());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            Log.d("MainActivity", "responseCode: " + responseCode);
            System.out.println("responseCode"+" "+responseCode);

            if(responseCode == 202) { //정상
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                Log.d("responsecode","정상");
            } else {        //에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                Log.d("responsecode","실패");
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = br.readLine()) != null){
                response.append(inputLine);
            }
            br.close();

            System.out.println(response.toString());

        } catch (Exception e) {
            System.out.println(e);
            Log.e("MainActivity", "Error during API call", e);
        }


    }

}