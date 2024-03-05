package com.otto.mart.model.APIModel.Response.balance

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class BalanceData(
	var balances: List<BalancesItem?>? = null
)