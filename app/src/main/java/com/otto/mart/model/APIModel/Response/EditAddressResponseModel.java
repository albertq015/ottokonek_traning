package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.AddressModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EditAddressResponseModel extends BaseResponseModel{

    @JsonIgnoreProperties("data")
    EditAddressResponseData data;

    public EditAddressResponseData getData() {
        return data;
    }

    public void setData(EditAddressResponseData data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class EditAddressResponseData{
        @JsonIgnoreProperties("address")
        private AddressModel address;

        public AddressModel getAddress() {
            return address;
        }

        public void setAddress(AddressModel address) {
            this.address = address;
        }
    }
}
