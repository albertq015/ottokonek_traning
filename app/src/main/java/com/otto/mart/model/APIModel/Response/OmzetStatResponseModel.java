package com.otto.mart.model.APIModel.Response;

import androidx.annotation.Keep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OmzetStatResponseModel extends BaseResponseModel {

    @Keep
    public OmzetStatResponseModel() {
    }

    @JsonProperty("data")
    private OmzetStatData data;

    public OmzetStatData getData() {
        return data;
    }

    public void setData(OmzetStatData data) {
        this.data = data;
    }

    public int getDaily_omset() {
        return data.getDaily_omset();
    }

    public void setDaily_omset(int daily_omset) {
        data.setDaily_omset(daily_omset);
    }

    public String getAll_omset() {
        return data.getAll_omset();
    }

    public void setAll_omset(String all_omset) {
        data.setAll_omset(all_omset);
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class OmzetStatData {

    @Keep
    public OmzetStatData(){

    }

    @JsonProperty("daily_omset")
    private int daily_omset;
    @JsonProperty("all_omset")
    private String all_omset;

    public int getDaily_omset() {
        return daily_omset;
    }

    public void setDaily_omset(int daily_omset) {
        this.daily_omset = daily_omset;
    }

    public String getAll_omset() {
        return all_omset;
    }

    public void setAll_omset(String all_omset) {
        this.all_omset = all_omset;
    }
}