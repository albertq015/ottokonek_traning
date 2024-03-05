package com.otto.mart.model.APIModel.Response.tokoOttopay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.tokoOttopay.OrderHistory;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderHistoryResponse extends BaseResponseModel {

    private List<OrderHistory> data;

    public List<OrderHistory> getData() {
        return data;
    }

    public void setData(List<OrderHistory> data) {
        this.data = data;
    }
}
