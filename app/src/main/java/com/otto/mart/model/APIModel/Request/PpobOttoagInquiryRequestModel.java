package com.otto.mart.model.APIModel.Request;

public class PpobOttoagInquiryRequestModel {
    String biller_code;
    String product_code;
    String provider_code;
    int denomination;
    String cust_id;
    String customer_reference;
    String period;
    String months;
    String rolename;
    boolean cashback;

    public String getBiller_code() {
        return biller_code;
    }

    public void setBiller_code(String biller_cpde) {
        this.biller_code = biller_cpde;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProvider_code() {
        return provider_code;
    }

    public void setProvider_code(String provider_code) {
        this.provider_code = provider_code;
    }

    public int getDenomination() {
        return denomination;
    }

    public void setDenomination(int denomination) {
        this.denomination = denomination;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getCustomer_reference() {
        return customer_reference;
    }

    public void setCustomer_reference(String customer_reference) {
        this.customer_reference = customer_reference;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public boolean isCashback() {
        return cashback;
    }

    public void setCashback(boolean cashback) {
        this.cashback = cashback;
    }
}
