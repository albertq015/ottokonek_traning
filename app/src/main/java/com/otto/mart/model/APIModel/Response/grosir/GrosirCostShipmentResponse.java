package com.otto.mart.model.APIModel.Response.grosir;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

import app.beelabs.com.codebase.base.response.BaseMetaResponse;
import app.beelabs.com.codebase.base.response.BaseResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GrosirCostShipmentResponse extends BaseResponse {
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class DataResponse{
        private ArrayList<ServiceCostShipment> services;

        public ArrayList<ServiceCostShipment> getServices() {
            return services;
        }

        public void setServices(ArrayList<ServiceCostShipment> services) {
            this.services = services;
        }
    }
}
