package com.otto.mart.model.APIModel.Response.olshop.confirm;

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

public class FinalConfirmOrderResponse extends BaseResponseModel {
    FinalConfirmOrder data;

    public FinalConfirmOrder getData() {
        return data;
    }

    public void setData(FinalConfirmOrder data) {
        this.data = data;
    }
}
