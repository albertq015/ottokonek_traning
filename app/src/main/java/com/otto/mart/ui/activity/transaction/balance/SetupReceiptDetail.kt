package com.otto.mart.ui.activity.transaction.balance

import android.content.Context
import com.otto.mart.R
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel
import com.otto.mart.model.APIModel.Misc.history.OmzetHistory
import com.otto.mart.support.util.DataUtil
import java.util.ArrayList

class SetupReceiptDetail {

    fun getOmzetReceipt(context: Context,item: OmzetHistory): MutableList<WidgetBuilderModel?> {

        val keyValueList: MutableList<WidgetBuilderModel?> = ArrayList()

        // #1
//        if (item.payment_method != null) {
//            if (!item.payment_method.equals("")) {
//                val transactionType = WidgetBuilderModel()
//                transactionType.key = context.getString(R.string.label_payment_type)
//                transactionType.value = item.payment_method
//                keyValueList.add(transactionType)
//            }
//        }

        // #2
        if (item.date_string != null) {
            if (!item.date_string.equals("")) {
                val date = WidgetBuilderModel()
                date.key = context.getString(R.string.text_date)
                date.value = item.date_string
                keyValueList.add(date)
            }
        }

        // #3
        if (item.reference_number != null) {
            if (!item.reference_number.equals("")) {
                val refNumber = WidgetBuilderModel()
                refNumber.key = "Receipt Number"
                refNumber.value = item.no_resi
                keyValueList.add(refNumber)
            }
        }

        // #4
        if (item.biller_reference != null) {
            if (!item.biller_reference.equals("")) {
                val billRef = WidgetBuilderModel()
                billRef.key = "No. Ref Biller"
                billRef.value = item.biller_reference
                keyValueList.add(billRef)
            }
        }

        // #5
        if (item.no_resi != null) {
            if (!item.no_resi.equals("")) {
                val noRefIssuer = WidgetBuilderModel()
                noRefIssuer.key = "Ref. Issuer Number"
                noRefIssuer.value = item.reference_number
                keyValueList.add(noRefIssuer)
            }
        }

        // #6
        if (item.noPelanggan != null) {
            if (!item.noPelanggan.equals("")) {
                val noPelanggan = WidgetBuilderModel()
                noPelanggan.key = "No. Pelanggan"
                noPelanggan.value = item.noPelanggan
                keyValueList.add(noPelanggan)
            }
        }

        // #7
        if (item.customer_name != null) {
            if (!item.customer_name.equals("")) {
                val customerName = WidgetBuilderModel()
                customerName.key = "Nama Pelanggan"
                customerName.value = item.customer_name
                keyValueList.add(customerName)
            }
        }

        if (item.custname != null) {
            if (!item.custname.equals("")) {
                val custname = WidgetBuilderModel()
                custname.key = "Nama Pelanggan"
                custname.value = item.custname
                keyValueList.add(custname)
            }
        }

        // #8
        if (item.description != null) {
            if (!item.description.equals("")) {
                val description = WidgetBuilderModel()
                description.key = "Description"
                description.value = item.description
                keyValueList.add(description)
            }
        }

        // #9
        if (item.noserial != null) {
            if (!item.noserial.equals("")) {
                val noserial = WidgetBuilderModel()
                noserial.key = "No. Serial"
                noserial.value = item.noserial
                keyValueList.add(noserial)
            }
        }

        // #10
        if (item.nometer != null) {
            if (!item.nometer.equals("")) {
                val nometer = WidgetBuilderModel()
                nometer.key = "No. Meter"
                nometer.value = item.nometer
                keyValueList.add(nometer)
            }
        }

        // #11
        if (item.daya != null) {
            if (!item.daya.equals("")) {
                val daya = WidgetBuilderModel()
                daya.key = "Daya"
                daya.value = item.daya
                keyValueList.add(daya)
            }
        }

        // #12
        if (item.stroom_token != null) {
            if (!item.stroom_token.equals("")) {
                val stroomToken = WidgetBuilderModel()
                stroomToken.key = "Stroom Token"
                stroomToken.value = item.stroom_token
                keyValueList.add(stroomToken)
            }
        }

        // #13
        if (item.kwhTotal != null) {
            if (!item.kwhTotal.equals("")) {
                val kwhTotal = WidgetBuilderModel()
                kwhTotal.key = "Total KWH"
                kwhTotal.value = item.kwhTotal
                keyValueList.add(kwhTotal)
            }
        }

        // #14
        if (item.bulanTagihan != null) {
            if (!item.bulanTagihan.equals("")) {
                val bulanTagihan = WidgetBuilderModel()
                bulanTagihan.key = "Bulan Tagihan"
                bulanTagihan.value = item.bulanTagihan
                keyValueList.add(bulanTagihan)
            }
        }

        // #15
        if (item.userId != null) {
            if (!item.userId.equals("")) {
                val userId = WidgetBuilderModel()
                userId.key = "User ID"
                userId.value = item.userId
                keyValueList.add(userId)
            }
        }

        // #16
        if (item.noServerId != null) {
            if (!item.noServerId.equals("")) {
                val noServerId = WidgetBuilderModel()
                noServerId.key = "No. Server ID"
                noServerId.value = item.noServerId
                keyValueList.add(noServerId)
            }
        }

        // #17
        if (item.noRoleName != null) {
            if (!item.noRoleName.equals("")) {
                val noRoleName = WidgetBuilderModel()
                noRoleName.key = "No. Role Name"
                noRoleName.value = item.noRoleName
                keyValueList.add(noRoleName)
            }
        }

        // #18
        if (item.kodePin != null) {
            if (!item.kodePin.equals("")) {
                val kodePin = WidgetBuilderModel()
                kodePin.key = "Kode PIN"
                kodePin.value = item.kodePin
                keyValueList.add(kodePin)
            }
        }

        // #19
        if (item.kodeVoucher != null) {
            if (!item.kodeVoucher.equals("")) {
                val kodeVoucher = WidgetBuilderModel()
                kodeVoucher.key = "Kode Voucher"
                kodeVoucher.value = item.kodeVoucher
                keyValueList.add(kodeVoucher)
            }
        }

        // #20
        if (item.noVaKeluarga != null) {
            if (!item.noVaKeluarga.equals("")) {
                val noVaKeluarga = WidgetBuilderModel()
                noVaKeluarga.key = "No. VA Keluarga"
                noVaKeluarga.value = item.noVaKeluarga
                keyValueList.add(noVaKeluarga)
            }
        }

        // #21
        if (item.periode != null) {
            if (!item.periode.equals("")) {
                val periode = WidgetBuilderModel()
                periode.key = "Periode"
                periode.value = item.periode
                keyValueList.add(periode)
            }
        }

        // #22
        if (item.tarif != null) {
            if (!item.tarif.equals("")) {
                val tarif = WidgetBuilderModel()
                tarif.key = "Tarif"
                tarif.value = item.tarif
                keyValueList.add(tarif)
            }
        }

        // #23
        if (item.jatuhTempo != null) {
            if (!item.jatuhTempo.equals("")) {
                val jatuhTempo = WidgetBuilderModel()
                jatuhTempo.key = "Jatuh Tempo"
                jatuhTempo.value = item.jatuhTempo
                keyValueList.add(jatuhTempo)
            }
        }

        // #24
        if (item.retKebersihan != null) {
            if (!item.retKebersihan.equals("")) {
                val retKebersihan = WidgetBuilderModel()
                retKebersihan.key = "Ret. Kebersihan"
                retKebersihan.value = item.retKebersihan
                keyValueList.add(retKebersihan)
            }
        }

        // #25
        if (item.standMeter != null) {
            if (!item.standMeter.equals("")) {
                val standMeter = WidgetBuilderModel()
                standMeter.key = "Stand Meter"
                standMeter.value = item.standMeter
                keyValueList.add(standMeter)
            }
        }

        // #26
        if (item.kubikasi != null) {
            if (!item.kubikasi.equals("")) {
                val kubikasi = WidgetBuilderModel()
                kubikasi.key = "Kubikasi"
                kubikasi.value = item.kubikasi
                keyValueList.add(kubikasi)
            }
        }

        // #27
        if (item.noHp != null) {
            if (!item.noHp.equals("")) {
                val noHp = WidgetBuilderModel()
                noHp.key = "No. Handphone"
                noHp.value = item.noHp
                keyValueList.add(noHp)
            }
        }

        // #28
        if (item.npwp != null) {
            if (!item.npwp.equals("")) {
                val npwp = WidgetBuilderModel()
                npwp.key = "NPWP"
                npwp.value = item.npwp
                keyValueList.add(npwp)
            }
        }

        // #29
        if (item.email != null) {
            if (!item.email.equals("")) {
                val email = WidgetBuilderModel()
                email.key = "Email"
                email.value = item.email
                keyValueList.add(email)
            }
        }

        // #30
        if (item.nopolis != null) {
            if (!item.nopolis.equals("")) {
                val nopolis = WidgetBuilderModel()
                nopolis.key = "No. Polis"
                nopolis.value = item.nopolis
                keyValueList.add(nopolis)
            }
        }

        // #31
        if (item.petunjukPenggunaan != null) {
            if (!item.petunjukPenggunaan.equals("")) {
                val petunjukPenggunaan = WidgetBuilderModel()
                petunjukPenggunaan.key = "Petunjuk Penggunaan"
                petunjukPenggunaan.value = item.petunjukPenggunaan
                keyValueList.add(petunjukPenggunaan)
            }
        }

        // #32
        if (item.cpan != null) {
            if (!item.cpan.equals("")) {
                val cpan = WidgetBuilderModel()
                cpan.key = "CPAN"
                cpan.value = item.cpan
                keyValueList.add(cpan)
            }
        }

        // #33
        if (item.metodePencairan != null) {
            if (!item.metodePencairan.equals("")) {
                val metodePencairan = WidgetBuilderModel()
                metodePencairan.key = "Metode Pencairan"
                metodePencairan.value = item.metodePencairan
                keyValueList.add(metodePencairan)
            }
        }

        // #34
        if (item.namaBank != null) {
            if (!item.namaBank.equals("")) {
                val namaBank = WidgetBuilderModel()
                namaBank.key = "Nama Bank"
                namaBank.value = item.namaBank
                keyValueList.add(namaBank)
            }
        }

        // #35
        if (item.noRekening != null) {
            if (!item.noRekening.equals("")) {
                val noRekening = WidgetBuilderModel()
                noRekening.key = "No. Rekening"
                noRekening.value = item.noRekening
                keyValueList.add(noRekening)
            }
        }

        // #36
        if (item.namaPemilikRek != null) {
            if (!item.namaPemilikRek.equals("")) {
                val namaPemilikRek = WidgetBuilderModel()
                namaPemilikRek.key = "Nama Pemilik Rek"
                namaPemilikRek.value = item.namaPemilikRek
                keyValueList.add(namaPemilikRek)
            }
        }

        // #37
        if (item.nominalPencairan != null) {
            if (!item.nominalPencairan.equals("")) {
                val nominalPencairan = WidgetBuilderModel()
                nominalPencairan.key = "Nominal Pencairan"
                nominalPencairan.value = DataUtil.convertCurrency(item.amount)
                keyValueList.add(nominalPencairan)
            }
        }

        // #38
        if (item.commission != null) {
            if (!item.commission.equals("") && !item.commission.equals("Rp 0") &&
                    !item.commission.equals("0") && !item.commission.equals("Rp0")) {
                val commission = WidgetBuilderModel()
                commission.key = "Komisi"
                commission.value = DataUtil.convertCurrency(item.commission)
                keyValueList.add(commission)
            }
        }

        // #39
        if (item.retKebersihan != null) {
            if (!item.retKebersihan.equals("") && !item.retKebersihan.equals("Rp 0") &&
                    !item.retKebersihan.equals("0") && !item.retKebersihan.equals("Rp0")) {
                val commission = WidgetBuilderModel()
                commission.key = "Ret Kebersihan"
                commission.value = DataUtil.convertCurrency(item.retKebersihan)
                keyValueList.add(commission)
            }
        }

        // #-5
        if (item.harga != null) {
            if (!item.harga.equals("")) {
                val harga = WidgetBuilderModel()
                harga.key = "Price"
                harga.value = DataUtil.convertCurrency(item.amount)
                keyValueList.add(harga)
            }
        }

        // #-4
        if (item.denda != null) {
            if (!item.denda.equals("") && !item.denda.equals("Rp 0") &&
                    !item.denda.equals("0") &&  !item.denda.equals("Rp0")) {
                val denda = WidgetBuilderModel()
                denda.key = "Denda"
                denda.value = item.denda
                keyValueList.add(denda)
            }
        }

        // #-3
        if (item.biayaAdmin != null) {
            if (!item.biayaAdmin.equals("") && !item.biayaAdmin.equals("Rp 0") &&
                    !item.biayaAdmin.equals("0") &&  !item.biayaAdmin.equals("Rp0")) {
                val biayaAdmin = WidgetBuilderModel()
                biayaAdmin.key = "Biaya Admin"
                biayaAdmin.value = item.biayaAdmin
                keyValueList.add(biayaAdmin)
            }
        }

        // #-2
        if (item.total != null) {
            if (!item.total.equals("")) {
                val total = WidgetBuilderModel()
                total.key = "Total"
                total.value = item.total
                keyValueList.add(total)
            }
        }

        // #-1
        if (item.status != null) {
            if (!item.status.equals("")) {
                val status = WidgetBuilderModel()
                status.key = "Status"
                status.value = item.status
                keyValueList.add(status)
            }
        }

        return keyValueList
    }

