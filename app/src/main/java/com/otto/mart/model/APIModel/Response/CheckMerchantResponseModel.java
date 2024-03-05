package com.otto.mart.model.APIModel.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckMerchantResponseModel extends BaseResponseModel {

    private Merchant data;

    public Merchant getData() {
        return data;
    }

    public void setData(Merchant data) {
        this.data = data;
    }

    public class Merchant{
        private CheckMerchantResponseData merchant;

        public CheckMerchantResponseData getMerchant() {
            return merchant;
        }

        public void setMerchant(CheckMerchantResponseData merchant) {
            this.merchant = merchant;
        }
    }
}
