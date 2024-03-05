package com.otto.mart.model.APIModel.Response.transfer

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel

@JsonIgnoreProperties(ignoreUnknown = true)
class TransferBankInquiryResponse : BaseResponseModel() {
    var data: Data? = null

    class Data {
        var amount = 0L
        var rrn: String? = null
        var receiverName: String? = null
        var date: String? = null
        var senderCustName: String? = null
        var beneficiaryBankName: String? = null
        var beneficiaryAccountNo: String? = null
        var beneficiaryCustName: String? = null
        var custRefNumber: String? = null
        var transferAmount: String? = null
    }
}