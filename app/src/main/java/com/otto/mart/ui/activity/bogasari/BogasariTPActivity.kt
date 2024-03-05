package com.otto.mart.ui.activity.bogasari

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.bogasari.BogasariInquiryData
import com.otto.mart.support.util.DataUtil
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_bogasari_tp.*
import kotlinx.android.synthetic.main.dialog_header.*

class BogasariTPActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bogasari_tp)
        initContent()
    }

    override fun onBackPressed() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun initContent() {
        val data = intent.getParcelableExtra<BogasariInquiryData>("payment")
      //  trasactionDate.setText(DataUtil.getDateString(data.datetime?.toLong() ?: 0L))
        orderNo.setText(data?.rrn)
        total.setText(DataUtil.convertCurrency(data?.amount))

        closeButton.setOnClickListener { onBackPressed() }
    }
}