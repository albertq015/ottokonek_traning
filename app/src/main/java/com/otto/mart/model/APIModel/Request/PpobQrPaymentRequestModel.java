package com.otto.mart.model.APIModel.Request;

public class PpobQrPaymentRequestModel {
    String biller_ref_num;
    int wallet_id;
    String json;
    String pin;
    String type;
    long selling_price;
    long sub_amount;


    public String getBiller_ref_num() {
        return biller_ref_num;
    }

    public void setBiller_ref_num(String biller_ref_num) {
        this.biller_ref_num = biller_ref_num;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(long selling_price) {
        this.selling_price = selling_price;
    }

    public long getSub_amount() {
        return sub_amount;
    }

    public void setSub_amount(long sub_amount) {
        this.sub_amount = sub_amount;
    }
}
