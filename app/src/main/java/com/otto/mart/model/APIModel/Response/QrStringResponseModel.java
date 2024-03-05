package com.otto.mart.model.APIModel.Response;

import androidx.annotation.Keep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QrStringResponseModel extends BaseResponseModel {

    @Keep
    public QrStringResponseModel() {
    }

    @JsonProperty("data")
    private QrStringDataModel data;

    public QrStringDataModel getData() {
        return data;
    }

    public void setData(QrStringDataModel data) {
        this.data = data;
    }

    public String getQr_string() {
        return data.getQr_string();
    }

    public void setQr_string(String qr_string) {
        data.setQr_string(qr_string);
    }

    public String getRReferenceLabel() {
        return data.getReferenceLabel();
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class QrStringDataModel {

    @Keep
    public QrStringDataModel() {
    }

    @JsonProperty("qrString")
    private String qr_string;
    private String referenceLabel;

    public String getQr_string() {
        return qr_string;
    }

    public void setQr_string(String qr_string) {
        this.qr_string = qr_string;
    }

    public String getReferenceLabel() {
        return referenceLabel;
    }

    public void setReferenceLabel(String referenceLabel) {
        this.referenceLabel = referenceLabel;
    }
}