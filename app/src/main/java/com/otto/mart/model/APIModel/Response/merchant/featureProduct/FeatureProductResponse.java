package com.otto.mart.model.APIModel.Response.merchant.featureProduct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureProductResponse extends BaseResponseModel {

    private FeatureProductData data;

    public FeatureProductData getData() {
        return data;
    }

    public void setData(FeatureProductData data) {
        this.data = data;
    }
}