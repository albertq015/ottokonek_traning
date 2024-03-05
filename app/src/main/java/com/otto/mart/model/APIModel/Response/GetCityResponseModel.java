package com.otto.mart.model.APIModel.Response;

import androidx.annotation.Keep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Misc.CategoryModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCityResponseModel extends BaseResponseModel {

    @Keep
    public GetCityResponseModel() {
    }

    @JsonProperty("data")
    private CityResponseData data;

    public List<CategoryModel> getCity() {
        return data.getCity();
    }

    public void setCountry(List<CategoryModel> city) {
        this.data.setCity(city);
    }

    public CityResponseData getData() {
        return data;
    }

    public void setData(CityResponseData data) {
        this.data = data;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class CityResponseData {

    @Keep
    public CityResponseData() {
    }

    @JsonProperty("city")
    private List<CategoryModel> city;

    public List<CategoryModel> getCity() {
        return city;
    }

    public void setCity(List<CategoryModel> city) {
        this.city = city;
    }
}
