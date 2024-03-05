package com.otto.mart.model.APIModel.Request;

import com.otto.mart.model.APIModel.Request.Base.BasePpobPaymentRequestModel;

public class PpobListrikPaymentRequestModel extends BasePpobPaymentRequestModel {

    Object inquiry_data;

    public Object getInquiry_data() {
        return inquiry_data;
    }

    public void setInquiry_data(Object inquiry_data) {
        this.inquiry_data = inquiry_data;
    }
}
