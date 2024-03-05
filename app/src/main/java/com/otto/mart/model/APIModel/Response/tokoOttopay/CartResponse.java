package com.otto.mart.model.APIModel.Response.tokoOttopay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.tokoOttopay.Supplier;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CartResponse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Data {

        private List<Supplier> cart;

        public List<Supplier> getCart() {
            return cart;
        }

        public void setCart(List<Supplier> cart) {
            this.cart = cart;
        }
    }
}