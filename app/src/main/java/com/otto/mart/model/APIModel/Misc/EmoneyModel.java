package com.otto.indomarco.Model.APIModel.Misc;

public class EmoneyModel {
    int total_wallet;
    int available_wallet;
    int total_credit_limit;
    int available_credit_limit;

    public int getTotal_wallet() {
        return total_wallet;
    }

    public void setTotal_wallet(int total_wallet) {
        this.total_wallet = total_wallet;
    }

    public int getAvailable_wallet() {
        return available_wallet;
    }

    public void setAvailable_wallet(int available_wallet) {
        this.available_wallet = available_wallet;
    }

    public int getTotal_credit_limit() {
        return total_credit_limit;
    }

    public void setTotal_credit_limit(int total_credit_limit) {
        this.total_credit_limit = total_credit_limit;
    }

    public int getAvailable_credit_limit() {
        return available_credit_limit;
    }

    public void setAvailable_credit_limit(int available_credit_limit) {
        this.available_credit_limit = available_credit_limit;
    }
}
