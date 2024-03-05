package com.otto.mart.model.APIModel.Misc;

import java.util.List;

public class ListrikConfirmationModel {
    private List<WidgetBuilderModel> keyValueList;
    private String productName;

    public ListrikConfirmationModel(List<WidgetBuilderModel> keyValueList, String productName) {
        this.keyValueList = keyValueList;
        this.productName = productName;
    }

    public ListrikConfirmationModel() {
    }

    public List<WidgetBuilderModel> getKeyValueList() {
        return keyValueList;
    }

    public void setKeyValueList(List<WidgetBuilderModel> keyValueList) {
        this.keyValueList = keyValueList;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
