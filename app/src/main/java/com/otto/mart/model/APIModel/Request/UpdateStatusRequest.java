package com.otto.mart.model.APIModel.Request;

public class UpdateStatusRequest {
    private String otp_code;
    private int security_question_id;
    private String answer;
    private String new_pin;

    public String getOtp_code() {
        return otp_code;
    }

    public void setOtp_code(String otp_code) {
        this.otp_code = otp_code;
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

    public String getNew_pin() {
        return new_pin;
    }

    public void setNew_pin(String new_pin) {
        this.new_pin = new_pin;
    }
}
