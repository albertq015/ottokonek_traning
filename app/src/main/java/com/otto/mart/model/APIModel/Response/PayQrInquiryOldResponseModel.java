package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PayQrInquiryOldResponseModel extends BaseResponseModel {

    PayQrInquiryData data;

    public PayQrInquiryData getData() {
        return data;
    }

    public void setData(PayQrInquiryData data) {
        this.data = data;
    }

    public String getInquiry_data() {
        return data.inquiry_data;
    }

    public void setInquiry_data(String inquiry_data) {
        data.inquiry_data = inquiry_data;
    }

    public List<WidgetBuilderModel> getKey_value_list() {
        return data.key_value_list;
    }

    public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
        data.key_value_list = key_value_list;
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
class PayQrInquiryData {
    String inquiry_data;
    List<WidgetBuilderModel> key_value_list;

    public String getInquiry_data() {
        return inquiry_data;
    }

    public void setInquiry_data(String inquiry_data) {
        this.inquiry_data = inquiry_data;
    }

    public List<WidgetBuilderModel> getKey_value_list() {
        return key_value_list;
    }

    public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
        this.key_value_list = key_value_list;
    }
}
