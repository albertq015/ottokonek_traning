package com.otto.mart.model.APIModel.Response.donasi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DonasiQrStringResponse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        private String qr_string;

        public String getQr_string() {
            return qr_string;
        }

        public void setQr_string(String qr_string) {
            this.qr_string = qr_string;
        }
    }
}