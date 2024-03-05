package com.otto.mart.ui.activity.cashOut

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Response.cashOut.CashOutQr
import com.otto.mart.model.localmodel.WebViewContent
import com.otto.mart.support.util.BitmapUtil
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.DeviceUtil
import com.otto.mart.support.util.UIUtils
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.content_cash_out_qr_detail.*

class CashOutQrDetailActivity : AppActivity() {

    companion object {
        val KEY_CASH_OUT_DATA = "cash_out_data"
        val KEY_IS_FROM_CONFIRMATION = "is_from_confirmation"
    }

    var mCashOutQr: CashOutQr? = null
    var isFromConfrimation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_out_qr_detail)

        //collect our intent
        mCashOutQr = intent.getParcelableExtra<Parcelable>(KEY_CASH_OUT_DATA) as CashOutQr

        if (intent.hasExtra(KEY_IS_FROM_CONFIRMATION)) {
            isFromConfrimation = intent.getBooleanExtra(KEY_IS_FROM_CONFIRMATION, false)
        }

        initView()
        displayQrDetail()
    }

    override fun onBackPressed() {
        if (isFromConfrimation) {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

            startActivity(Intent(this, CashOutMenuActivity::class.java))
            startActivity(Intent(this, CashOutQrListActivity::class.java))
        } else {
            super.onBackPressed()
        }
    }

    private fun initView() {
        val screenWidth = resources.displayMetrics.widthPixels
        val width: Int = (70 * screenWidth) / 100
        UIUtils.resize(ivQr, width, width)

        btnClose.setOnClickListener {
            onBackPressed()
        }
    }

    private fun displayQrDetail() {
        mCashOutQr?.let {
            tvExpiredDate.text = it.expired_time
            tvDate.text = it.date_time
            tvPasscode.text = it.passcode
            tvAccountNumber.text = it.account_number
            tvAccountType.text = it.account_type
            tvAmount.text = DataUtil.InputDecimal(it.amount.toString())
            tvStatus.text = it.status
            tv_request_id.text = it.requestId
            tv_admin_fee.text = it.adminFee.toString()
            displayQrCode(it.qrcode)
        }
    }

    private fun displayQrCode(qrContent: String?) {
        ivQr.setImageBitmap(
            BitmapUtil.generateBitmap(
                qrContent,
                9,
                DeviceUtil.dpToPx(240),
                DeviceUtil.dpToPx(240)
            )
        )
    }
}