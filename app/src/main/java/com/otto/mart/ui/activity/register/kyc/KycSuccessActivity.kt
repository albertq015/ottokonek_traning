package com.otto.mart.ui.activity.register.kyc

import android.content.Intent
import android.os.Bundle
import com.otto.mart.R
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import com.otto.mart.ui.activity.deposit.ottocash.WalletOttocashActivity

import kotlinx.android.synthetic.main.activity_kyc_success.*
import kotlinx.android.synthetic.main.content_kyc_success.*

class KycSuccessActivity : AppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kyc_success)

        initView()
    }

    private fun initView() {
        btnClose.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            startActivity(Intent(this, WalletOttocashActivity::class.java))
        }
    }
}
