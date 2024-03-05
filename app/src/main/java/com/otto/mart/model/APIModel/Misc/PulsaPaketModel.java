package com.otto.mart.model.APIModel.Misc;

public class PulsaPaketModel {
    private String phone;
    private String productName;

    public PulsaPaketModel() {
    }

    public PulsaPaketModel(String phone, String productName) {
        this.phone = phone;
        this.productName = productName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
