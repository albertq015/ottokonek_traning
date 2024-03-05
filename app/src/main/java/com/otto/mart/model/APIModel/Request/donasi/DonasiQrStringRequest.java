package com.otto.mart.model.APIModel.Request.donasi;

public class DonasiQrStringRequest {

    private long amount;
    private String bill_ref_num;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getBill_ref_num() {
        return bill_ref_num;
    }

    public void setBill_ref_num(String bill_ref_num) {
        this.bill_ref_num = bill_ref_num;
    }
}
