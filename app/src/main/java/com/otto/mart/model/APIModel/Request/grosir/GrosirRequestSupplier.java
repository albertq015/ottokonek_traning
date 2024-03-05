package com.otto.mart.model.APIModel.Request.grosir;

public class GrosirRequestSupplier {

    private String merchant_phone;
    private String area_id;

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getMerchant_phone() {
        return merchant_phone;
    }

    public void setMerchant_phone(String merchant_phone) {
        this.merchant_phone = merchant_phone;
    }
}
