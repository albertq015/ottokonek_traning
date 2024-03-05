package com.otto.mart.model.APIModel.Request.olshop.address;

import com.otto.mart.model.APIModel.Misc.olshop.ShippingAddressData;

import java.io.Serializable;

public class ShippingAddressRequestModel implements Serializable {
    ShippingAddressData shipping_address;

    public ShippingAddressData getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(ShippingAddressData shipping_address) {
        this.shipping_address = shipping_address;
    }
}
