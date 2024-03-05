package com.otto.mart.model.APIModel.Response.inbox;

import androidx.annotation.Keep;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

public class InboxUpdateBulkResponse extends BaseResponseModel {

    @Keep
    public InboxUpdateBulkResponse() {
    }

    @JsonProperty("data")
    Object data;
}