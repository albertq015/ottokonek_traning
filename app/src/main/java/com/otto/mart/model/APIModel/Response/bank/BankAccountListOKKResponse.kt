package com.otto.mart.model.APIModel.Response.bank

import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

data class BankAccountListOKKResponse(
        var data: BankAccountListOKKData? = null
) : BaseResponseModel()