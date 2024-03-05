package com.otto.mart.model.APIModel.Request.multibank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidateQrRequest {


    @JsonProperty("qr_data")
    private String qrData;

    public void setQrData(String qrData) {
        this.qrData = qrData;
    }

    public String getQrData() {
        return qrData;
    }

}
