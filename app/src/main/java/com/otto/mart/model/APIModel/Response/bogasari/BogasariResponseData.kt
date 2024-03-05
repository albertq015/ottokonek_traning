package com.otto.mart.model.APIModel.Response.bogasari

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BogasariResponseData(

        @JsonProperty("merchant_id")
        var merchantId: String? = null,

        @JsonProperty("merchant_name")
        var merchantName: String? = null,

        @JsonProperty("product_list")
        var productList: BogasariProductResponse? = null
)