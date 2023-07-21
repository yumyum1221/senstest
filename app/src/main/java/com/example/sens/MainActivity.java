package com.example.sens;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
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
                    sendSms(phoneNumber, message);
                }
            }
        });
    }

    private void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "문자가 발송되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "문자 발송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String phoneNumber = phoneEditText.getText().toString().trim();
                String message = messageEditText.getText().toString().trim();
                sendSms(phoneNumber, message);
            } else {
                Toast.makeText(this, "SMS 전송 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
