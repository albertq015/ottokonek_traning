package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.AirProductModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpobAirProductlistResponseModel extends BaseResponseModel {

    List<AirProductModel> data;

    public List<AirProductModel> getData() {
        return data;
    }

    public void setData(List<AirProductModel> data) {
        this.data = data;
    }

    public PpobAirProductlistResponseModel() {
    }
}


