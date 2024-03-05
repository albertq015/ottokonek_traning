package com.otto.mart.model.APIModel.Response.olshop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReversalResponseModel extends BaseResponseModel {

    ReversalResponseData data;

    public ReversalResponseData getData() {
        return data;
    }

    public void setData(ReversalResponseData data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ReversalResponseData{
        String responseDescription;
        String responseCode;

        public String getResponseDescription() {
            return responseDescription;
        }

        public void setResponseDescription(String responseDescription) {
            this.responseDescription = responseDescription;
        }

        public String getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(String responseCode) {
            this.responseCode = responseCode;
        }
    }
}
