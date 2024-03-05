package com.otto.mart.model.APIModel.Response

import com.fasterxml.jackson.annotation.JsonProperty
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

data class MerchantStatusResponse(
        var data: MerchantStatusData? = null

) : BaseResponseModel()

data class MerchantStatusData(
        @field:JsonProperty("status_suspense")
        val statusSuspense: Boolean? = null,

        @field:JsonProperty("mpan")
        val mpan: String? = null,

        @field:JsonProperty("level")
        var level: String? = null,

        @field:JsonProperty("mid")
        val mid: String? = null,

        @field:JsonProperty("store_name")
        val storeName: String? = null,

        @field:JsonProperty("register_status")
        val registerStatus: String? = null
)