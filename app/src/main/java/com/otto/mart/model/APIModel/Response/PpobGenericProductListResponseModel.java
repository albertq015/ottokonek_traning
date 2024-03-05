package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.InsuranceModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpobGenericProductListResponseModel extends BaseResponseModel {

    private List<InsuranceModel> data;

    public List<InsuranceModel> getData() {
        return data;
    }

    public void setData(List<InsuranceModel> data) {
        this.data = data;
    }
}


