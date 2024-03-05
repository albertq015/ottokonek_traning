package com.otto.mart.model.APIModel.Response.grosir;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import app.beelabs.com.codebase.base.response.BaseMetaResponse;
import app.beelabs.com.codebase.base.response.BaseResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GrosirRegisterSupplierResponse extends BaseResponse {
    private BaseMetaResponse meta;
    private DataResponse data;

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
    public DataResponse getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(DataResponse data) {
        this.data = data;
    }

    /**
     * The type Data response.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class DataResponse {

    }
}
