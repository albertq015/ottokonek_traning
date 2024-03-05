package com.otto.mart.model.APIModel.Response.cashOut

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
class CashOutInputConfirmationResponse : BaseResponseModel() {
     var data: CashOutQr? = null
}