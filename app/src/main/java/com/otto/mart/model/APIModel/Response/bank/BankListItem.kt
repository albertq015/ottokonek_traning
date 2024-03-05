package com.otto.mart.model.APIModel.Response.bank

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BankListItem(
        @JsonProperty("code") val code: String? = null,
        @JsonProperty("fullName") val fullName: String? = null,
        @JsonProperty("shortName") val shortName: String? = null,
        @JsonProperty("urlImage") val urlImage: String? = null
)

