package com.otto.mart.model.APIModel.Request;

public class RegisterOtpRequestModel extends LoginOtpRequestModel {

    private int question_id;
    private String answer;
    private String qr_merchant_id;
    private String phone;
    double latitude;
    double longitude;
    String otp_code;
    boolean rose;
    String request_id;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String getOtp_code() {
        return otp_code;
    }

    @Override
    public void setOtp_code(String otp_code) {
        this.otp_code = otp_code;
    }

    public String getQr_merchant_id() {
        return qr_merchant_id;
    }

    public void setQr_merchant_id(String qr_merchant_id) {
        this.qr_merchant_id = qr_merchant_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isRose() {
        return rose;
    }

    public void setRose(boolean rose) {
        this.rose = rose;
    }
}
