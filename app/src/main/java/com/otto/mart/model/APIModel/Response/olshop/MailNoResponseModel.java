package com.otto.mart.model.APIModel.Response.olshop;

import com.otto.mart.model.APIModel.Request.olshop.AddressRequestModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

public class MailNoResponseModel extends BaseResponseModel {

    private AddressRequestModel data;

    public AddressRequestModel getData() {
        return data;
    }

    public void setData(AddressRequestModel data) {
        this.data = data;
    }
}
