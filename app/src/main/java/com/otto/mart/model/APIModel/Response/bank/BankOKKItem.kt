package com.otto.mart.model.APIModel.Response.bank

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BankOKKItem(
        @JsonProperty("accountNumber") val accountNumber: String?,
        @JsonProperty("bankCode") val bankCode: String?,
        @JsonProperty("accountName") val accountName: String?,
        @JsonProperty("bankName") val bankName: String?,
        @JsonProperty("urlImage") val urlImage: String?,
        @JsonProperty("resLogo") var resLogo: Int = 0,
        @JsonProperty("id") val id: Int
)

