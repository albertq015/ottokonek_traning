package com.otto.mart.model.APIModel.Request.olshop;

public class PaymentJurnalRequestModel {
    private int payment_id;

    public PaymentJurnalRequestModel(int payment_id) {
        this.payment_id = payment_id;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }
}
