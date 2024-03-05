package com.otto.mart.model.APIModel.Request;

public class ResetOtpPinRequestModel {

    String phone;
    int security_question_id;
    String answer;
    double longitude;
    double latitude;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSecurity_question_id() {
        return security_question_id;
    }

    public void setSecurity_question_id(int security_question_id) {
        this.security_question_id = security_question_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
