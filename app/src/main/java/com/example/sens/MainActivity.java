package com.example.sens;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void sendSms(String from, String to, String content) {
        SmsApiClient smsApiClient = SmsApiClient.getInstance();
        Call<SmsResponse> call = smsApiClient.sendSms(from, to, content);
        call.enqueue(new Callback<SmsResponse>() {
            @Override
            public void onResponse(Call<SmsResponse> call, Response<SmsResponse> response) {
                if (response.isSuccessful()) {
                    SmsResponse smsResponse = response.body();
                    String statusCode = String.valueOf(smsResponse.getStatusCode());
                    String statusName = smsResponse.getStatusName();
                    // TODO: SMS 보내기 성공 처리
                } else {
                    // SMS 보내기 실패 처리
                    // response.errorBody()를 통해 실패 이유 확인 가능
                }
            }

            @Override
            public void onFailure(Call<SmsResponse> call, Throwable t) {
                // SMS 보내기 실패 처리
            }
        });
    }
}