package com.otto.mart.model.APIModel.Response.cashOut

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
class CashOutListResponse : BaseResponseModel() {

    var data: QrCode? = null

    @JsonIgnoreProperties(ignoreUnknown = true)
    class QrCode {
        var qrcodes: List<CashOutQr>? = null
    }
}