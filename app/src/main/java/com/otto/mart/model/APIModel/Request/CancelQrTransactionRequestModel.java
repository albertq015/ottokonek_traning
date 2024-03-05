package com.otto.mart.model.APIModel.Request;

public class CancelQrTransactionRequestModel {


    String bill_ref_num;
    int wallet_id;
    String type;
    String json;

    public String getBill_ref_num() {
        return bill_ref_num;
    }

    public void setBill_ref_num(String bill_ref_num) {
        this.bill_ref_num = bill_ref_num;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
