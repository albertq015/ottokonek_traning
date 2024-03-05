package com.otto.mart.model.localmodel.ui.ottopoint;

import java.util.List;

public class TokenListrikDataModel {

    private List<TokenListrikPreviewModel> items;

    public TokenListrikDataModel(List<TokenListrikPreviewModel> items) {
        this.items = items;
    }

    public List<TokenListrikPreviewModel> getItems() {
        return items;
    }

    public void setItems(List<TokenListrikPreviewModel> items) {
        this.items = items;
    }
}
