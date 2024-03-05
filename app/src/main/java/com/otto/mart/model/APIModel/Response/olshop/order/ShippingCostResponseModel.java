package com.otto.mart.model.APIModel.Response.olshop.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

public class ShippingCostResponseModel extends BaseResponseModel {

    private ShippingResponseData data;

    public ShippingResponseData getData() {
        return data;
    }

    public void setData(ShippingResponseData data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ShippingResponseData{
        private List<ShippingItem> shipping_cost;

        public List<ShippingItem> getShipping_cost() {
            return shipping_cost;
        }

        public void setShipping_cost(List<ShippingItem> shipping_cost) {
            this.shipping_cost = shipping_cost;
        }
    }
}
