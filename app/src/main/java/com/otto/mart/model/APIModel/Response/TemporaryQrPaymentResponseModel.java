package com.otto.mart.model.APIModel.Response;

import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

public class TemporaryQrPaymentResponseModel extends BaseResponseModel {
    List<WidgetBuilderModel> data;

    public List<WidgetBuilderModel> getData() {
        return data;
    }

    public void setData(List<WidgetBuilderModel> data) {
        this.data = data;
    }
}
