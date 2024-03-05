package com.otto.mart.ui.activity.transaction.refund

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.PaymentData
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel
import com.otto.mart.model.APIModel.Request.refund.MerchantRefundRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.model.APIModel.Response.refund.MerchantRefundResponse
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.ApiCallback
import com.otto.mart.support.util.CryptoUtil
import com.otto.mart.support.util.DataUtil
import com.otto.mart.ui.Partials.adapter.ppob.PpobPaymentDetailAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.ppob.PpobPaymentSuccessActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.content_ppob_confirmation.*
import kotlinx.android.synthetic.main.content_ppob_product_input.btnNext
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class RefundConfirrmationActivity : AppActivity() {

    companion object {
        val KEY_RRN = "rrn"
    }

    val API_KEY_MERCHANT_REFUND = 100

    var mConfirmationDataList = mutableListOf<WidgetBuilderModel>()

    var mPpobOttoagPaymentResponseModel: PpobOttoagPaymentResponseModel? = null
    var mRrn = ""
    var mPin = ""
    var mTotal = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refund_confirrmation)

        if (intent.hasExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA)) {
            mPpobOttoagPaymentResponseModel = Gson().fromJson(intent.getStringExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA), PpobOttoagPaymentResponseModel::class.java)
        }

        if (intent.hasExtra(KEY_RRN)) {
            mRrn = intent?.getStringExtra(KEY_RRN)!!
        }

        initView()
        initRecyclerview()
        displayTransactionInfo()
    }

    private fun displayTransactionInfo() {
        var date = ""
        mTotal = "Rp 0"

        mPpobOttoagPaymentResponseModel?.let {
            for (widgetBuilderModel in it.data.keyValueList) {
                if (widgetBuilderModel.key.equals("tanggal", true)) {
                    date = widgetBuilderModel.value
                }

                if (widgetBuilderModel.key.equals("harga", true)) {
                    mTotal = widgetBuilderModel.value
                }
            }
        }

        var phone = WidgetBuilderModel()
        phone.key = "Nomor HP"
        phone.value = SessionManager.getPhone()
        mConfirmationDataList.add(phone)

        var issuer = WidgetBuilderModel()
        issuer.key = "Issuer"
        issuer.value = "OTTOPAY"
        mConfirmationDataList.add(issuer)

        var noReff = WidgetBuilderModel()
        noReff.key = "No Ref Transaksi"
        noReff.value = mRrn
        mConfirmationDataList.add(noReff)

        var dateTrx = WidgetBuilderModel()
        dateTrx.key = "Tanggal Transaksi"
        dateTrx.value = date
        mConfirmationDataList.add(dateTrx)

        var totalAmount = WidgetBuilderModel()
        totalAmount.key = "Nominal Pengembalian Dana"
        totalAmount.value = mTotal
        mConfirmationDataList.add(totalAmount)

        val adapter = PpobPaymentDetailAdapter(this, mConfirmationDataList)
        adapter.orientation = PpobPaymentDetailAdapter.VERTICAL
        recyclerview.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppKeys.KEY_INPUT_PIN) {
                if (data != null) {
                    mPin = data?.getStringExtra(AppKeys.KEY_PIN_CODE)!!
                    merchantRefund()
                }
            }
        }
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_refund_confirrmation)

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        btnNext.setOnClickListener {
            inputPin()
        }
    }

    private fun initRecyclerview() {
        recyclerview.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        recyclerview.setLayoutManager(linearLayoutManager)
    }

    private fun inputPin() {
        val intent = Intent(this, RegisterPinResetActivity::class.java)
        intent.putExtra(AppKeys.KEY_INPUT_PIN_TYPE_INPUT, true)
        startActivityForResult(intent, AppKeys.KEY_INPUT_PIN)
    }

    private fun merchantRefundSuccess(merchantRefundResponse: MerchantRefundResponse) {
        var ppobPaymentType = PpobProductType()
        ppobPaymentType.type = PpobProductType.REFUND

        var ppobPayment = PpobPayment()
        ppobPayment.ppobProductType = ppobPaymentType
        ppobPayment.productName = "Pengembalian Dana"
        ppobPayment.ppobProductType = ppobPaymentType
        ppobPayment.totalPayment = DataUtil.getLong(mTotal).toDouble()

        var ppobOttoagPaymentResponseModel = PpobOttoagPaymentResponseModel()
        var data = PaymentData()
        data.keyValueList = merchantRefundResponse.data.key_value_list
        ppobOttoagPaymentResponseModel.data = data

        val intent = Intent(this, PpobPaymentSuccessActivity::class.java)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, ppobPayment)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA, Gson().toJson(ppobOttoagPaymentResponseModel))
        startActivity(intent)
    }

    //region API Call

    private fun merchantRefund() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

        var merchantRefundRequest = MerchantRefundRequest()
        merchantRefundRequest.rrn = CryptoUtil.encryptRSA(mRrn.toByteArray())

//        RefundDao(this).merchantRefund(merchantRefundRequest, BaseDao.getInstance(this, API_KEY_MERCHANT_REFUND).callback)

        OttoKonekAPI.qrRefund(this, merchantRefundRequest, object : ApiCallback<MerchantRefundResponse>() {
            override fun onResponseSuccess(body: MerchantRefundResponse?) {
                if (!isFinishing) ProgressDialogComponent.dismissProgressDialog(this@RefundConfirrmationActivity)
                body?.let {
                    if (isSuccess200) {
                        merchantRefundSuccess(it)
                    } else {
                        val dialog = ErrorDialog(this@RefundConfirrmationActivity, this@RefundConfirrmationActivity, false, false, it.meta?.message, "")
                        dialog.setOnDismissListener {
                            finish()
                        }
                        dialog.show()
                    }
                }
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                if (!isFinishing) ProgressDialogComponent.dismissProgressDialog(this@RefundConfirrmationActivity)
                onApiResponseError()
            }
        })
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        ProgressDialogComponent.dismissProgressDialog(this)
        when (responseCode) {
            API_KEY_MERCHANT_REFUND ->
                if ((br as BaseResponseModel).meta.code == 200) {
                    val merchantRefundResponse = br as MerchantRefundResponse?
                    merchantRefundResponse?.let { merchantRefundSuccess(it) }
                } else {
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.setOnDismissListener {
                        finish()
                    }
                    dialog.show()
                }
        }
    }

    override fun onApiFailureCallback(message: String, ac: BaseActivity) {
        onApiResponseError()
    }

    //endregion API Call
}
