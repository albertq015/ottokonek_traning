package com.otto.mart.model.APIModel.Response.faq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FAQResponseModel extends BaseResponseModel {
    private FAQResponseData data;

    public FAQResponseData getData() {
        return data;
    }

    public FAQResponseModel setData(FAQResponseData data) {
        this.data = data;
        return this;
    }
}
