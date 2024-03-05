package com.otto.mart.model.APIModel.Request.grosir;

public class GrosirCheckStatusRequest {
    private String merchant_phone;
    private String order_no;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getMerchant_phone() {
        return merchant_phone;
    }

    public void setMerchant_phone(String merchant_phone) {
        this.merchant_phone = merchant_phone;
    }
}
