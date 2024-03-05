package com.otto.mart.model.APIModel.Request.donasi;

public class DonasiQRPaymentRequest {

    private String product_code;
    private String customer_reference;
    private int admin_fee;
    private int sub_amount;
    private int amount;
    private String inquiry_data;
    private int wallet_id;
    private String pin;

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

    public int getAdmin_fee() {
        return admin_fee;
    }

    public void setAdmin_fee(int admin_fee) {
        this.admin_fee = admin_fee;
    }

    public int getSub_amount() {
        return sub_amount;
    }

    public void setSub_amount(int sub_amount) {
        this.sub_amount = sub_amount;
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

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}