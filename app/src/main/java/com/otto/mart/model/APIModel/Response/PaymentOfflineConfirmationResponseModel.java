package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentOfflineConfirmationResponseModel extends BaseResponseModel {

    public PaymentOfflineConfirmationResponseModel() {
    }
}
