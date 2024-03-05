package com.otto.mart.ui.activity.qr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.PaymentData
import com.otto.mart.model.APIModel.Request.qr.QrPaymentRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.model.APIModel.Response.qr.QrInquiryResponse
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.presenter.dao.TransactionDao
import com.otto.mart.support.util.DataUtil
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.ppob.PpobPaymentSuccessActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.content_detail_inquiry.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response


class DetailInquiryQrActivity : AppActivity() {

    var mQrResponse: QrInquiryResponse? = null
    var mAccountSelected: AccountListResponse.Data? = null
    var amount: String = ""
    val API_KEY_RESPONSE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_qr_inquiry)

        if (intent.hasExtra(AppKeys.KEY_INQUIRY_QR)) {
            mQrResponse = Gson().fromJson(
                intent.getStringExtra(AppKeys.KEY_INQUIRY_QR),
                QrInquiryResponse::class.java
            )
        }

        if (intent.hasExtra("dataBank")) {
            mAccountSelected = Gson().fromJson(
                intent.getStringExtra("dataBank"),
                AccountListResponse.Data::class.java
            )
        }

        if (intent.hasExtra("amount")) {
            amount = intent.getStringExtra("amount").toString()
        }

        init()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppKeys.KEY_INPUT_PIN) {
                val requestModel = QrPaymentRequest()
                // val requestModel = PayQrInquiryRequestModel()
                requestModel.accountNumber = mAccountSelected?.accountNumber
                requestModel.bin = mAccountSelected?.bin
                requestModel.requestId = mQrResponse?.data?.requestId
                requestModel.tipFeeFixed = 0  //mQrResponse?.data?.adminFee
                requestModel.transactionAmount = amount.replace(",","").toDouble()
                callQrPayment(requestModel)
            }
        }
    }

    fun init() {
        tvToolbarTitle.text = "Confirmation"
        tv_merchant_name.text = mQrResponse?.data?.merchantName
        tv_account.text =
            mAccountSelected?.binName + " - " + DataUtil.setFormatAccountNumber(mAccountSelected?.accountNumber)
        tvAmount.text = amount
        tvAdminFee.text = mQrResponse?.data?.adminFee.toString()
        tvTotal.text = amount

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }
        btnSubmit.setOnClickListener {
            inputPin()
        }
    }

    private fun inputPin() {
        val intent = Intent(this, RegisterPinResetActivity::class.java)
        intent.putExtra(AppKeys.KEY_INPUT_PIN_TYPE_INPUT_QR_CONFRIM, true)
        startActivityForResult(intent, AppKeys.KEY_INPUT_PIN)
    }

    private fun callQrPayment(requestModel: QrPaymentRequest?) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        TransactionDao(this).payQrPaymentNew(
            requestModel,
            BaseDao.getInstance(this, API_KEY_RESPONSE).callback
        )
    }

    override fun onApiResponseCallback(
        br: BaseResponse?,
        responseCode: Int,
        response: Response<*>?
    ) {
        super.onApiResponseCallback(br, responseCode, response)
        if ((br as BaseResponseModel).meta.code == 200) {
            succesQrPayment((br as BasePaymentResponseModel))
        } else {
            val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
            dialog.setOnDismissListener {
                finish()
            }
            if (!this.isFinishing) {
                dialog.show()
            }
        }

    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        super.onApiFailureCallback(message, ac)
        onApiResponseError()
    }

    fun succesQrPayment(basePayment: BasePaymentResponseModel) {

        var ppobPaymentType = PpobProductType()
        ppobPaymentType.type = PpobProductType.QR_PAYMENT

        var ppobPayment = PpobPayment()
        ppobPayment.productName = "QR Payment"
        ppobPayment.ppobProductType = ppobPaymentType


        var ppobOttoagPaymentResponseModel = PpobOttoagPaymentResponseModel()
        var data = PaymentData()
        data.keyValueList = basePayment.data.keyValueList
        ppobOttoagPaymentResponseModel.data = data

        val intent = Intent(this, PpobPaymentSuccessActivity::class.java)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, ppobPayment)
        intent.putExtra(
            AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA,
            Gson().toJson(ppobOttoagPaymentResponseModel)
        )
        startActivity(intent)
    }
}