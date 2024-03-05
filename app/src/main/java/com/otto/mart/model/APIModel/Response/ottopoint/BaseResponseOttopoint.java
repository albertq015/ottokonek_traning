package com.otto.mart.model.APIModel.Response.ottopoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import app.beelabs.com.codebase.base.response.BaseResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
class BaseResponseOttopoint extends BaseResponse {

    public BaseResponseOttopoint() {
    }
}
