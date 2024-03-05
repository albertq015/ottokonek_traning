package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Misc.AddressModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressListResponseModel extends BaseResponseModel {

    private AddressListResponseData data;

    public AddressListResponseData getData() {
        return data;
    }

    public void setData(AddressListResponseData data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class AddressListResponseData {
        @JsonProperty("address")
        private List<AddressModel> address;

        public List<AddressModel> getAddresses() {
            return address;
        }

        public void setAddresses(List<AddressModel> addresses) {
            this.address = addresses;
        }
    }
}