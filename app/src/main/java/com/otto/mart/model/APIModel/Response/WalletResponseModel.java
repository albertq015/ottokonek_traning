package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.WalletDataModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletResponseModel extends BaseResponseModel {

    List<WalletDataModel> data;

    public List<WalletDataModel> getData() {
        return data;
    }

    public void setData(List<WalletDataModel> data) {
        this.data = data;
    }
}

