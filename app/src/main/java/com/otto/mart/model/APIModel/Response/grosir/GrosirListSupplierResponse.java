package com.otto.mart.model.APIModel.Response.grosir;
import java.util.List;
import app.beelabs.com.codebase.base.response.BaseMetaResponse;
import app.beelabs.com.codebase.base.response.BaseResponse;

public class GrosirListSupplierResponse extends BaseResponse {

    private BaseMetaResponse meta;
    private List<DataResponseItemList> data;

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

    public List<DataResponseItemList> getData() {
        return data;
    }

    public void setData(List<DataResponseItemList> data) {
        this.data = data;
    }

    public class DataResponse {

        List<DataResponseItemList> suppliers;

        public List<DataResponseItemList> getSuppliers() {
            return suppliers;
        }

        public void setSuppliers(List<DataResponseItemList> suppliers) {
            this.suppliers = suppliers;
        }

    }

}


