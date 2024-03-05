package com.otto.mart.model.APIModel.Misc.history

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
class OmzetHistory {
    var type: String? = null
    var merchant_name: String? = null
    var reference_number: String? = null
    var noPelanggan: String? = null
    var description: String? = null
    var date = 0L
    var date_string: String? = null
    var status: String? = null
    var direction: String? = null
    var payment_method: String? = null
    var no_resi: String? = null
    var biller_reference: String? = null
    var stroom_token: String? = null
    var serial: String? = null
    var customer_id: String? = null
    var customer_name: String? = null
    var commission: String? = null
    var custname: String? = null
    var noserial: String? = null
    var nometer: String? = null
    var daya: String? = null
    var kwhTotal: String? = null
    var bulanTagihan: String? = null
    var userId: String? = null
    var noServerId: String? = null
    var noRoleName: String? = null
    var kodePin: String? = null
    var noVaKeluarga: String? = null
    var periode: String? = null
    var tarif: String? = null
    var jatuhTempo: String? = null
    var retKebersihan: String? = null
    var standMeter: String? = null
    var kubikasi: String? = null
    var noHp: String? = null
    var npwp: String? = null
    var email: String? = null
    var nopolis: String? = null
    var pinkode: String? = null
    var nomortransaksi: String? = null
    var billref: String? = null
    var merchantId: String? = null
    var kodeVoucher: String? = null
    var petunjukPenggunaan: String? = null
    var cpan: String? = null
    var metodePencairan: String? = null
    var namaBank: String? = null
    var noRekening: String? = null
    var namaPemilikRek: String? = null
    var nominalPencairan: String? = null
    var biayaAdmin: String? = null
    var harga: String? = null
    var denda: String? = null
    var total: String? = null
    var uiMsg: String? = null
    var entry_date: String? = null
    var posting_date: String? = null
    var currency: String? = null
    var transaction_indicator: String? = null
    var amount: Double? = null
    var running_balance: Double? = null
}