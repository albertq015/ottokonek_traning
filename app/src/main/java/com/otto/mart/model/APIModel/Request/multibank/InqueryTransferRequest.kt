package com.otto.mart.model.APIModel.Request.multibank

class InqueryTransferRequest(

    var bin: String? = "", // bin dari from account
    var currency: String? = "",
    var destinationAccountName: String? = "",//
    var destinationAccountNumber: String? = "",// account number dari list to account bank
    var destinationInstitutionId: String? = "",// bin dari  dari list to account bank
    var destinationInstitutionName: String? = "",// nama bank dari list to account bank
    var narrative: String? = "",//deskripsi
    var sourceAccountNumber: String? = "", //account number dari from account
    var transactionAmount: Int =0

)


