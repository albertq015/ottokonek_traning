package com.otto.mart.model.APIModel.Response.tokoOttopay;

import com.otto.mart.model.APIModel.Misc.tokoOttopay.Store;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

public class StoreListResponse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private List<Store> suppliers;

        public List<Store> getSuppliers() {
            return suppliers;
        }

        public void setSuppliers(List<Store> suppliers) {
            this.suppliers = suppliers;
        }
    }
}