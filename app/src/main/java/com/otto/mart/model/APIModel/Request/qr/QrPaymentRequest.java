package com.otto.mart.model.APIModel.Request.qr;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QrPaymentRequest {

    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("bin")
    private String bin;
    @JsonProperty("tipFeeFixed")
    private int tipFeeFixed;
    @JsonProperty("transactionAmount")
    private double transactionAmount;


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


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getTipFeeFixed() {
        return tipFeeFixed;
    }

    public void setTipFeeFixed(int tipFeeFixed) {
        this.tipFeeFixed = tipFeeFixed;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}