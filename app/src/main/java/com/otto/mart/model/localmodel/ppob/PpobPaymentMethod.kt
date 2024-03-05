package com.otto.mart.model.localmodel.ppob

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class PpobPaymentMethod (
        var name: String? = null,
        val code: String? = null,
        val icon: Int? = null,
        val balance: Long? = null,
        var selected: Boolean? = null
) : Parcelable {
    companion object {
        val DEPOSIT = "deposit"
        val QR = "qr"
        val SPLIT = "split"
    }
}