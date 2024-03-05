package com.otto.mart.model.APIModel.Response;

import androidx.annotation.Keep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferBankInquiryResponseModel extends BaseResponseModel {

    @Keep
    public TransferBankInquiryResponseModel() {
    }

    @JsonProperty("data")
    private TransferBankInquiryResponseData data;

    public TransferBankInquiryResponseData getData() {
        return data;
    }

    public void setData(TransferBankInquiryResponseData data) {
        this.data = data;
    }


    public String getRecieverName() {
        return data.getReceiverName();
    }
    
    public String getFee() {
        return data.getFee();
    }


}

@JsonIgnoreProperties(ignoreUnknown = true)
class TransferBankInquiryResponseData {

    @Keep
    public TransferBankInquiryResponseData() {
    }

    @JsonProperty("receiverName")
    private String receiverName;

    @JsonProperty("fee")
    private String fee;

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
