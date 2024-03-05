package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TelkomInquiryResponseModel extends BaseResponseModel {

    private TelkomInquiryData data;

    public TelkomInquiryData getData() {
        return data;
    }

    public void setData(TelkomInquiryData data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class TelkomInquiryData{
        private TelkomInquiryResponseData inquiry_data;
        private List<WidgetBuilderModel> key_value_list;
        private String product_logo;

        public String getProduct_logo() {
            return product_logo;
        }

        public void setProduct_logo(String product_logo) {
            this.product_logo = product_logo;
        }

        public List<WidgetBuilderModel> getKey_value_list() {
            return key_value_list;
        }

        public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
            this.key_value_list = key_value_list;
        }

        public TelkomInquiryResponseData getInquiry_data() {
            return inquiry_data;
        }

        public void setInquiry_data(TelkomInquiryResponseData inquiry_data) {
            this.inquiry_data = inquiry_data;
        }
    }
}
