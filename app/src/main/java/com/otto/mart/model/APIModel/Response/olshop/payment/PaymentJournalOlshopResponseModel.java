package com.otto.mart.model.APIModel.Response.olshop.payment;

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

public class PaymentJournalOlshopResponseModel extends BaseResponseModel {
    List<PaymentJournal> data;

    public List<PaymentJournal> getData() {
        return data;
    }

    public void setData(List<PaymentJournal> data) {
        this.data = data;
    }
}
