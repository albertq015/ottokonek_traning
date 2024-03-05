package com.otto.mart.model.APIModel.Request.cashOut

class CashOutInputConfirmationRequest {
    var account_number : String? = null
    var amount: Long? = null
    var bin : String? = null
    var pin: String? = null
    var rrn: String? = null
}