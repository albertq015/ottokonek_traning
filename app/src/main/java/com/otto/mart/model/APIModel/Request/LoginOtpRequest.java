package com.otto.mart.model.APIModel.Request;

public class LoginOtpRequest {
    String user_id;
    String otp_code;
    double latitude;
    double longitude;
    String firebase_token;
    String firebase_token_sandbox;
    String version;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOtp_code() {
        return otp_code;
    }

    public void setOtp_code(String otp_code) {
        this.otp_code = otp_code;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFirebase_token() {
        return firebase_token;
    }

    public void setFirebase_token(String firebase_token) {
        this.firebase_token = firebase_token;
    }

    public String getFirebase_token_sandbox() {
        return firebase_token_sandbox;
    }

    public void setFirebase_token_sandbox(String firebase_token_sandbox) {
        this.firebase_token_sandbox = firebase_token_sandbox;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
