package com.otto.mart.model.APIModel.Response.donasi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DonasiInquiryResponse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        private InquiryData inquiry_data;
        private List<String> key_value_list;

        public InquiryData getInquiry_data() {
            return inquiry_data;
        }

        public void setInquiry_data(InquiryData inquiry_data) {
            this.inquiry_data = inquiry_data;
        }

        public List<String> getKey_value_list() {
            return key_value_list;
        }

        public void setKey_value_list(List<String> key_value_list) {
            this.key_value_list = key_value_list;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class InquiryData {

            private long adminfee;
            private long amount;
            private String custid;
            private String memberid;
            private String msg;
            private String period;
            private String productcode;
            private String rc;
            private String rrn;
            private String uimsg;

            public long getAdminfee() {
                return adminfee;
            }

            public void setAdminfee(long adminfee) {
                this.adminfee = adminfee;
            }

            public long getAmount() {
                return amount;
            }

            public void setAmount(long amount) {
                this.amount = amount;
            }

            public String getCustid() {
                return custid;
            }

            public void setCustid(String custid) {
                this.custid = custid;
            }

            public String getMemberid() {
                return memberid;
            }

            public void setMemberid(String memberid) {
                this.memberid = memberid;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getPeriod() {
                return period;
            }

            public void setPeriod(String period) {
                this.period = period;
            }

            public String getProductcode() {
                return productcode;
            }

            public void setProductcode(String productcode) {
                this.productcode = productcode;
            }

            public String getRc() {
                return rc;
            }

            public void setRc(String rc) {
                this.rc = rc;
            }

            public String getRrn() {
                return rrn;
            }

            public void setRrn(String rrn) {
                this.rrn = rrn;
            }

            public String getUimsg() {
                return uimsg;
            }

            public void setUimsg(String uimsg) {
                this.uimsg = uimsg;
            }
        }
    }
}
