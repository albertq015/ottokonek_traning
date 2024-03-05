package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponseModel extends BaseResponseModel {

    TokenDataModel data;

    public TokenDataModel getData() {
        return data;
    }

    public void setData(TokenDataModel data) {
        this.data = data;
    }

    public String getAccess_token() {
        return data.access_token;
    }

    public void setAccess_token(String access_token) {
        this.data.access_token = access_token;
    }

    public String getRefresh_token() {
        return data.refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.data.refresh_token = refresh_token;
    }

    public String getToken_type() {
        return data.token_type;
    }

    public void setToken_type(String token_type) {
        this.data.token_type = token_type;
    }

    public int getExpires_in() {
        return data.expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.data.expires_in = expires_in;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class TokenDataModel {
    String access_token;
    String refresh_token;
    String token_type;
    int expires_in;
}