package com.otto.mart.model.APIModel.Response.multibank

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class HistoryTransactionAccount {
    var entry_date: String? = null
    var posting_date: String? = null
    var description: String? = null
    var currency: String? = null
    var transaction_indicator: String? = null
    var amount: Double? = null
    var running_balance: Int = 0
    var reference_number: String? = null


}