package com.otto.mart.model.APIModel.Request;

public class PpobListrikInquiryRequestModel extends PpobListrikDenomRequestModel {
    int denomination;

    public int getDenomination() {
        return denomination;
    }

    public void setDenomination(int denomination) {
        this.denomination = denomination;
    }
}
