package com.otto.mart.model.APIModel.Response.merchant.theme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantThemeResponse extends BaseResponseModel {
    MerchantTheme data;

    public MerchantTheme getData() {
        return data;
    }

    public void setData(MerchantTheme data) {
        this.data = data;
    }
}
