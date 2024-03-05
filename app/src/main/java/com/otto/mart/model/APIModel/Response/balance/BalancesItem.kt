package com.otto.mart.model.APIModel.Response.balance

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BalancesItem(

	@field:JsonProperty("balance")
	var balance: Long? = null,

	@field:JsonProperty("name")
	var name: String? = null,

	@field:JsonProperty("logo")
	var logo: String? = null,

	@field:JsonProperty("formatted_balance")
	var formattedBalance: String? = null
)