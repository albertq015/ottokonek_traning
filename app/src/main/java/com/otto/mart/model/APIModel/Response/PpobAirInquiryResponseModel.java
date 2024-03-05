package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.AirInquiryModel;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpobAirInquiryResponseModel extends BaseResponseModel {

    PpobAirDataModel data;

    public PpobAirInquiryResponseModel() {
    }

    public PpobAirDataModel getData() {
        return data;
    }

    public void setData(PpobAirDataModel data) {
        this.data = data;
    }


    public AirInquiryModel getInquiry_data() {
        return data.inquiry_data;
    }

    public void setInquiry_data(AirInquiryModel inquiry_data) {
        data.inquiry_data = inquiry_data;
    }

    public List<WidgetBuilderModel> getKey_value_list() {
        return data.key_value_list;
    }

    public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
        data.key_value_list = key_value_list;
    }

    public String getProductLogo(){
        return getData().getProduct_logo();
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class PpobAirDataModel {
    AirInquiryModel inquiry_data;
    List<WidgetBuilderModel> key_value_list;
    String product_logo;

    public PpobAirDataModel() {
    }

    public String getProduct_logo() {
        return product_logo;
    }

    public void setProduct_logo(String product_logo) {
        this.product_logo = product_logo;
    }

    public AirInquiryModel getInquiry_data() {
        return inquiry_data;
    }

    public void setInquiry_data(AirInquiryModel inquiry_data) {
        this.inquiry_data = inquiry_data;
    }

    public List<WidgetBuilderModel> getKey_value_list() {
        return key_value_list;
    }

    public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
        this.key_value_list = key_value_list;
    }
}

