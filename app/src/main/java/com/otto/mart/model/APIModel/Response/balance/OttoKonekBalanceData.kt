package com.otto.mart.model.APIModel.Response.balance

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OttoKonekBalanceData(
	var name: String? = null,
	var logo: String? = null,
	var balance: Double? = null,
	var formatted_balance: String? = null
)