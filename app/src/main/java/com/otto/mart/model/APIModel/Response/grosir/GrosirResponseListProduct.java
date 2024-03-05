package com.otto.mart.model.APIModel.Response.grosir;

import java.util.List;

import app.beelabs.com.codebase.base.response.BaseMetaResponse;
import app.beelabs.com.codebase.base.response.BaseResponse;

public class GrosirResponseListProduct extends BaseResponse {


    private BaseMetaResponse meta;
    private List<DataResponseProductItem> data;

    /**
     * Gets meta.
     *
     * @return the meta
     */
    public BaseMetaResponse getMeta() {
        return meta;
    }

    /**
     * Sets meta.
     *
     * @param meta the meta
     */
    public void setMeta(BaseMetaResponse meta) {
        this.meta = meta;
    }

    /**
     * Gets data.
     *
     * @return the data
     */

    public List<DataResponseProductItem> getData() {
        return data;
    }

    public void setData(List<DataResponseProductItem> data) {
        this.data = data;
    }


}
