package com.otto.mart.model.APIModel.Response.olshop.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.olshop.ShippingAddressData;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateAddressResponseModel extends BaseResponseModel {
    ShippingAddressData data;

    public ShippingAddressData getData() {
        return data;
    }

    public void setData(ShippingAddressData data) {
        this.data = data;
    }
}
