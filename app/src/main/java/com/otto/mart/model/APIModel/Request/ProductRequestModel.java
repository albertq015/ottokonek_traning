package com.otto.mart.model.APIModel.Request;


import com.otto.mart.model.APIModel.Misc.FilterModel;

public class ProductRequestModel {

    int user_id;
    String latitude;
    String longitude;
    FilterModel filter;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public FilterModel getFilter() {
        return filter;
    }

    public void setFilter(FilterModel filter) {
        this.filter = filter;
    }
}
