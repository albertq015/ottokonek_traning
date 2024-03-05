package com.otto.mart.model.APIModel.Response.grosir;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.olshop.ShippingAddressData;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class GrosirAddressListResponse extends BaseResponseModel {

    private DataResponse data;

    public DataResponse getData() {
        return data;
    }

    public void setData(DataResponse data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class DataResponse{
        private List<ShippingAddressData> shipping_addresses;

        public List<ShippingAddressData> getShipping_addresses() {
            return shipping_addresses;
        }

        public void setShipping_addresses(List<ShippingAddressData> shipping_addresses) {
            this.shipping_addresses = shipping_addresses;
        }
    }
}
