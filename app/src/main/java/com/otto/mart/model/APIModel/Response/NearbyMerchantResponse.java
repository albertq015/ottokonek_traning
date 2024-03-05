package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.nearbyMerchant.NearbyMerchant;

import java.util.List;

import app.beelabs.com.codebase.base.response.BaseResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NearbyMerchantResponse extends BaseResponse {
    private int code;
    private String successMessage;
    private String errorMessage;
    private List<NearbyMerchant> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<NearbyMerchant> getData() {
        return data;
    }

    public void setData(List<NearbyMerchant> data) {
        this.data = data;
    }
}
