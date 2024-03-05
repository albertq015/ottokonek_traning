package com.otto.mart.model.APIModel.Response.multibank

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.otto.mart.model.APIModel.Misc.PaymentData
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
class TransferMultiBankConfrimResponse : BaseResponseModel() {
    var data: PaymentData? = null
}
