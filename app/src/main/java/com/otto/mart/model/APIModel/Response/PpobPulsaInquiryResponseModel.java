package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.PulsaInquiryModel;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpobPulsaInquiryResponseModel extends BaseResponseModel {
    PpobPulsaDataModel data;

    public PpobPulsaInquiryResponseModel() {
    }

    public PpobPulsaDataModel getData() {
        return data;
    }

    public void setData(PpobPulsaDataModel data) {
        this.data = data;
    }

    public PulsaInquiryModel getInquiry_data() {
        return data.inquiry_data;
    }

    public void setInquiry_data(PulsaInquiryModel inquiry_data) {
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
class PpobPulsaDataModel {
    PulsaInquiryModel inquiry_data;
    List<WidgetBuilderModel> key_value_list;
    String product_logo;

    public PpobPulsaDataModel() {
    }

    public String getProduct_logo() {
        return product_logo;
    }

    public void setProduct_logo(String product_logo) {
        this.product_logo = product_logo;
    }

    public PulsaInquiryModel getInquiry_data() {
        return inquiry_data;
    }

    public void setInquiry_data(PulsaInquiryModel inquiry_data) {
        this.inquiry_data = inquiry_data;
    }

    public List<WidgetBuilderModel> getKey_value_list() {
        return key_value_list;
    }

    public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
        this.key_value_list = key_value_list;
    }
}

