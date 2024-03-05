package com.otto.mart.model.APIModel.Request.multibank

class AddBankInquiryRequest(
    var bin: String? = "", // bin dari from account
    var destinationAccountNumber: String? = ""// account number dari list to account bank

)