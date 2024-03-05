package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.LocationSearchModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCountryResponseModel extends BaseResponseModel {

    CountryResponseData data;

    public List<LocationSearchModel> getCountry() {
        return data.country;
    }

    public void setCountry(List<LocationSearchModel> country) {
        this.data.country = country;
    }

    public CountryResponseData getData() {
        return data;
    }

    public void setData(CountryResponseData data) {
        this.data = data;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class CountryResponseData {

    List<LocationSearchModel> country;

    public List<LocationSearchModel> getCountry() {
        return country;
    }

    public void setCountry(List<LocationSearchModel> country) {
        this.country = country;
    }
}
