package com.otto.mart.model.APIModel.Response.multibank

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class IssuerLinkedBalanceResponse : BaseResponse() {
    var data: ILBRData? = null

    class ILBRData {
        var accountName = ""
        var accountNumber = ""
        var availableBalance = 0.0
        var branchCode = ""
        var currencyCode = ""
        var ledgerBalance = 0.0
        var productCode = ""
        var productName = ""
        var errorval = ""
        var formatedBalance = ""
        var errorCodeResponse : Int? = null
    }
}

