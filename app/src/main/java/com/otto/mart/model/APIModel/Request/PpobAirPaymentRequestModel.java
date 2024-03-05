package com.otto.mart.model.APIModel.Request;

import com.otto.mart.model.APIModel.Request.Base.BasePpobPaymentRequestModel;

public class PpobAirPaymentRequestModel extends BasePpobPaymentRequestModel {
    String biller_code;
    String product_code;
    String customer_reference;
    String email;
    int wallet_id;


    public String getBiller_code() {
        return biller_code;
    }

    public void setBiller_code(String biller_code) {
        this.biller_code = biller_code;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getCustomer_reference() {
        return customer_reference;
    }

    public void setCustomer_reference(String customer_reference) {
        this.customer_reference = customer_reference;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }
}
