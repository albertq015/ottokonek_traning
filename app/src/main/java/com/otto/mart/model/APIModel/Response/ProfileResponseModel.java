package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.ProfileResponseData;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileResponseModel extends BaseResponseModel {


    private ProfileResponseData data;

    public ProfileResponseData getData() {
        return data;
    }

    public void setData(ProfileResponseData data) {
        this.data = data;
    }
}
