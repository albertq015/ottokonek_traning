package com.otto.mart.model.APIModel.Response.grosir;

import java.util.ArrayList;

import app.beelabs.com.codebase.base.response.BaseMetaResponse;
import app.beelabs.com.codebase.base.response.BaseResponse;

public class GrosirCheckSupplierRespond extends BaseResponse {
    private BaseMetaResponse meta;
    private DataResponse data;

    public BaseMetaResponse getMeta() {
        return meta;
    }

    public void setMeta(BaseMetaResponse meta) {
        this.meta = meta;
    }

    public DataResponse getData() {
        return data;
    }

    public void setData(DataResponse data) {
        this.data = data;
    }

    public class DataResponse{
        private boolean registered;
        private ArrayList<String> doc_requirement;
        private ArrayList<String> tech_requirement;
        private boolean first_order;

        public boolean isRegistered() {
            return registered;
        }

        public void setRegistered(boolean registered) {
            this.registered = registered;
        }

        public ArrayList<String> getDoc_requirement() {
            return doc_requirement;
        }

        public void setDoc_requirement(ArrayList<String> doc_requirement) {
            this.doc_requirement = doc_requirement;
        }

        public ArrayList<String> getTech_requirement() {
            return tech_requirement;
        }

        public void setTech_requirement(ArrayList<String> tech_requirement) {
            this.tech_requirement = tech_requirement;
        }

        public boolean isFirst_order() {
            return first_order;
        }

        public void setFirst_order(boolean first_order) {
            this.first_order = first_order;
        }
    }
}
