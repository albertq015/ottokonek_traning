package com.otto.mart.model.APIModel.Request;

import com.otto.mart.model.APIModel.Request.Base.BasePpobPaymentRequestModel;

public class PpobOttoagPaymentRequestModel extends BasePpobPaymentRequestModel {
    String biller_code;
    String product_code;
    String provider_code;
    long fee;
    int admin_fee;
    long sub_amount;
    long amount;
    String inquiry_data;
    int months;
    int total_premium;

    //voucher gaem
    String userid;
    String zoneid;

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

    public String getProvider_code() {
        return provider_code;
    }

    public void setProvider_code(String provider_code) {
        this.provider_code = provider_code;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long price) {
        this.fee = price;
    }

    public int getAdmin_fee() {
        return admin_fee;
    }

    public void setAdmin_fee(int admin_fee) {
        this.admin_fee = admin_fee;
    }

    public long getSub_amount() {
        return sub_amount;
    }

    public void setSub_amount(long sub_amount) {
        this.sub_amount = sub_amount;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getInquiry_data() {
        return inquiry_data;
    }

    public void setInquiry_data(String inquiry_data) {
        this.inquiry_data = inquiry_data;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getTotal_premium() {
        return total_premium;
    }

    public void setTotal_premium(int total_premium) {
        this.total_premium = total_premium;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getZoneid() {
        return zoneid;
    }

    public void setZoneid(String zoneid) {
        this.zoneid = zoneid;
    }
}
