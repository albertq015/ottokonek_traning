package com.otto.mart.model.APIModel.Request;

import com.otto.mart.model.APIModel.Request.Base.BasePpobPaymentRequestModel;

public class PpobTelkomPaymentRequestModel extends BasePpobPaymentRequestModel {
    private String biller_code;
    private String product_code;
    private String provider_code;

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
}
