package com.otto.mart.model.APIModel.Request.ottopoint;

public class OpRedeemVoucherDealsRequestModel {

    private String campaign;
    private int jumlah;

    public OpRedeemVoucherDealsRequestModel(String campaign, int jumlah){
        this.campaign = campaign;
        this.jumlah = jumlah;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
