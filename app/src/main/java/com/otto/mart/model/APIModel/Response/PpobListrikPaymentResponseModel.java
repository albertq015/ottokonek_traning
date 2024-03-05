package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel2;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpobListrikPaymentResponseModel extends BasePaymentResponseModel2 {

    public PpobListrikPaymentResponseModel() {
    }
}
