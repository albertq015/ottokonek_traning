package com.otto.mart.model.APIModel.Response.payment;

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

public class AlfamartPurchaseResponse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        private String token;
        private String reference_number;
        private String msisdn;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getReference_number() {
            return reference_number;
        }

        public void setReference_number(String reference_number) {
            this.reference_number = reference_number;
        }

        public String getMsisdn() {
            return msisdn;
        }

        public void setMsisdn(String msisdn) {
            this.msisdn = msisdn;
        }
    }
}