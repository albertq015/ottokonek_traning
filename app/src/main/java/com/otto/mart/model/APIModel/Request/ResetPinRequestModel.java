package com.otto.mart.model.APIModel.Request;

public class ResetPinRequestModel {
    int customerId;
    int customer_id;
    String phone;
    String new_pin;
    String otp_code;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNew_pin() {
        return new_pin;
    }

    public void setNew_pin(String new_pin) {
        this.new_pin = new_pin;
    }

    public String getOtp_code() {
        return otp_code;
    }

    public void setOtp_code(String otp_code) {
        this.otp_code = otp_code;
    }
}
