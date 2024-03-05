package com.otto.mart.model.APIModel.Request;

public class CheckMerchantRequestModel {
    private String phoneNumber;
    private String merchantId;

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantId() {
        return merchantId;
    }
}
