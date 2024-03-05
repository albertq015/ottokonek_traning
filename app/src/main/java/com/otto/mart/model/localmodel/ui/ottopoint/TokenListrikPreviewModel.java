package com.otto.mart.model.localmodel.ui.ottopoint;

public class TokenListrikPreviewModel {

    private String label = "";
    private String value = "";

    public TokenListrikPreviewModel(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
