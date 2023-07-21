package com.example.sens;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private EditText phoneEditText, messageEditText;
    private Button sendButton;
    String sensApiKey = BuildConfig.SENS_API_KEY;

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
                String phoneNumber = phoneEditText.getText().toString().trim();
                String message = messageEditText.getText().toString().trim();

                if (phoneNumber.isEmpty() || message.isEmpty()) {
                    Toast.makeText(MainActivity.this, "전화번호와 문자 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
                } else {
                    // Retrofit을 사용하여 SENS API 호출
                    sendSms(phoneNumber, message);
                }
            }
        });
    }


    private void sendSms(String phoneNumber, String message) {
        SmsApiClient.getInstance()
                .sendSms("SMS", "COMM", "YOUR_PHONE_NUMBER", phoneNumber, message)
                .enqueue(new Callback<SmsResponse>() {
                    @Override
                    public void onResponse(Call<SmsResponse> call, Response<SmsResponse> response) {
                        if (response.isSuccessful()) {
                            // SMS 발송 성공
                            Toast.makeText(MainActivity.this, "문자가 발송되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            int errorCode = response.code(); // 응답 코드 가져오기
                            // SMS 발송 실패
                            Toast.makeText(MainActivity.this, "문자 발송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SmsResponse> call, Throwable t) {
                        // 통신 실패
                        Toast.makeText(MainActivity.this, "통신 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String phoneNumber = phoneEditText.getText().toString().trim();
                String message = messageEditText.getText().toString().trim();
                // Retrofit을 사용하여 SENS API 호출
                sendSms(phoneNumber, message);
            } else {
                Toast.makeText(this, "SMS 전송 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}