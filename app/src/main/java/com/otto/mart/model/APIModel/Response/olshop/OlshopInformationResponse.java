package com.otto.mart.model.APIModel.Response.olshop;

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

public class OlshopInformationResponse extends BaseResponseModel {

    private OlshopInformationData data;

    public OlshopInformationData getData() {
        return data;
    }

    public void setData(OlshopInformationData data) {
        this.data = data;
    }

    public class OlshopInformationData {
        private int id;
        private String title;
        private String status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            if (status == null) this.status = "Active";
            else if (status.isEmpty()) this.status = "Active";
            else this.status = status;
        }
    }
}
