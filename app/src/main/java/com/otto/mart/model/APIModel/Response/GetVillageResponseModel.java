package com.otto.mart.model.APIModel.Response;

import androidx.annotation.Keep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Misc.CategoryModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetVillageResponseModel extends BaseResponseModel{

    @Keep
    public GetVillageResponseModel() {
    }

    @JsonProperty("data")
    private VillageResponseData data;

    public List<CategoryModel> getVillage() {
        return data.getVillage();
    }

    public void setVillage(List<CategoryModel> village) {
        this.data.setVillage(village);
    }

    public VillageResponseData getData() {
        return data;
    }

    public void setData(VillageResponseData data) {
        this.data = data;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class VillageResponseData {

    @Keep
    public VillageResponseData() {
    }

    @JsonProperty("village")
    private List<CategoryModel> village;

    public List<CategoryModel> getVillage() {
        return village;
    }

    public void setVillage(List<CategoryModel> village) {
        this.village = village;
    }
}
