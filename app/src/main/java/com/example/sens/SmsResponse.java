package com.example.sens;

import com.google.gson.annotations.SerializedName;

public class SmsResponse {
    @SerializedName("statusCode")
    private int statusCode;
    @SerializedName("statusName")
    private String statusName;
    @SerializedName("requestId")
    private String requestId;

    public SmsResponse(int statusCode, String statusName, String requestId) {
        this.statusCode = statusCode;
        this.statusName = statusName;
        this.requestId = requestId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}