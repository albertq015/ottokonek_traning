package com.otto.mart.ui.activity.wallet

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.*
import com.otto.mart.ui.Partials.adapter.wallet.WalletSuccessAdapter
import com.otto.mart.ui.Partials.decorator.SpaceDecorator
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_payment_success.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.OutputStream
import java.util.*
import kotlin.reflect.KFunction1

class PaymentSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        initView()
    }

    override fun onBackPressed() {
        backToHome()
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.text_bukti_transaksi)
        tvToolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.ocean_blue))
        imgToolbarLeft.setImageResource(R.drawable.icon_close_blue)
        imgToolbarLeft.layoutParams = imgToolbarLeft.layoutParams.apply {
            height = 20.px()
            width = 20.px()
        }

        tvTransactionTime.text = getString(R.string.text_transaction_time) + DataUtil.getDateString(Calendar.getInstance().timeInMillis, DataUtil.DATE_FORMAT)

        btnToolbarBack.setOnClickListener { onBackPressed() }
        btnShare.setOnClickListener { shareReceipt() }

        rvKeyValue.layoutManager = LinearLayoutManager(this)
        rvKeyValue.addItemDecoration(SpaceDecorator(
                20.px(),
                LinearLayout.VERTICAL,
                20.px(),
                20.px(),
                20.px()
        ))
    }

    override fun onStart() {
        super.onStart()
        showSuccessData()
    }

    private fun showSuccessData() {
        tvMerchantName.text = intent.getStringExtra("merchantName")
        val ppobPaymentData = getIntentData()
        val ppobOttoagPayment = getIntentPaymentAG()

        ppobOttoagPayment?.let {
            rvKeyValue.adapter = WalletSuccessAdapter(it.data.keyValueList).apply {
                listener = this@PaymentSuccessActivity::onResiFound
            }
        }
    }

    private fun getIntentData(): PpobPayment? {
        return intent.getParcelableExtra<PpobPayment>(AppKeys.KEY_PPOB_PAYMENT_DATA)
    }

    private fun getIntentPaymentAG(): PpobOttoagPaymentResponseModel? {
        val stringIntentData = intent.getStringExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA)
        stringIntentData?.let {
            return Gson().fromJson(stringIntentData, PpobOttoagPaymentResponseModel::class.java)
        }

        return null
    }

    private fun onResiFound(resi: String) {
        tvNoRef.text = "No Ref : $resi"
    }

    private fun backToHome() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun shareReceipt() {
        // View with spaces as per constraints
        //Bitmap bitmapView = ScreenShott.getInstance().takeScreenShotOfView(nsvParentSS);

        btnShare.gone()
        val bitmapView = BitmapUtil.getBitmapFromView(masterLayout, masterLayout.getChildAt(0).height, masterLayout.getChildAt(0).width)//ScreenShott.getInstance().takeScreenShotOfRootView(masterLayout)

        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val outstream: OutputStream?
        try {
            outstream = contentResolver.openOutputStream(uri!!)
            bitmapView.compress(Bitmap.CompressFormat.JPEG, 100, outstream)
            outstream!!.close()
        } catch (e: Exception) {
            System.err.println(e.toString())
        }

        share.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(share, getString(R.string.ppob_msg_share_it)))
        btnShare.visible()
    }
}
