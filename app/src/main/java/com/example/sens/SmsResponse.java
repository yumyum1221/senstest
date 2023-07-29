package com.example.sens;

import com.google.gson.annotations.SerializedName;

public class SmsResponse {
    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("statusName")
    private String statusName;

    @SerializedName("requestId")
    private String requestId;

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public String getRequestId() {
        return requestId;
    }
}
