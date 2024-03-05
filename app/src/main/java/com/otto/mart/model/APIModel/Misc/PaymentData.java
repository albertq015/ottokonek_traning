package com.otto.mart.model.APIModel.Misc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentData {

    @JsonProperty("key_value_list")
    private List<WidgetBuilderModel> keyValueList;

    private String uimsg;

    public List<WidgetBuilderModel> getKeyValueList() {
        return keyValueList;
    }

    public void setKeyValueList(List<WidgetBuilderModel> keyValueList) {
        this.keyValueList = keyValueList;
    }

    public String getUimsg() {
        return uimsg;
    }

    public void setUimsg(String uimsg) {
        this.uimsg = uimsg;
    }
}