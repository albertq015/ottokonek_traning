package com.otto.mart.model.APIModel.Response.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel;
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountListResponseModel extends BaseResponseModel {

    private Account data;

    public Account getData() {
        return data;
    }

    public void setData(Account data) {
        this.data = data;
    }

    public class Account{
        private List<BankAccountModel> account;

        public List<BankAccountModel> getAccount() {
            return account;
        }

        public void setAccount(List<BankAccountModel> account) {
            this.account = account;
        }
    }
}
