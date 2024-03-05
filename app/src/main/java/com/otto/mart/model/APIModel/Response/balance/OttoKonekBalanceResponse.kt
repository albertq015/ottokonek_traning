package com.otto.mart.model.APIModel.Response.balance

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
data class OttoKonekBalanceResponse (
        var data: OttoKonekBalanceData? = null
) : BaseResponseModel()