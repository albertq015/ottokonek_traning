package com.otto.mart.model.APIModel.Response.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankEditResponseModel extends BaseResponseModel {

    @JsonProperty("data")
    private Account data;

    public Account getData() {
        return data;
    }

    public void setData(Account data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Account {
        @JsonProperty("account")
        private BankAccountModel account;

        public BankAccountModel getAccount() {
            return account;
        }

        public void setAccount(BankAccountModel account) {
            this.account = account;
        }
    }
}
