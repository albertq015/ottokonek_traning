package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.LogoProduct;
import com.otto.mart.model.APIModel.Misc.OttoagDenomModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpobAirDenomResponseModel extends BaseResponseModel {

    private PulsaDenom data;

    public PulsaDenom getData() {
        return data;
    }

    public void setData(PulsaDenom data) {
        this.data = data;
    }

    public PpobAirDenomResponseModel() {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class PulsaDenom{
        private List<OttoagDenomModel> denomination;
        private LogoProduct product;

        public List<OttoagDenomModel> getDenomination() {
            return denomination;
        }

        public void setDenomination(List<OttoagDenomModel> denomination) {
            this.denomination = denomination;
        }

        public LogoProduct getProduct() {
            return product;
        }

        public void setProduct(LogoProduct product) {
            this.product = product;
        }
    }
}
