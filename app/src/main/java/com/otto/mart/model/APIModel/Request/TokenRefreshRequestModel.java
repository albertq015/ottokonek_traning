package com.otto.mart.model.APIModel.Request;

public class TokenRefreshRequestModel extends TokenRequestModel {
    private String refresh_token;

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
