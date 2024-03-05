package com.otto.mart.model.APIModel.Request.multibank

data class IssuerLinkedBalance(

    var accountNumber: String? = "",
    var bin: String? = "",
    var id: Int? = 0
)