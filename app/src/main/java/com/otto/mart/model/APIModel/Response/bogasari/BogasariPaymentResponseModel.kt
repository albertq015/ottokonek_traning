package com.otto.mart.model.APIModel.Response.bogasari

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
data class BogasariPaymentResponseModel(

        @JsonProperty("data")
        var data: BogasariPaymentResponseData? = null

): BaseResponseModel()