package com.otto.mart.model.APIModel.Response.BaseModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.otto.mart.model.APIModel.Misc.PaymentData;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class BasePaymentResponseModel extends BaseResponseModel {

    public PaymentData data;

    public PaymentData getData() {
        return data;
    }

    public void setData(PaymentData data) {
        this.data = data;
    }
}