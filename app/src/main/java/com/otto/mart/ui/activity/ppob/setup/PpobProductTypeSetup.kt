package com.otto.mart.ui.activity.ppob.setup

import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.localmodel.ppob.PpobProductType

class PpobProductTypeSetup {

    companion object {
        val KEY_FAVORITE_VOUCHER_GAME = "voucher-game"
        val KEY_FAVORITE_TOP_UP = "top-up"
    }

    //Pulsa
    fun getProductPulsa(): PpobProductType {
        return PpobProductType(
                "Pulsa",
                AppKeys.PPOB_TYPE_PULSA,
                "phone-prepaid2",
                R.drawable.icon_button_pulsa,
                false,
                PpobProductType.PRABAYAR,
                false
        )
    }

    //Pulsa Pascabayar
    fun getProductPulsaPascabayar(): PpobProductType {
        return PpobProductType(
                "Pulsa",
                AppKeys.PPOB_TYPE_PULSA_PASCABAYAR,
                "phone-postpaid2",
                R.drawable.icon_button_pulsa,
                false,
                PpobProductType.PASCABAYAR,
                false
        )
    }

    //Paket Data
    fun getProductPaketData(): PpobProductType {
        return PpobProductType(
                "Paket Data",
                AppKeys.PPOB_TYPE_PAKET_DATA,
                "phone-data2",
                R.drawable.icon_button_paketdata,
                false,
                PpobProductType.PRABAYAR,
                false
        )
    }

    //Listrik Token
    fun getProductListrikToken(): PpobProductType {
        return PpobProductType(
                "Listrik PLN",
                AppKeys.PPOB_TYPE_ELECTRICITY_TOKEN,
                "electricity-token2",
                R.drawable.icon_button_tagihanlistrik,
                false,
                PpobProductType.PRABAYAR,
                false
        )
    }

    //Listrik Tagihan
    fun getProductListrikTagihan(): PpobProductType {
        return PpobProductType(
                "Listrik PLN",
                AppKeys.PPOB_TYPE_ELECTRICITY_BILL,
                "electricity-bill2",
                R.drawable.icon_button_tagihanlistrik,
                false,
                PpobProductType.PASCABAYAR,
                false
        )
    }

    //Air
    fun getProductAir(): PpobProductType {
        return PpobProductType(
                "Tagihan Air",
                AppKeys.PPOB_TYPE_AIR,
                "pdam2",
                R.drawable.icon_button_tagihanlistrik,
                false,
                PpobProductType.PASCABAYAR,
                false
        )
    }

    //Internet
    fun getProductInternet(): PpobProductType {
        return PpobProductType(
                "Internet",
                AppKeys.PPOB_TYPE_INTERNET,
                "tv-cable-internet",
                R.drawable.icon_button_cbn,
                false,
                PpobProductType.PASCABAYAR,
                false
        )
    }

    //Asuransi
    fun getProductAsuransi(): PpobProductType {
        return PpobProductType(
                "Asuransi",
                AppKeys.PPOB_TYPE_ASURANSI,
                "insurance",
                R.drawable.icon_button_asuransi,
                true,
                PpobProductType.PASCABAYAR,
                false
        )
    }

    //Telkom
    fun getProductTelkom(): PpobProductType {
        return PpobProductType(
                "Telepon Rumah",
                AppKeys.PPOB_TYPE_TELKOM,
                "telkom2",
                R.drawable.icon_button_telkom,
                false,
                PpobProductType.PASCABAYAR,
                false
        )
    }

    //Telkom Internet
    fun getProductTelkomInternet(): PpobProductType {
        return PpobProductType(
                "IndiHome / Speedy",
                AppKeys.PPOB_TYPE_TELKOM_INTERNET,
                "speedy",
                R.drawable.icon_button_telkom,
                false,
                PpobProductType.PASCABAYAR,
                false
        )
    }

    //Pendidikan
    fun getProductPendidikan(): PpobProductType {
        return PpobProductType(
                "Pendidikan",
                AppKeys.PPOB_TYPE_PENDIDIKAN,
                "education",
                R.drawable.icon_button_education,
                false,
                PpobProductType.PASCABAYAR,
                false
        )
    }

    //Top Up
    fun getProductTopUp(): PpobProductType {
        return PpobProductType(
                "Isi Saldo",
                AppKeys.PPOB_TYPE_TOP_UP,
                KEY_FAVORITE_TOP_UP,
                R.drawable.icon_button_topupemoney,
                false,
                PpobProductType.PRABAYAR,
                false
        )
    }

    //Cicilan
    fun getProductCicilan(): PpobProductType {
        return PpobProductType(
                "Cicilan",
                AppKeys.PPOB_TYPE_CICILAN,
                "multi-finance",
                R.drawable.icon_button_cicilan,
                false,
                PpobProductType.PASCABAYAR,
                false
        )
    }

    //BPJS
    fun getProductBpjs(): PpobProductType {
        return PpobProductType(
                "BPJS",
                AppKeys.PPOB_TYPE_BPJS,
                "bpjs",
                R.drawable.icon_button_bpjs,
                false,
                PpobProductType.PASCABAYAR,
                false
        )
    }

    //Donasi
    fun getProductDonasi(): PpobProductType {
        return PpobProductType(
                "Donasi",
                AppKeys.PPOB_TYPE_DONASI,
                "donation",
                R.drawable.icon_button_donasi,
                false,
                PpobProductType.DONASI,
                false
        )
    }

    //Voucher Game
    fun getProductGame(): PpobProductType {
        return PpobProductType(
                "Voucher Game",
                AppKeys.PPOB_TYPE_GAME,
                KEY_FAVORITE_VOUCHER_GAME,
                R.drawable.icon_button_game,
                false,
                PpobProductType.PRABAYAR,
                false
        )
    }

    //Voucher
    fun getProductVoucher(): PpobProductType {
        return PpobProductType(
                "Voucher",
                AppKeys.PPOB_TYPE_VOUCHER,
                "voucher",
                R.drawable.icon_button_hiburan,
                false,
                PpobProductType.PRABAYAR,
                false
        )
    }

    //Transfer Bank
    fun getProductTransferBank(): PpobProductType {
        return PpobProductType(
                "Transfer ke Bank",
                PpobProductType.QR_PAYMENT,
                PpobProductType.QR_PAYMENT,
                R.drawable.icon_button_donasi,
                false,
                PpobProductType.TRANSFER_BANK,
                false
        )
    }
}