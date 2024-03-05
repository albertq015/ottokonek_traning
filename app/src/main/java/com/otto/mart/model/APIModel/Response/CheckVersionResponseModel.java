package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

public class CheckVersionResponseModel extends BaseResponseModel {

    private CheckVersionResponseData data;

    public CheckVersionResponseData getData() {
        return data;
    }

    public void setData(CheckVersionResponseData data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class CheckVersionResponseData {
        private String applicationId;
        private int version_api;
        private int version_app;
        private boolean force_update;

        public int getVersion_api() {
            return version_api;
        }

        public void setVersion_api(int version_api) {
            this.version_api = version_api;
        }

        public int getVersion_app() {
            return version_app;
        }

        public void setVersion_app(int version_app) {
            this.version_app = version_app;
        }

        public boolean isForce_update() {
            return force_update;
        }

        public void setForce_update(boolean force_update) {
            this.force_update = force_update;
        }

        public String getApplicationId() {
            return applicationId;
        }

        public void setApplicationId(String applicationId) {
            this.applicationId = applicationId;
        }
    }
}
