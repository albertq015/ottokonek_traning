package com.otto.mart.model.APIModel.Response.emoney;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.WalletDataModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmoneySummaryResponse extends BaseResponseModel {

    private WalletDataModel data;

    public WalletDataModel getData() {
        return data;
    }

    public void setData(WalletDataModel data) {
        this.data = data;
    }
}
