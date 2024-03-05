package com.otto.mart.model.APIModel.Response.cashOut

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.android.parcel.Parcelize

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcelize
data class CashOutQr(
    var date_time: String? = null,
    var order_number: String? = null,
    var amount: Long? = null,
    var status: String? = null,
    var qrcode: String? = null,
    var expired_time: String? = null,
    var passcode: String? = null,
    var account_number: String? = null,
    var account_type: String? = null,
    var requestId: String? = null,
    var adminFee: Int? = 0
) : Parcelable