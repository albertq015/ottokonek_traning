package com.otto.mart.model.APIModel.Request.ppob;

public class PpobDenomRequest {

    String prefix;
    String keyword = "";


    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
