package com.otto.mart.model.APIModel.Response.bank

import com.fasterxml.jackson.annotation.JsonProperty

data class BankAccountListOKKData(
        @JsonProperty("approved") val approved: List<BankOKKItem>? = null,
        @JsonProperty("on_process") val onProcess: List<BankOKKItem>? = null,
        @JsonProperty("rejected") val rejected: List<BankOKKItem>? = null
)

