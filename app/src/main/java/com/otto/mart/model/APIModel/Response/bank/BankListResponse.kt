package com.otto.mart.model.APIModel.Response.bank

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
data class BankListResponse(@JsonProperty("data") val data: List<BankListItem>?) : BaseResponseModel()