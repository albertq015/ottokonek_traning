package com.otto.mart.model.APIModel.Request.bank;

public class AddBankDepositRequest {
    private String account_number;
    private String account_owner;
    private String bank_code;
    private String bank_name;
    private String old_account_number;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOld_account_number() {
        return old_account_number;
    }

    public void setOld_account_number(String old_account_number) {
        this.old_account_number = old_account_number;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAccount_owner() {
        return account_owner;
    }

    public void setAccount_owner(String account_owner) {
        this.account_owner = account_owner;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }
}
