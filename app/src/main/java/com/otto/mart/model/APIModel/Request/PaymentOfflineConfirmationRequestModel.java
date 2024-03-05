package com.otto.mart.model.APIModel.Request;

public class PaymentOfflineConfirmationRequestModel {

    String pin;
    int amount = 0;
    int admin_fee = 0;
    String rrn;
    int wallet_id;
    private String merchant_id;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAdmin_fee() {
        return admin_fee;
    }

    public void setAdmin_fee(int admin_fee) {
        this.admin_fee = admin_fee;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }
}
