package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.AuthDataModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponseModel extends BaseResponseModel implements Serializable {

    private AuthDataModel data;

    public AuthDataModel getData() {
        return data;
    }

    public void setData(AuthDataModel data) {
        this.data = data;
    }

}


