package com.otto.mart.model.APIModel.Response.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QrQrisInquiryResponse extends BaseResponseModel {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Data {
        private String rrn;
        private String merchantName;
        private String merchantAddress;
        private int amount;
        private String cpan;
        private String mpan;
        private String nmid;

        public String getCpan() {
            return cpan;
        }

        public void setCpan(String cpan) {
            this.cpan = cpan;
        }

        public String getMpan() {
            return mpan;
        }

        public void setMpan(String mpan) {
            this.mpan = mpan;
        }

        public String getNmid() {
            return nmid;
        }

        public void setNmid(String nmid) {
            this.nmid = nmid;
        }

        public String getRrn() {
            return rrn;
        }

        public void setRrn(String rrn) {
            this.rrn = rrn;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public String getMerchantAddress() {
            return merchantAddress;
        }

        public void setMerchantAddress(String merchantAddress) {
            this.merchantAddress = merchantAddress;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
