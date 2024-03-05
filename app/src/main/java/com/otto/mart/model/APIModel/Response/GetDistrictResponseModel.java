package com.otto.mart.model.APIModel.Response;

import androidx.annotation.Keep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Misc.CategoryModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDistrictResponseModel extends BaseResponseModel {

    @Keep
    public GetDistrictResponseModel() {
    }

    @JsonProperty("data")
    private DistrictResponseData data;

    public List<CategoryModel> getDistrict() {
        return data.getDistrict();
    }

    public void setCountry(List<CategoryModel> district) {
        this.data.setDistrict(district);
    }

    public DistrictResponseData getData() {
        return data;
    }

    public void setData(DistrictResponseData data) {
        this.data = data;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class DistrictResponseData {

    @Keep
    public DistrictResponseData() {
    }

    @JsonProperty("district")
    private List<CategoryModel> district;

    public List<CategoryModel> getDistrict() {
        return district;
    }

    public void setDistrict(List<CategoryModel> district) {
        this.district = district;
    }
}
