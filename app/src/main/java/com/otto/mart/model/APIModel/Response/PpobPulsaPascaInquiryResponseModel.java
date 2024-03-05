package com.otto.mart.model.APIModel.Response;

import com.otto.mart.model.APIModel.Misc.PulsaPascaInquiryModel;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

public class PpobPulsaPascaInquiryResponseModel extends BaseResponseModel {
    PpobPulsaPascaInquiryDataModel data;

    public PpobPulsaPascaInquiryDataModel getData() {
        return data;
    }

    public void setData(PpobPulsaPascaInquiryDataModel data) {
        this.data = data;
    }

    public PulsaPascaInquiryModel getInquiry_data() {
        return data.inquiry_data;
    }

    public void setInquiry_data(PulsaPascaInquiryModel inquiry_data) {
        this.data.inquiry_data = inquiry_data;
    }

    public List<WidgetBuilderModel> getKey_value_list() {
        return data.key_value_list;
    }

    public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
        this.data.key_value_list = key_value_list;
    }

    public String getProductLogo(){
        return getData().getProduct_logo();
    }
}

class PpobPulsaPascaInquiryDataModel {
    PulsaPascaInquiryModel inquiry_data;
    List<WidgetBuilderModel> key_value_list;
    String product_logo;

    public String getProduct_logo() {
        return product_logo;
    }

    public void setProduct_logo(String product_logo) {
        this.product_logo = product_logo;
    }

    public PulsaPascaInquiryModel getInquiry_data() {
        return inquiry_data;
    }

    public void setInquiry_data(PulsaPascaInquiryModel inquiry_data) {
        this.inquiry_data = inquiry_data;
    }

    public List<WidgetBuilderModel> getKey_value_list() {
        return key_value_list;
    }

    public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
        this.key_value_list = key_value_list;
    }
}
