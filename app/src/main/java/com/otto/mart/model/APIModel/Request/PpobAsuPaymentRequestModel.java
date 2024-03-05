package com.otto.mart.model.APIModel.Request;

import com.otto.mart.model.APIModel.Request.Base.BasePpobPaymentRequestModel;

public class PpobAsuPaymentRequestModel extends BasePpobPaymentRequestModel {

    String product_code;
    int months;
    int admin_fee;
    int total_premium;
    int amount;
    String inquiry_data;
    int wallet_id;
    String customer_reference;
    int sub_amount;

    @Override
    public String getCustomer_reference() {
        return customer_reference;
    }

    @Override
    public void setCustomer_reference(String customer_reference) {
        this.customer_reference = customer_reference;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getAdmin_fee() {
        return admin_fee;
    }

    public void setAdmin_fee(int admin_fee) {
        this.admin_fee = admin_fee;
    }

    public int getTotal_premium() {
        return total_premium;
    }

    public void setTotal_premium(int total_premium) {
        this.total_premium = total_premium;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getInquiry_data() {
        return inquiry_data;
    }

    public void setInquiry_data(String inquiry_data) {
        this.inquiry_data = inquiry_data;
    }

    @Override
    public int getWallet_id() {
        return wallet_id;
    }

    @Override
    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public int getSub_amount() {
        return sub_amount;
    }

    public void setSub_amount(int sub_amount) {
        this.sub_amount = sub_amount;
    }
}
