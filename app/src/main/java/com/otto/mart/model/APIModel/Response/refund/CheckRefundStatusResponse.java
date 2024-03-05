package com.otto.mart.model.APIModel.Response.refund;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import app.beelabs.com.codebase.base.response.BaseResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckRefundStatusResponse extends BaseResponseModel {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Data {
        private String rc;
        private boolean refundable;
        private String refundRrn;

        public String getRc() {
            return rc;
        }

        public void setRc(String rc) {
            this.rc = rc;
        }

        public boolean isRefundable() {
            return refundable;
        }

        public void setRefundable(boolean refundable) {
            this.refundable = refundable;
        }

        public String getRefundRrn() {
            return refundRrn;
        }

        public void setRefundRrn(String refundRrn) {
            this.refundRrn = refundRrn;
        }
    }
}