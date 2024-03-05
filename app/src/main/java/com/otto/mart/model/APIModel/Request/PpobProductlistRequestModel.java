package com.otto.mart.model.APIModel.Request;

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

public class PpobProductlistRequestModel extends BaseResponseModel {

    String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
