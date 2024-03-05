package com.otto.mart.model.APIModel.Response.transfer

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
class TransferBankPaymentResponse : BaseResponseModel() {
    var data: Data? = null

    class Data {
        var amount = 0L
        var rrn: String? = null
        var receiverName: String? = null
    }
}