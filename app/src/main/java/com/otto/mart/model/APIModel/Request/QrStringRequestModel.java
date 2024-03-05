package com.otto.mart.model.APIModel.Request;

public class QrStringRequestModel {

    Double amount;
    long feeAmount;
    long feePercentage;
    String storeLabel;
    String bill_ref_num;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public long getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(long feeAmount) {
        this.feeAmount = feeAmount;
    }

    public long getFeePercentage() {
        return feePercentage;
    }

    public void setFeePercentage(long feePercentage) {
        this.feePercentage = feePercentage;
    }

    public String getStoreLabel() {
        return storeLabel;
    }

    public void setStoreLabel(String storeLabel) {
        this.storeLabel = storeLabel;
    }

    public String getBill_ref_num() {
        return bill_ref_num;
    }

    public void setBill_ref_num(String bill_ref_num) {
        this.bill_ref_num = bill_ref_num;
    }
}
