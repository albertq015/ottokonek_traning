package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.LogoProduct;
import com.otto.mart.model.APIModel.Misc.OttoagDenomModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PpobOttoagDenomResponseModel extends BaseResponseModel {

    private OttoagDenomResponseDataModel data;

    public OttoagDenomResponseDataModel getData() {
        return data;
    }

    public void setData(OttoagDenomResponseDataModel data) {
        this.data = data;
    }

    public PpobOttoagDenomResponseModel() {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class OttoagDenomResponseDataModel {
        private List<OttoagDenomModel> denomination;
        private List<OttoagDenomModel> direct;
        private List<OttoagDenomModel> pod;
        private LogoProduct product;

        public List<OttoagDenomModel> getDenomination() {
            return denomination;
        }

        public void setDenomination(List<OttoagDenomModel> denomination) {
            this.denomination = denomination;
        }

        public List<OttoagDenomModel> getDirect() {
            return direct;
        }

        public void setDirect(List<OttoagDenomModel> direct) {
            this.direct = direct;
        }

        public List<OttoagDenomModel> getPod() {
            return pod;
        }

        public void setPod(List<OttoagDenomModel> pod) {
            this.pod = pod;
        }

        public LogoProduct getProduct() {
            return product;
        }

        public void setProduct(LogoProduct product) {
            this.product = product;
        }
    }
}
