package com.otto.mart.model.APIModel.Response.cashOut

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import kotlinx.android.parcel.Parcelize

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcelize
data class CashOutInput(
        var account_number: String? = null,
        var amount: Long? = null,
        var customer_id: String? = null,
        var customer_name: String? = null,
        var date_time: String? = null,
        var bin : String? = null,
        var rrn: String? = null,
        var accountBank : String? = null
) : Parcelable