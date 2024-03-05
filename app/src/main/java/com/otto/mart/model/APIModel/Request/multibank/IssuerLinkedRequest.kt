package com.otto.mart.model.APIModel.Request.multibank

data class IssuerLinkedRequest(
    var accountName: String? ="",
    var accountNumber: String? = "",
    var accountStatus: String? = "",
    var accountType: String? = "",
    var bin: String? = "",
    var cid: String? = "",
    var mid: String? = "",
    var status: String? = ""
)
