package com.otto.mart.model.APIModel.Response.cashOut

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CashOutAmount (
        var amount: String? = null,
        var isSelected: Boolean? = false
)