package com.otto.mart.model.APIModel.Response.multibank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.otto.mart.model.APIModel.Misc.PaymentData;

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptAdressResponse extends BaseResponseModel {


    private PaymentData data;

    public PaymentData getData() {
        return data;
    }

    public void setData(PaymentData data) {
        this.data = data;
    }

}
