package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PPobPaymentQrResponseModel extends BaseResponseModel {

    PpobPaymentQrDataModel data;

    public PpobPaymentQrDataModel getModel() {
        return data;
    }

    public void setModel(PpobPaymentQrDataModel data) {
        this.data = data;
    }

    public String getBillingId() {
        return data.billingId;
    }

    public void setBillingId(String billingId) {
        data.billingId = billingId;
    }

    public String getBillerRefNum() {
        return data.billerRefNum;
    }

    public void setBillerRefNum(String billerRefNum) {
        data.billerRefNum = billerRefNum;
    }

    public String getJson() {
        return data.json;
    }

    public void setJson(String json) {
        data.json = json;
    }

    public PpobPaymentQrDataModel getData() {
        return data;
    }

    public void setData(PpobPaymentQrDataModel data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class PpobPaymentQrDataModel {
        String billingId;
        String billerRefNum;
        String json;

        public String getBillingId() {
            return billingId;
        }

        public void setBillingId(String billingId) {
            this.billingId = billingId;
        }

        public String getBillerRefNum() {
            return billerRefNum;
        }

        public void setBillerRefNum(String billerRefNum) {
            this.billerRefNum = billerRefNum;
        }

        public String getJson() {
            return json;
        }

        public void setJson(String json) {
            this.json = json;
        }
    }
}


