package com.otto.mart.model.APIModel.Response.payment;

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

public class IndomaretQrPurchaseCancelResponseModel extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String reference_number;
        private int expired_date;
        private String token;

        public String getReference_number() {
            return reference_number;
        }

        public void setReference_number(String reference_number) {
            this.reference_number = reference_number;
        }

        public int getExpired_date() {
            return expired_date;
        }

        public void setExpired_date(int expired_date) {
            this.expired_date = expired_date;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}