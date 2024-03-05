package com.otto.mart.model.APIModel.Response;

import androidx.annotation.Keep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Misc.bank.BankListingModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetBankResponseModel extends BaseResponseModel {

    @Keep
    public GetBankResponseModel() {
    }

    @JsonProperty("data")
    private BankDataModel data;

    public BankDataModel getData() {
        return data;
    }

    public void setData(BankDataModel data) {
        this.data = data;
    }

    public List<BankListingModel> getBanks() {
        return data.getBanks();
    }

    public void setBanks(List<BankListingModel> banks) {
        data.setBanks(banks);
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
class BankDataModel {

    @Keep
    public BankDataModel() {
    }

    @JsonProperty("banks")
    private List<BankListingModel> banks;

    public List<BankListingModel> getBanks() {
        return banks;
    }

    public void setBanks(List<BankListingModel> banks) {
        this.banks = banks;
    }
}
