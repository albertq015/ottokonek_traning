package com.otto.mart.model.APIModel.Request.transfer

data class TransferBankInquiryRequest(
        var amount: Double? = 0.0,
        var bankCode: String? = "",
        var bankAccount: String? = "",
        var custRefNumber: String? = "",
        var phone: String? = "",
        var latitude: Double? = 0.0,
        var longitude: Double? = 0.0,
        var ip: String? = "",

        //ottokonek
        var accountName: String? = ""
)