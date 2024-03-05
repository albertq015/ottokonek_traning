package com.otto.mart.model.APIModel.Response.multibank

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
class TransferBankInquiryMultiBankResponse : BaseResponseModel() {
    var data: Data? = null

    @JsonIgnoreProperties(ignoreUnknown = true)
    class Data {
        var requestId: String? = null
        var rrn: String? = null
        var destinationAccountNumber: String? = null
        var destinationAccountName: String? = null
        var destinationAccountCurrency: String? = null
        var destinationBranchCode: String? = null
        var adminFee: Int? = 0
        var destinationAccountBank: String? = null
        var productCode: String? = null
        var productName: String? = null
        var transactionAmount: Int? = null
        var inactiveFlag : String? = null
    }

}