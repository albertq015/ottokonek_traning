package com.otto.mart.model.APIModel.Response;

import androidx.annotation.Keep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppClonerListResponse extends BaseResponseModel {

    @Keep
    public AppClonerListResponse() {
    }

    @JsonProperty("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private List<Packages> packages;

        public List<Packages> getPackages() {
            return packages;
        }

        public void setPackages(List<Packages> packages) {
            this.packages = packages;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Packages {

            @Keep
            public Packages() {
            }

            @JsonProperty("id")
            private int id;

            @JsonProperty("app_name")
            private String app_name;

            @JsonProperty("package_name")
            private String package_name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getApp_name() {
                return app_name;
            }

            public void setApp_name(String app_name) {
                this.app_name = app_name;
            }

            public String getPackage_name() {
                return package_name;
            }

            public void setPackage_name(String package_name) {
                this.package_name = package_name;
            }
        }
    }
}
