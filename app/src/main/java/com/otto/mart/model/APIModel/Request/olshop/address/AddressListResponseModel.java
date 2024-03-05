package com.otto.mart.model.APIModel.Request.olshop.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.olshop.ShippingAddressData;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

public class AddressListResponseModel extends BaseResponseModel {

    AddressListData data;

    public AddressListData getData() {
        return data;
    }

    public void setData(AddressListData data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class AddressListData {
        List<ShippingAddressData> shipping_addresses;

        public List<ShippingAddressData> getShipping_addresses() {
            return shipping_addresses;
        }

        public void setShipping_addresses(List<ShippingAddressData> shipping_addresses) {
            this.shipping_addresses = shipping_addresses;
        }
    }
}
