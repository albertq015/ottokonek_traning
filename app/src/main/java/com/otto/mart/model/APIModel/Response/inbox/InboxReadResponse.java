package com.otto.mart.model.APIModel.Response.inbox;

import androidx.annotation.Keep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InboxReadResponse  extends BaseResponseModel {
    @Keep
    public InboxReadResponse() {
    }

    @JsonProperty("data")
    private Object data;

    public Object getData() {
        return data;
    }

    public InboxReadResponse setData(Object data) {
        this.data = data;
        return this;
    }
}