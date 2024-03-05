package com.otto.mart.model.APIModel.Request.multibank

data class AddBankAccountRequest(
    var accountName: String? = "",
    var accountNumber: String? = "",
    var bankCode: String? = "",
    var mid: String? = "",
    var notes: String? = ""
)