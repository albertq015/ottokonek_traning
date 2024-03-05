package com.otto.mart.model.APIModel.Request.multibank

data class IssuerLinkedConfrim(
    var accountNumber: String? = "",
    var bin: String? = "",
    var otp: String? = ""
)