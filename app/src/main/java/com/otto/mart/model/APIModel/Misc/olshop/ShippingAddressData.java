package com.otto.mart.model.APIModel.Misc.olshop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingAddressData implements Serializable {
    private boolean is_favourite;
    private boolean is_primary;
    private long latitude;
    private long longitude;
    private String mid;
    private String name;
    private long district_id;
    private String detail;
    private int zip_code;
    private int id;
    private int user_id;
    private IdNameModel municipality;
        private IdNameModel province;
        private IdNameModel region;
    private IdNameModel city;
    private IdNameModel district;

    public IdNameModel getCity() {
        return city;
    }

    public void setCity(IdNameModel city) {
        this.city = city;
    }

    public IdNameModel getDistrict() {
        return district;
    }

    public void setDistrict(IdNameModel district) {
        this.district = district;
    }

    public boolean isIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(boolean is_favourite) {
        this.is_favourite = is_favourite;
    }

    public boolean isIs_primary() {
        return is_primary;
    }

    public void setIs_primary(boolean is_primary) {
        this.is_primary = is_primary;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(long district_id) {
        this.district_id = district_id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getZip_code() {
        return zip_code;
    }

    public void setZip_code(int zip_code) {
        this.zip_code = zip_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public IdNameModel getMunicipality() {
        return municipality;
    }

    public void setMunicipality(IdNameModel municipality) {
        this.municipality = municipality;
    }

    public IdNameModel getProvince() {
        return province;
    }

    public void setProvince(IdNameModel province) {
        this.province = province;
    }

    public IdNameModel getRegion() {
        return region;
    }

    public void setRegion(IdNameModel region) {
        this.region = region;
    }
}
