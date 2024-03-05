package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.DenominationModel;
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpobListrikDenomResponseModel extends BaseResponseModel {

    PpobListrikDenomData data;

    public PpobListrikDenomResponseModel() {
    }

    public PpobListrikDenomData getData() {
        return data;
    }

    public void setData(PpobListrikDenomData data) {
        this.data = data;
    }


    public List<WidgetBuilderModel> getKey_value_list() {
        return data.key_value_list;
    }

    public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
        data.key_value_list = key_value_list;
    }

    public List<DenominationModel> getDenomination() {
        return data.denomination;
    }

    public void setDenomination(List<DenominationModel> denomination) {
        data.denomination = denomination;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class PpobListrikDenomData {
    List<WidgetBuilderModel> key_value_list;
    List<DenominationModel> denomination;

    public List<WidgetBuilderModel> getKey_value_list() {
        return key_value_list;
    }

    public void setKey_value_list(List<WidgetBuilderModel> key_value_list) {
        this.key_value_list = key_value_list;
    }

    public List<DenominationModel> getDenomination() {
        return denomination;
    }

    public void setDenomination(List<DenominationModel> denomination) {
        this.denomination = denomination;
    }
}
