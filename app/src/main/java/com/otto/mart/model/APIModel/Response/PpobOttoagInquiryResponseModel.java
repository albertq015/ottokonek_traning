package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.MultifinaceDataModel;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

public class PpobOttoagInquiryResponseModel extends BaseResponseModel {
    PpobMultifinanceInquiryData data;

    public PpobOttoagInquiryResponseModel() {
    }

    public PpobMultifinanceInquiryData getData() {
        return data;
    }

    public void setData(PpobMultifinanceInquiryData data) {
        this.data = data;
    }

    public String getRc() {
        return data.rc;
    }

    public void setRc(String rc) {
        data.rc = rc;
    }

    public String getRrn() {
        return data.rrn;
    }

    public void setRrn(String rrn) {
        data.rrn = rrn;
    }

    public int getAdminfee() {
        return data.adminfee;
    }

    public void setAdminfee(int adminfee) {
        data.adminfee = adminfee;
    }

    public long getAmount() {
        return data.amount;
    }

    public void setAmount(long amount) {
        data.amount = amount;
    }

    public String getCustid() {
        return data.custid;
    }

    public void setCustid(String custid) {
        data.custid = custid;
    }

    public String getMemberid() {
        return data.memberid;
    }

    public void setMemberid(String memberid) {
        data.memberid = memberid;
    }

    public String getMsg() {
        return data.msg;
    }

    public void setMsg(String msg) {
        data.msg = msg;
    }

    public String getPeriode() {
        return data.periode;
    }

    public void setPeriode(String periode) {
        data.periode = periode;
    }

    public String getProductcode() {
        return data.productcode;
    }

    public void setProductcode(String productcode) {
        data.productcode = productcode;
    }

    public String getUimsg() {
        return data.uimsg;
    }

    public void setUimsg(String uimsg) {
        data.uimsg = uimsg;
    }

    public List<MultifinaceDataModel> getList_detail() {
        return data.list_detail;
    }

    public void setList_detail(List<MultifinaceDataModel> list_detail) {
        data.list_detail = list_detail;
    }

    public List<WidgetBuilderModel> getKey_value_list() {
        return data.key_value_list;
    }

    public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
        data.key_value_list = key_value_list;
    }

    public int getTotalpremium() {
        return data.totalpremium;
    }

    public void setTotalpremium(int totalpremium) {
        data.totalpremium = totalpremium;
    }

    public String getProduct_logo() {
        return data.product_logo;
    }

    public void setProduct_logo(String product_logo) {
        data.product_logo = product_logo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PpobMultifinanceInquiryData {
        String rc;
        String rrn;
        int adminfee;
        long amount;
        String custid;
        String memberid;
        String msg;
        String periode;
        String productcode;
        String uimsg;
        List<MultifinaceDataModel> list_detail;
        List<WidgetBuilderModel> key_value_list;
        int totalpremium;
        String product_logo;
        String period;
        String cashback_omzet;

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
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

        public int getAdminfee() {
            return adminfee;
        }

        public void setAdminfee(int adminfee) {
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

        public String getPeriode() {
            return periode;
        }

        public void setPeriode(String periode) {
            this.periode = periode;
        }

        public String getProductcode() {
            return productcode;
        }

        public void setProductcode(String productcode) {
            this.productcode = productcode;
        }

        public String getUimsg() {
            return uimsg;
        }

        public void setUimsg(String uimsg) {
            this.uimsg = uimsg;
        }

        public List<MultifinaceDataModel> getList_detail() {
            return list_detail;
        }

        public void setList_detail(List<MultifinaceDataModel> list_detail) {
            this.list_detail = list_detail;
        }

        public List<WidgetBuilderModel> getKey_value_list() {
            return key_value_list;
        }

        public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
            this.key_value_list = key_value_list;
        }

        public int getTotalpremium() {
            return totalpremium;
        }

        public void setTotalpremium(int totalpremium) {
            this.totalpremium = totalpremium;
        }

        public String getProduct_logo() {
            return product_logo;
        }

        public void setProduct_logo(String product_logo) {
            this.product_logo = product_logo;
        }

        public String getCashback_omzet() {
            return cashback_omzet;
        }

        public void setCashback_omzet(String cashback_omzet) {
            this.cashback_omzet = cashback_omzet;
        }
    }
}
