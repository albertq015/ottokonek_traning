package com.otto.mart.model.APIModel.Response.ottopoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpEarningPointPulsaResponseModel extends BaseResponseOttopoint {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data{

        private long points;

        public long getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }
    }
}