    fun getDepositReceipt(item: OmzetHistory): MutableList<WidgetBuilderModel?> {

        val keyValueList: MutableList<WidgetBuilderModel?> = ArrayList()

        val noResi = WidgetBuilderModel()
        noResi.key = "Nomor Resi"
        noResi.value = item.reference_number
        keyValueList.add(noResi)

        if ((item.no_resi != null) && !item.no_resi.equals("", ignoreCase = true) && !item.no_resi?.trim { it <= ' ' }.equals("-", ignoreCase = true)) {
            val noResiCust = WidgetBuilderModel()
            noResiCust.key = "Issuer Ref"
            noResiCust.value = item.no_resi
            keyValueList.add(noResiCust)
        }

        val date = WidgetBuilderModel()
        date.key = "Tanggal Transaksi"
        date.value = item.date_string
        keyValueList.add(date)

        item.description?.let {
            val desc = WidgetBuilderModel()
            desc.key = "Description"
            desc.value = item.description
            keyValueList.add(desc)
        }

        val paymentMethod = WidgetBuilderModel()
        paymentMethod.key = "Metode Pembayaran"
        paymentMethod.value = item.payment_method
        keyValueList.add(paymentMethod)

        val transactionType = WidgetBuilderModel()
        transactionType.key = "Tipe Transaksi"
        transactionType.value = item.type
        keyValueList.add(transactionType)

        val direction = WidgetBuilderModel()
        direction.key = "Direction"
        direction.value = item.direction
        keyValueList.add(direction)

        if ((item.biller_reference != null) && !item.biller_reference.equals("", ignoreCase = true) && !item.biller_reference?.trim { it <= ' ' }.equals("-", ignoreCase = true)) {
            val billRef = WidgetBuilderModel()
            billRef.key = "No. Referensi Biller"
            billRef.value = item.biller_reference
            keyValueList.add(billRef)
        }

        if ((item.stroom_token != null) && !item.stroom_token.equals("", ignoreCase = true) && !item.stroom_token?.trim { it <= ' ' }.equals("-", ignoreCase = true)) {
            val token = WidgetBuilderModel()
            token.key = "Token"
            token.value = item.stroom_token
            keyValueList.add(token)
        }

        if ((item.serial != null) && !item.serial.equals("", ignoreCase = true) && !item.serial?.trim { it <= ' ' }.equals("-", ignoreCase = true)) {
            val serial = WidgetBuilderModel()
            serial.key = "Serial"
            serial.value = item.serial
            keyValueList.add(serial)
        }

        if ((item.pinkode != null) && !item.pinkode.equals("", ignoreCase = true) && !item.pinkode?.trim { it <= ' ' }.equals("-", ignoreCase = true)) {
            val serial = WidgetBuilderModel()
            serial.key = "Pin Kode"
            serial.value = item.pinkode
            keyValueList.add(serial)
        }

        if ((item.commission != null) && item.commission != "" && item.commission?.trim { it <= ' ' } != "-"
                && item.commission?.trim { it <= ' ' } != "0") {
            val totalPayment = WidgetBuilderModel()
            totalPayment.key = "Komisi"
            totalPayment.value = DataUtil.convertCurrency(item.commission)
            keyValueList.add(totalPayment)
        }

        val totalPayment = WidgetBuilderModel()
        totalPayment.key = "Jumlah Total"
        totalPayment.value = DataUtil.convertCurrency(item.amount)
        keyValueList.add(totalPayment)

        return keyValueList
    }

}