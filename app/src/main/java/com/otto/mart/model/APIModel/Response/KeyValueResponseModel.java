package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyValueResponseModel extends BaseResponseModel {
    KeyValueListData data;

    public KeyValueListData getData() {
        return data;
    }

    public void setData(KeyValueListData data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class KeyValueListData {
        List<WidgetBuilderModel> key_value_list;

        public List<WidgetBuilderModel> getKey_value_list() {
            return key_value_list;
        }

        public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
            this.key_value_list = key_value_list;
        }
    }
}
