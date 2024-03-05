package com.otto.mart.model.APIModel.Request.multibank

data class TransferMultiBankConfrimRequest(
    var bin: String? = "",
    var sourceAccountNumber: String? = ""

)