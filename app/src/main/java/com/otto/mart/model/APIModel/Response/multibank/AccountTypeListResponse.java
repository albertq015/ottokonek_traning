package com.otto.mart.model.APIModel.Response.multibank;

import com.otto.mart.model.APIModel.Response.BaseModel.RoseBaseResponseModel;

import java.util.List;

public class AccountTypeListResponse extends RoseBaseResponseModel {


    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    private List<Data> data;

    public static class Data{
        private String code;
        private String id;
        private String name;


        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
