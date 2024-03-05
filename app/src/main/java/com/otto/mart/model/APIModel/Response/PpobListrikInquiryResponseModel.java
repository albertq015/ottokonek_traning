package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Misc.PLNInquiryData;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpobListrikInquiryResponseModel extends BaseResponseModel {
    @JsonProperty("data")
    PpobListriInquiryData data;

    public PpobListrikInquiryResponseModel() {
    }

    public PLNInquiryData getInquiry_data() {
        return data.inquiry_data;
    }

    public void setInquiry_data(PLNInquiryData inquiry_data) {
        data.inquiry_data = inquiry_data;
    }

    public List<WidgetBuilderModel> getKey_value_List() {
        return data.key_value_list;
    }

    public void setKey_value_List(List<WidgetBuilderModel> key_value_List) {
        data.key_value_list = key_value_List;
    }

    public PpobListriInquiryData getData() {
        return data;
    }

    public void setData(PpobListriInquiryData data) {
        this.data = data;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class PpobListriInquiryData {

    public PpobListriInquiryData() {
    }

    @JsonProperty("inquiry_data")
    PLNInquiryData inquiry_data;
    @JsonProperty("key_value_list")
    List<WidgetBuilderModel> key_value_list;
    String product_logo;

    public String getProduct_logo() {
        return product_logo;
    }

    public void setProduct_logo(String product_logo) {
        this.product_logo = product_logo;
    }

    public PLNInquiryData getInquiry_data() {
        return inquiry_data;
    }

    public void setInquiry_data(PLNInquiryData inquiry_data) {
        this.inquiry_data = inquiry_data;
    }

    public List<WidgetBuilderModel> getKey_value_List() {
        return key_value_list;
    }

    public void setKey_value_List(List<WidgetBuilderModel> key_value_List) {
        this.key_value_list = key_value_List;
    }
}
