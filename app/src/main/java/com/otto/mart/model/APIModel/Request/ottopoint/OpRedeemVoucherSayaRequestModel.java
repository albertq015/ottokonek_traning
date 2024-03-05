package com.otto.mart.model.APIModel.Request.ottopoint;

public class OpRedeemVoucherSayaRequestModel {

    private String category;
    private String cust_id;
    private String cust_id2 = "";
    private String campaignId;
    private String product_code;

    public OpRedeemVoucherSayaRequestModel(String category, String cust_id, String cust_id2, String campaignId, String product_code){
        this.category = category;
        this.cust_id = cust_id;
        this.cust_id2 = cust_id2;
        this.campaignId = campaignId;
        this.product_code = product_code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getCust_id2() {
        return cust_id2;
    }

    public void setCust_id2(String cust_id2) {
        this.cust_id2 = cust_id2;
    }
}
