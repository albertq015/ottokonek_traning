package com.otto.mart.model.APIModel.Request.transfer

data class TransferSknRequest (
      var account_number: String = "",
      var amount: Long = 0L,
      var bank_code: String = "",
      var bank_name: String = "",
      var currency_code: String = "",
      var customer_name: String = "",
      var description: String = "",
      var submitted_at: String = ""
)