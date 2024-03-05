package com.otto.mart.model.APIModel.Response.history

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.otto.mart.model.APIModel.Misc.PaymentData
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
class HistoryDetailResponse : BaseResponseModel() {
    var data: PaymentData? = null
}