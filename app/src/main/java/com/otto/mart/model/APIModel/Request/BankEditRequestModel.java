package com.otto.mart.model.APIModel.Request;

public class BankEditRequestModel {
    private int bank_account_id;
    private String account_number;
    private int bank_id;
    private String account_owner;
    private boolean set_as_main;

    public int getBank_account_id() {
        return bank_account_id;
    }

    public void setBank_account_id(int bankAccount_id) {
        this.bank_account_id = bankAccount_id;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public int getBank_id() {
        return bank_id;
    }

    public void setBank_id(int bank_id) {
        this.bank_id = bank_id;
    }

    public String getAccount_owner() {
        return account_owner;
    }

    public void setAccount_owner(String account_owner) {
        this.account_owner = account_owner;
    }

    public boolean isSet_as_main() {
        return set_as_main;
    }

    public void setSet_as_main(boolean set_as_main) {
        this.set_as_main = set_as_main;
    }
}
