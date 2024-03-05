package com.otto.mart.model.localmodel.ppob

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class PpobPayment(
        var custNumber: String? = null,
        var email: String? = null,
        var komisi: Long? = null,
        var period: Long? = null,
        var serverId: String? = null,
        var roleName: String? = null,
        var isAddToFavorite: Boolean? = null,
        var ppobProductType: PpobProductType? = null,
        var ppobPaymentMethod: PpobPaymentMethod? = null,
        var productName: String? = null,
        var productCode: String? = null,
        var totalPayment: Double? = null
) : Parcelable