package com.otto.mart.ui.activity.ppob.setup

import com.otto.mart.R
import com.otto.mart.model.localmodel.ppob.PpobPaymentMethod

class PpobPaymentMethodSetup {

     fun getPaymentMethod(): MutableList<PpobPaymentMethod> {
        var paymentMethodList = mutableListOf<PpobPaymentMethod>()

        var cash = PpobPaymentMethod(
                "Deposit",
                PpobPaymentMethod.DEPOSIT,
                R.drawable.ic_payment_method_cash,
                null,
                true
        )
        paymentMethodList.add(cash)

        var qr = PpobPaymentMethod(
                "Kode QR",
                PpobPaymentMethod.QR,
                R.drawable.ic_payment_method_qr,
                null,
                false
        )
        paymentMethodList.add(qr)

        return paymentMethodList
    }
}