package com.otto.mart.model.APIModel.Request.grosir;

public class AddressCreateUpdateDeleteRequest {
    private String name;
    private String detail;
    private String zip_code;
    private long district_id;
    private boolean is_primary;
    private long id;
    private String municipality_code;
    private boolean is_favourite;

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public boolean isIs_primary() {
        return is_primary;
    }

    public void setIs_primary(boolean is_primary) {
        this.is_primary = is_primary;
    }

    public String getMunicipality_code() {
        return municipality_code;
    }

    public void setMunicipality_code(String municipality_code) {
        this.municipality_code = municipality_code;
    }

    public boolean isIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(boolean is_favourite) {
        this.is_favourite = is_favourite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }



    public long getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(long district_id) {
        this.district_id = district_id;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
