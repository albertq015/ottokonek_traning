package com.otto.mart.model.APIModel.Request.transfer

data class TransferBankPaymentRequest(
        var amount: Double? = 0.0,
        var rrn: String? = "",
        var pin: String? = "",
        var latitude: Double? = 0.0,
        var longitude: Double? = 0.0,
        var ip: String? = ""
)