package com.otto.mart.model.APIModel.Response.balance

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
data class BalanceResponse(
        var data: BalanceData? = null
) : BaseResponseModel()