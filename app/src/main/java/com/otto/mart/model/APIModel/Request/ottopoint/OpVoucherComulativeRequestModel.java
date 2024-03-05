package com.otto.mart.model.APIModel.Request.ottopoint;

import java.io.Serializable;

public class OpVoucherComulativeRequestModel implements Serializable {

    private String name_voucher;
    private String Account_number;
    private int jumlah;
    private String campaign;
    private String category;
    private String cust_id;
    private String cust_id2;
    private String product_code;
    private String date;

    public OpVoucherComulativeRequestModel(String name_voucher, String account_number, int jumlah, String campaign, String category, String cust_id, String cust_id2, String product_code, String date) {
        this.name_voucher = name_voucher;
        this.Account_number = account_number;
        this.jumlah = jumlah;
        this.campaign = campaign;
        this.category = category;
        this.cust_id = cust_id;
        this.cust_id2 = cust_id2;
        this.product_code = product_code;
        this.date = date;
    }

    public String getName_voucher() {
        return name_voucher;
    }

    public void setName_voucher(String name_voucher) {
        this.name_voucher = name_voucher;
    }

    public String getAccount_number() {
        return Account_number;
    }

    public void setAccount_number(String account_number) {
        Account_number = account_number;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getCust_id2() {
        return cust_id2;
    }

    public void setCust_id2(String cust_id2) {
        this.cust_id2 = cust_id2;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
