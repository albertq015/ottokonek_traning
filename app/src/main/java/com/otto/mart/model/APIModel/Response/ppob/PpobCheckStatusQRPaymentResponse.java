package com.otto.mart.model.APIModel.Response.ppob;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpobCheckStatusQRPaymentResponse extends BaseResponseModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

        private String uimsg;
        private List<WidgetBuilderModel> key_value_list;

        public String getUimsg() {
            return uimsg;
        }

        public void setUimsg(String uimsg) {
            this.uimsg = uimsg;
        }

        public List<WidgetBuilderModel> getKey_value_list() {
            return key_value_list;
        }

        public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
            this.key_value_list = key_value_list;
        }
    }
}