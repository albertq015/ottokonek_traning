package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PayQrInquiryResponse extends BaseResponseModel {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean {

        @JsonProperty("InquiryData")
        private InquiryDataBean InquiryData;

        public InquiryDataBean getInquiryData() {
            return InquiryData;
        }

        public void setInquiryData(InquiryDataBean InquiryData) {
            this.InquiryData = InquiryData;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class InquiryDataBean {

            private String merchant_name;
            private String rrn;
            private int admin_fee;
            private int amount;

            public String getMerchant_name() {
                return merchant_name;
            }

            public void setMerchant_name(String merchant_name) {
                this.merchant_name = merchant_name;
            }

            public String getRrn() {
                return rrn;
            }

            public void setRrn(String rrn) {
                this.rrn = rrn;
            }

            public int getAdmin_fee() {
                return admin_fee;
            }

            public void setAdmin_fee(int admin_fee) {
                this.admin_fee = admin_fee;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }
        }
    }
}
