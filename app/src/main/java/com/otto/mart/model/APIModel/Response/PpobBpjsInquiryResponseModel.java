package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.BpjsDataModel;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpobBpjsInquiryResponseModel extends BaseResponseModel {

    PpobAsuInquiryData data;

    public PpobBpjsInquiryResponseModel() {
    }

    public PpobAsuInquiryData getData() {
        return data;
    }

    public void setData(PpobAsuInquiryData data) {
        this.data = data;
    }

    public int getAdminfee() {
        return data.adminfee;
    }

    public void setAdminfee(int adminfee) {
        data.adminfee = adminfee;
    }

    public int getAmount() {
        return data.amount;
    }

    public void setAmount(int amount) {
        data.amount = amount;
    }

    public String getHeadva() {
        return data.headva;
    }

    public void setHeadva(String headva) {
        data.headva = headva;
    }

    public String getMemberid() {
        return data.memberid;
    }

    public void setMemberid(String memberid) {
        data.memberid = memberid;
    }

    public int getMonths() {
        return data.months;
    }

    public void setMonths(int months) {
        data.months = months;
    }

    public int getNumfm() {
        return data.numfm;
    }

    public void setNumfm(int numfm) {
        data.numfm = numfm;
    }

    public String getProductcode() {
        return data.productcode;
    }

    public void setProductcode(String productcode) {
        data.productcode = productcode;
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

    public int getTotalpremium() {
        return data.totalpremium;
    }

    public void setTotalpremium(int totalpremium) {
        data.totalpremium = totalpremium;
    }

    public String getVa() {
        return data.va;
    }

    public void setVa(String va) {
        data.va = va;
    }

    public List<BpjsDataModel> getList_detail() {
        return data.list_detail;
    }

    public void setList_detail(List<BpjsDataModel> list_detail) {
        data.list_detail = list_detail;
    }

    public List<WidgetBuilderModel> getKey_value_list() {
        return data.key_value_list;
    }

    public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
        data.key_value_list = key_value_list;
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
class PpobAsuInquiryData {
    int adminfee;
    int amount;
    String headva;
    String memberid;
    int months;
    String msg;
    int numfm;
    String productcode;
    String rc;
    String rrn;
    int totalpremium;
    String va;
    List<BpjsDataModel> list_detail;
    List<WidgetBuilderModel> key_value_list;


    public int getAdminfee() {
        return adminfee;
    }

    public void setAdminfee(int adminfee) {
        this.adminfee = adminfee;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getHeadva() {
        return headva;
    }

    public void setHeadva(String headva) {
        this.headva = headva;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getNumfm() {
        return numfm;
    }

    public void setNumfm(int numfm) {
        this.numfm = numfm;
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

    public int getTotalpremium() {
        return totalpremium;
    }

    public void setTotalpremium(int totalpremium) {
        this.totalpremium = totalpremium;
    }

    public String getVa() {
        return va;
    }

    public void setVa(String va) {
        this.va = va;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<BpjsDataModel> getList_detail() {
        return list_detail;
    }

    public void setList_detail(List<BpjsDataModel> list_detail) {
        this.list_detail = list_detail;
    }

    public List<WidgetBuilderModel> getKey_value_list() {
        return key_value_list;
    }

    public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
        this.key_value_list = key_value_list;
    }
}