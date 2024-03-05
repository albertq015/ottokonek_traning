package com.otto.mart.model.APIModel.Response.history

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.otto.mart.model.APIModel.Misc.history.OmzetHistory
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
class BankHistoryResponse : BaseResponseModel() {
    var data: BankHistory? = null
    var count = 0
}