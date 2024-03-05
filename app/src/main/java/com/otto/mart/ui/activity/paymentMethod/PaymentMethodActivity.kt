package com.otto.mart.ui.activity.paymentMethod

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.otto.mart.R
import com.otto.mart.ui.activity.AppActivity
import kotlinx.android.synthetic.main.activity_select_payment_method.*
import kotlinx.android.synthetic.main.toolbar.*

class PaymentMethodActivity : AppActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_payment_method)

        initView()

    }

    fun initView() {
        tvToolbarTitle.text = "Select Payment Method"
        btn_pay_at_order.setOnClickListener {
            val intent = Intent(this, PaymentSelectType::class.java)
            intent.putExtra("label","pao")
            startActivity(intent)
        }

        btn_pay_at_delivery.setOnClickListener {
            val intent = Intent(this, PaymentSelectType::class.java)
            intent.putExtra("label","pad")
            startActivity(intent)
        }

    }
}