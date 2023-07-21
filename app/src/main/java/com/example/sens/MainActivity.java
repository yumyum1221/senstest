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

//앱의 메인 화면과 관련된 코드가 포함된 클래스입니다.
//사용자로부터 전화번호와 문자 내용을 입력받고, 문자를 발송하는 기능을 구현합니다.
//onCreate() 메서드에서 앱이 시작될 때 초기화 작업을 수행합니다.
//sendButton의 클릭 이벤트 리스너를 등록하여 문자 발송 버튼을 눌렀을 때 동작을 처리합니다.
//SMS 발송에 필요한 권한을 확인하고, 권한이 있을 경우 sendSms() 메서드를 호출하여 SMS를 발송합니다

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
        String countryCode = "82";
        SmsApiClient.getInstance()
                .sendSms("SMS", "COMM", phoneNumber, message, countryCode)
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