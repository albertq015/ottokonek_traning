package com.otto.mart.model.APIModel.Response.bank

import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

class BankAccountListDepositResponse : BaseResponseModel() {
    var data: MutableList<BankAccountModel>? = null
}