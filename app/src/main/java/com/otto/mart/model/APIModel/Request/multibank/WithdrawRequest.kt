package com.otto.mart.model.APIModel.Request.multibank

class WithdrawRequest(
    var accountNumber: String? = "",
    var amount: Double? = 0.0,
    var bin: String? = "",
    var pin: String? = ""
)