package com.otto.mart.model.APIModel.Response.olshop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.olshop.IdNameModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressResponseModel extends BaseResponseModel {
    private List<IdNameModel> data;
    private String msg;
    private String rc;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public List<IdNameModel> getData() {
        return data;
    }

    public void setData(List<IdNameModel> data) {
        this.data = data;
    }
}
