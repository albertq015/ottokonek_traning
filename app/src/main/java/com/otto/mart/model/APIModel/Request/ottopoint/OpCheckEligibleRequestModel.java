package com.otto.mart.model.APIModel.Request.ottopoint;

public class OpCheckEligibleRequestModel {

    private String phone;

    public OpCheckEligibleRequestModel(String phone){
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
