package com.otto.mart.model.APIModel.Response.BaseModel;

import androidx.annotation.Keep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import app.beelabs.com.codebase.base.response.BaseResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponseModel extends BaseResponse {

    @Keep
    public BaseResponseModel() {

    }

    @JsonProperty("meta")
    private MetaModel meta;

    public MetaModel getMeta() {
        return meta;
    }

    public void setMeta(MetaModel meta) {
        this.meta = meta;
    }

}