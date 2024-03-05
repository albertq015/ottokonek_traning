package com.otto.mart.ui.activity.transaction.alfamart

import com.otto.mart.model.localmodel.payment.TopUpGuide

class TopUpGuideData {

    fun getTopUpDepositGuide(): MutableList<TopUpGuide> {
        var guideList = mutableListOf<TopUpGuide>()

        //Deposit Indomaret
        val stepDepositIndomaret = mutableListOf(
                "1. Infokan ke Kasir bahwa Anda akan topup OttoPay (pastikan Kasir memilih “OttoPay’ di mesin POS)",
                "2. Infokan ke Kasir angka Token dan Jumlah Topup yang Anda inginkan",
                "3. Setelah Kasir selesai bertransaksi di mesin POS, harap cek saldo Deposit OttoPay Anda dan pastikan saldo telah bertambah sejumlah Topup yang Anda lakukan"
        )

        var depositIndomaret = TopUpGuide(
                "Langkah-langkah Topup Saldo Deposit di Indomaret",
                stepDepositIndomaret,
                false
        )
        guideList.add(depositIndomaret)


        //Deposit Alfamart
        val stepDepositAlfammart = mutableListOf(
                "1. Infokan ke Kasir bahwa Anda akan topup OttoPay (pastikan Kasir memilih “OttoPay’ di mesin POS)",
                "2. Infokan ke Kasir nomor kode produk(812) + nomor HP. Contoh: 8120812XXXXXXX",
                "3. Setelah kasir melakukan konfirmasi nama toko, sebutkan nominal yang akan di Top-Up",
                "4. Setelah Kasir selesai bertransaksi di mesin POS, harap cek saldo Deposit OttoPay Anda dan pastikan saldo telah bertambah sejumlah Topup yang Anda lakukan"
        )

        var depositAlfamart = TopUpGuide(
                "Langkah-langkah Topup Saldo Deposit di Alfamart",
                stepDepositAlfammart,
                false
        )
        guideList.add(depositAlfamart)

        return guideList
    }

    fun getTopUpOttoCashGuide(): MutableList<TopUpGuide> {
        var guideList = mutableListOf<TopUpGuide>()

        //Top Up OttoCash Indomaret
        val stepTopUpOttoCashIndomaret = mutableListOf(
                "1. Infokan ke Kasir bahwa Anda akan topup OttoCash (pastikan Kasir memilih “OttoCash” di mesin POS)",
                "2. Infokan ke Kasir angka Token dan Jumlah Topup yang Anda inginkan",
                "3. Setelah Kasir selesai bertransaksi di mesin POS, harap cek saldo OttoCash Anda dan pastikan saldo telah bertambah sejumlah Topup yang Anda lakukan"
        )

        var topUpOttoCashIndomaret = TopUpGuide(
                "Langkah-langkah Topup Saldo OttoCash di Indomaret",
                stepTopUpOttoCashIndomaret,
                false
        )
        guideList.add(topUpOttoCashIndomaret)


        //Bayar OttoCash Indomaret
        val stepBayarOttoCashIndomaret = mutableListOf(
                "1. Infokan ke Kasir bahwa Anda akan bayar belanja dengan OttoCash (pastikan Kasir memilih “OttoCash” di mesin POS)",
                "2. Infokan ke Kasir angka Token",
                "3. Setelah Kasir selesai bertransaksi di mesin POS, maka saldo OttoCash Anda akan berkurang sejumlah total belanja Anda"
        )

        var bayarOttoCashIndomaret = TopUpGuide(
                "Langkah-langkah Bayar Belanja dengan OttoCash di Indomaret",
                stepBayarOttoCashIndomaret,
                false
        )
        guideList.add(bayarOttoCashIndomaret)


        //Bayar OttoCash Alfamart
        val stepBayarOttoCashAlfammart = mutableListOf(
                "1. Infokan ke Kasir bahwa Anda akan bayar belanja dengan OttoPay (pastikan Kasir memilih “OttoPay” di mesin POS)",
                "2. Infokan ke Kasir angka Token",
                "3. Setelah Kasir selesai bertransaksi di mesin POS, maka saldo OttoCash Anda akan berkurang sejumlah total belanja Anda"
        )

        var bayarOttoCashAlfamart = TopUpGuide(
                "Langkah-langkah Bayar Belanja di Alfamart",
                stepBayarOttoCashAlfammart,
                false
        )
        guideList.add(bayarOttoCashAlfamart)


        //Top Up OttoCash Alfamart
        val stepTopUpOttoCashAlfammart = mutableListOf(
                "1. Infokan ke Kasir bahwa Anda akan topup OttoCash (pastikan Kasir memilih “OttoCash\" di mesin POS)",
                "2. Infokan ke Kasir nomor kode produk(811) + nomor HP. Contoh: 8110812XXXXXXX ",
                "3. Setelah kasir melakukan konfirmasi nama toko, sebutkan nominal yang akan di Top-Up",
                "4. Setelah Kasir selesai bertransaksi di mesin POS, harap cek saldo Deposit OttoCash Anda dan pastikan saldo telah bertambah sejumlah Topup yang Anda lakukan"
        )

        var topUpOttoCashAlfamart = TopUpGuide(
                "Langkah-langkah Topup Saldo OttoCash di Alfamart",
                stepTopUpOttoCashAlfammart,
                false
        )
        guideList.add(topUpOttoCashAlfamart)

        return guideList
    }
}