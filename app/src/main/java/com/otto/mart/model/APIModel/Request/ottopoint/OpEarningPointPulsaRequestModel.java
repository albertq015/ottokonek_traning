package com.otto.mart.model.APIModel.Request.ottopoint;

public class OpEarningPointPulsaRequestModel {

    private String rc = "00";
    private String rule;

    public OpEarningPointPulsaRequestModel(String rule){
        this.rule = rule;
    }

    public OpEarningPointPulsaRequestModel(String rule, String rc){
        this.rule = rule;
        this.rc = rc;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }
}