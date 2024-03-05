package com.otto.mart.model.localmodel.ppob

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class PpobProductType (
        val name: String? = null,
        var code: String? = null,
        val favoriteCode: String? = null,
        val icon: Int? = null,
        val isOldApi: Boolean? = null,
        var type: String? = null,
        val isSetKomisi: Boolean? = null
) : Parcelable {
    companion object{
        val PRABAYAR = "prabayar"
        val PASCABAYAR = "pascabayar"
        val DONASI = "donasi"
        const val QR_PAYMENT = "qr_payment"
        val TRANSFER_BANK = "transfer_bank"
        val REFUND = "refund"
    }
}