package com.otto.mart.model.APIModel.Response.storeLocation;

import com.otto.mart.model.APIModel.Response.BaseModel.RoseBaseResponseModel;

import java.util.List;

public class BarangayResponse extends RoseBaseResponseModel {
    private List<Data> data;
    private String msg;
    private String rc;
    private int total;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class Data {
        private String code;
        private String createdAt;
        private int id;
        private String municipalityCode;
        private String name;
        private String updatedAt;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMunicipalityCode() {
            return municipalityCode;
        }

        public void setMunicipalityCode(String municipalityCode) {
            this.municipalityCode = municipalityCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
