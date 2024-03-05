package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.SecureQuestionModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityQuestionResponseModel extends BaseResponseModel {
    List<SecureQuestionModel> data;

    public List<SecureQuestionModel> getData() {
        return data;
    }

    public void setData(List<SecureQuestionModel> data) {
        this.data = data;
    }
}
