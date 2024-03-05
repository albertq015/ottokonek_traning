package com.otto.mart.model.APIModel.Response.refund;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantRefundResponse extends BaseResponseModel {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Data {
        String rc;
        String msg;
        List<WidgetBuilderModel> key_value_list;

        public String getRc() {
            return rc;
        }

        public void setRc(String rc) {
            this.rc = rc;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<WidgetBuilderModel> getKey_value_list() {
            return key_value_list;
        }

        public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
            this.key_value_list = key_value_list;
        }
    }
}