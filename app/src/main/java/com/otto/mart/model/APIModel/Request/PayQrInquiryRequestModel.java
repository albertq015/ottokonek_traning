package com.otto.mart.model.APIModel.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayQrInquiryRequestModel {

    @JsonProperty("qr_data")
    private String qrData;
    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("bin")
    private String bin;


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }



    public void setQrData(String qrData) {
        this.qrData = qrData;
    }

    public String getQrData() {
        return qrData;
    }
}