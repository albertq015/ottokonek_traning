package com.otto.mart.model.APIModel.Response;

import androidx.annotation.Keep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Misc.CategoryModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetProvinceResponsetModel extends BaseResponseModel {

    @Keep
    public GetProvinceResponsetModel() {
    }

    @JsonProperty("data")
    private ProvinceResponseData data;

    public List<CategoryModel> getProvince() {
        return data.getProvince();
    }

    public void setProvince(List<CategoryModel> province) {
        this.data.setProvince(province);
    }

    public ProvinceResponseData getData() {
        return data;
    }

    public void setData(ProvinceResponseData data) {
        this.data = data;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ProvinceResponseData {

    @Keep
    public ProvinceResponseData() {
    }

    @JsonProperty("province")
    private List<CategoryModel> province;

    public List<CategoryModel> getProvince() {
        return province;
    }

    public void setProvince(List<CategoryModel> province) {
        this.province = province;
    }
}
