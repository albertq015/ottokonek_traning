package com.otto.mart.model.APIModel.Request;

public class UpdatePinRequestModel {
    private String old_pin;
    private String new_pin;

    public String getOld_pin() {
        return old_pin;
    }

    public void setOld_pin(String old_pin) {
        this.old_pin = old_pin;
    }

    public String getNew_pin() {
        return new_pin;
    }

    public void setNew_pin(String new_pin) {
        this.new_pin = new_pin;
    }
}
