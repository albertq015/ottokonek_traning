package com.otto.mart.model.APIModel.Request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.WalletType;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletTypeResponseModel extends BaseResponseModel {

    private WalletTypeData data;

    public WalletTypeData getData() {
        return data;
    }

    public void setData(WalletTypeData data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class WalletTypeData{
        private List<WalletType> wallet_types;

        public List<WalletType> getWallet_types() {
            return wallet_types;
        }

        public void setWallet_types(List<WalletType> wallet_types) {
            this.wallet_types = wallet_types;
        }
    }
}
