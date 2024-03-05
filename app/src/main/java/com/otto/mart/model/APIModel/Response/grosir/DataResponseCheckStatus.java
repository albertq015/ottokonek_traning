package com.otto.mart.model.APIModel.Response.grosir;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import app.beelabs.com.codebase.base.response.BaseMetaResponse;
import app.beelabs.com.codebase.base.response.BaseResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataResponseCheckStatus extends BaseResponse {
    private BaseMetaResponse meta;
    private List<DataCheckStatus> data;

    public BaseMetaResponse getMeta() {
        return meta;
    }

    public void setMeta(BaseMetaResponse meta) {
        this.meta = meta;
    }

    public List<DataCheckStatus> getData() {
        return data;
    }

    public void setData(List<DataCheckStatus> data) {
        this.data = data;
    }
}
