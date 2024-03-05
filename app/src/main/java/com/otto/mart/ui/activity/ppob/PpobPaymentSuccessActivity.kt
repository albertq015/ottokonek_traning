package com.otto.mart.ui.activity.ppob

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel
import com.otto.mart.model.APIModel.Response.PpobOttoagInquiryResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.model.APIModel.Response.donasi.DonasiInquiryResponse
import com.otto.mart.model.APIModel.Response.multibank.ReceiptAdressResponse
import com.otto.mart.model.APIModel.Response.ppob.PpobInquiryResponse
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.support.util.ApiCallback
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.ppob.PpobPaymentDetailAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.content_ppob_payment_success.*

class PpobPaymentSuccessActivity : AppActivity() {

    val TAG = this.javaClass.simpleName

    var mPpobOttoagPaymentResponseModel: PpobOttoagPaymentResponseModel? = null
    var mPpobInquiryResponse = PpobInquiryResponse()
    var mDonasiInquiryResponse = DonasiInquiryResponse()
    var mPpobInquiryOldResponse = PpobOttoagInquiryResponseModel()
    var mPpobPayment = PpobPayment()
    var mAddresContact = ReceiptAdressResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        disableScreenshot()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppob_payment_success)



        if (intent.hasExtra(AppKeys.KEY_PPOB_PAYMENT_DATA)) {
            mPpobPayment =
                intent.getParcelableExtra<Parcelable>(AppKeys.KEY_PPOB_PAYMENT_DATA) as PpobPayment


        }

        if (intent.hasExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA)) {
            mPpobOttoagPaymentResponseModel = Gson().fromJson(
                intent.getStringExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA),
                PpobOttoagPaymentResponseModel::class.java
            )

        }

        if (intent.hasExtra(AppKeys.KEY_PPOB_INQUIRY_DATA)) {
            mPpobInquiryResponse = Gson().fromJson(
                intent.getStringExtra(AppKeys.KEY_PPOB_INQUIRY_DATA),
                PpobInquiryResponse::class.java
            )

        }

        if (intent.hasExtra(AppKeys.KEY_PPOB_DONASI_INQUIRY_DATA)) {
            mDonasiInquiryResponse = Gson().fromJson(
                intent.getStringExtra(AppKeys.KEY_PPOB_DONASI_INQUIRY_DATA),
                DonasiInquiryResponse::class.java
            )

        }

        if (intent.hasExtra(AppKeys.KEY_PPOB_INQUIRY_OLD_DATA)) {
            mPpobInquiryOldResponse = Gson().fromJson(
                intent.getStringExtra(AppKeys.KEY_PPOB_INQUIRY_OLD_DATA),
                PpobOttoagInquiryResponseModel::class.java
            )
        }

        initView()
        initRecyclerView()
        displayPaymentInfo()

        //playNotificationTone()

        if (mPpobOttoagPaymentResponseModel != null) {
            mPpobOttoagPaymentResponseModel?.data?.keyValueList?.let { displayPaymentDetail(it) }
        } else {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        disableScreenshot()
    }

    override fun onBackPressed() {
        backToHome()
    }

    private fun initView() {

        if (mPpobPayment.ppobProductType?.type.equals(PpobProductType.PRABAYAR) ||
            mPpobPayment.ppobProductType?.type.equals(PpobProductType.PASCABAYAR)
        ) {
            tvNote.visible()
        }

        imgClose.setOnClickListener {
            backToHome()
        }

        imgShare.setOnClickListener {
            callApiGetContactReceipt(true)

        }

        btnDetail.setOnClickListener {
            callApiGetContactReceipt(false)
            //  gotoTransactionDetail(false)

        }
    }

    private fun initRecyclerView() {
        recyclerview.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        recyclerview.setLayoutManager(linearLayoutManager)
    }

    private fun displayPaymentInfo() {

        if (mPpobPayment.ppobProductType?.type == PpobProductType.REFUND) {
            tvStatus.text = getString(R.string.message_refund_success)
            tvTotalPaymentLabel.text = getString(R.string.text_refund_amount)
        }

        if (intent.hasExtra("tokoonline")) {
            tvSNK.text = intent.getStringExtra("tokoonline")
            tvSNK.visible()
        }

        if (mPpobPayment.ppobProductType?.isOldApi == true) {
            tvAmount.text = DataUtil.convertCurrency(mPpobInquiryOldResponse.amount)
        } else {
            var komisi: Long = 0

            if (mPpobPayment.komisi != null) {
                komisi = mPpobPayment.komisi!!
            }

            if ((mPpobPayment.ppobProductType?.type == PpobProductType.DONASI)
                || (mPpobPayment.ppobProductType?.type == PpobProductType.REFUND)
                || (mPpobPayment.ppobProductType?.type == PpobProductType.TRANSFER_BANK)
            ) {
                tvAmount.text = DataUtil.convertCurrency(mPpobPayment.totalPayment)
            } else if (mPpobPayment.ppobProductType?.type == PpobProductType.QR_PAYMENT) {
                var total = mPpobPayment.totalPayment?.plus(komisi.toLong())
                tvAmount.text = DataUtil.convertCurrency(total)

                if (total == 0.0) {
                    amountLayout.gone()
                }
            } else {
                mPpobInquiryResponse.let {
                    it.data.let {
                        var total = DataUtil.getLong(it.total) + komisi
                        tvAmount.text = DataUtil.convertCurrency(total)
                    }
                }
            }
        }
    }

    private fun displayPaymentDetail(keyValueList: List<WidgetBuilderModel>) {
        var displayList = mutableListOf<WidgetBuilderModel>()

        var i = 0
        for (widgetBuilderModel in keyValueList) {
            if (!widgetBuilderModel.key.equals(getString(R.string.text_comission), true)) {
                if (i < 4) {
                    displayList.add(widgetBuilderModel)
                }
                i++
            }
        }

        val adapter = PpobPaymentDetailAdapter(this, displayList)
        recyclerview.adapter = adapter
    }

    private fun gotoTransactionDetail(isShare: Boolean) {
        val intent = Intent(this, PpobPaymentDetailActivity::class.java)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, mPpobPayment)
        intent.putExtra(
            AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA,
            Gson().toJson(mPpobOttoagPaymentResponseModel)
        )

        intent.putExtra(PpobPaymentDetailActivity.KEY_IS_SHARE, isShare)
        intent.putExtra(PpobPaymentDetailActivity.KEY_IS_FROM_RECEIPT, false)
        intent.putExtra(
            AppKeys.KEY_CONTACT_PAYMENT_SUCCESS_DATA,
            Gson().toJson(mAddresContact)
        )

        startActivity(intent)
    }

    private fun backToHome() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun playNotificationTone() {
        val mPlayer = MediaPlayer.create(this, R.raw.ottopay_notification_ringtone)

        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        when (am.ringerMode) {
            AudioManager.RINGER_MODE_SILENT -> Log.i(TAG, "Silent mode")
            AudioManager.RINGER_MODE_VIBRATE -> Log.i(TAG, "Vibrate mode")
            AudioManager.RINGER_MODE_NORMAL -> {
                Log.i(TAG, "Normal mode")
                mPlayer?.start()
            }
        }
    }

    fun callApiGetContactReceipt(isShare: Boolean) {

        showProgressDialog(R.string.loading_message)

        OttoKonekAPI.getContactReceipt(this, object : ApiCallback<ReceiptAdressResponse?>() {

            override fun onResponseSuccess(body: ReceiptAdressResponse?) {
                dismissProgressDialog()


                if (isSuccess200) {
                    body?.let {
                        mAddresContact = it
                    }

                    if (!isShare) {
                        gotoTransactionDetail(false)
                    } else {
                        gotoTransactionDetail(true)
                    }


                } else {
                    showErrorDialog(body?.meta?.message)
                }

            }

            override fun onApiServiceFailed(throwable: Throwable) {
                dismissProgressDialog()
                onApiResponseError()


            }


        });


    }

}