package com.otto.mart.model.APIModel.Request.donasi;

public class DonasiInquiryRequest {

    private String product_code;
    private String customer_reference;

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
}
