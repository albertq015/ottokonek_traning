package com.otto.mart.model.APIModel.Request;

public class FaqTopUpRequest {
    boolean ottopay_instruction = false;

    public boolean isOttopay_instruction() {
        return ottopay_instruction;
    }

    public void setOttopay_instruction(boolean ottopay_instruction) {
        this.ottopay_instruction = ottopay_instruction;
    }
}