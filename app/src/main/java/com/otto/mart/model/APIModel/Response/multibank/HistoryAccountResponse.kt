package com.otto.mart.model.APIModel.Response.multibank

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.history.BankHistory


@JsonIgnoreProperties(ignoreUnknown = true)
class HistoryAccountResponse : BaseResponseModel() {
    var data: HistoryAccountListResponse? = null
    var count = 0
}