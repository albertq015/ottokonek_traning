package com.otto.mart.ui.activity.multibank

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
import com.otto.mart.model.APIModel.Request.multibank.TransferMultiBankConfrimRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.model.APIModel.Response.multibank.TransferBankInquiryMultiBankResponse
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.presenter.dao.TransactionDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.DataUtil
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.ppob.PpobPaymentSuccessActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.activity_detail_confrim_transfer.*
import kotlinx.android.synthetic.main.activity_detail_confrim_transfer.btnSubmit
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class DetailConfirmationTransferActivity : AppActivity() {


    var dataResponse = TransferBankInquiryMultiBankResponse()

    var accountName: String? = null
    var accountNumber: String? = null

    // var payment: Int = 0
    var adminFee: Long? = 0
    var payment: Long? = 0
    var totalPayment: Long? = 0
    var mBin: String? = null
    var mPin: String = ""
    val API_KEY_TRANSFER_PAYMENT = 102
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_confrim_transfer)


        if (intent.hasExtra("data")) {
            dataResponse = Gson().fromJson(
                intent?.getStringExtra("data"),
                TransferBankInquiryMultiBankResponse::class.java
            )

            accountName = intent?.getStringExtra("accountName")
            accountNumber = intent?.getStringExtra("accountNumber")
            mBin = intent?.getStringExtra("bin")
        }

        init()
    }

    fun init() {

        imgToolbarLeft.setOnClickListener {
            onBackPressed()
        }
        tvToolbarTitle.text = "Confirmation"


        payment = dataResponse.data?.transactionAmount?.toLong()
        adminFee = dataResponse.data?.adminFee?.toLong()
        totalPayment = payment?.plus(adminFee!!)


        tvFromAccountBank.text =
            accountName + " - " + DataUtil.setFormatAccountNumber(accountNumber)
        tvAccountHolderName.text = dataResponse.data?.destinationAccountName
        tvTransactionFee.text = dataResponse.data?.adminFee.toString()
        tvTransferAmount.text = dataResponse.data?.transactionAmount.toString()
        tvToBankAccount.text =
            dataResponse.data?.destinationAccountBank + " - " + DataUtil.setFormatAccountNumber(
                dataResponse.data?.destinationAccountNumber
            )
        tvTotalPayment.text = DataUtil.convertCurrency(totalPayment)

        btnSubmit.setOnClickListener {
            goToPin()
            // callApiConfrim()
        }

        //  tvTotalPayment.text = dataResponse.data?.adminFee.toString().toInt() + dataResponse.data?.transactionAmount.toString().toInt()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppKeys.KEY_INPUT_PIN) {
                if (data != null) {
                    mPin = data?.getStringExtra(AppKeys.KEY_PIN_CODE)!!
                    callApiConfrim()
                }
            }
        }
    }

    fun goToPin() {
        val intent =
            Intent(this@DetailConfirmationTransferActivity, RegisterPinResetActivity::class.java)
        intent.putExtra(AppKeys.KEY_CONFRIM_TRANSFER, true)
        intent.putExtra("bin", mBin)
        startActivityForResult(intent, AppKeys.KEY_INPUT_PIN)
    }

    fun callApiConfrim() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()


        val request = TransferMultiBankConfrimRequest()
        request.bin = mBin
        request.sourceAccountNumber = accountNumber

        TransactionDao(this).TransferBank(
            request,
            BaseDao.getInstance(this, API_KEY_TRANSFER_PAYMENT).callback
        )

//        OttoKonekAPI.confrimTransferBank(
//            this,
//            request,
//            object : ApiCallback<TransferMultiBankConfrimResponse>() {
//                override fun onResponseSuccess(body: TransferMultiBankConfrimResponse?) {
//
//                }
//
//                override fun onApiServiceFailed(throwable: Throwable?) {
//
//                }
//
//            })
    }

    override fun onApiResponseCallback(
        br: BaseResponse?,
        responseCode: Int,
        response: Response<*>?
    ) {
        ProgressDialogComponent.dismissProgressDialog(this)
        validateApiResponse(br)
        super.onApiResponseCallback(br, responseCode, response)

        when (responseCode) {
            API_KEY_TRANSFER_PAYMENT ->
                if ((br as BaseResponseModel).meta.code == 200) {
                    PaymentSuccess((br as BasePaymentResponseModel))
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
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        onApiResponseError()
    }


    private fun PaymentSuccess(basePaymentResponseModel: BasePaymentResponseModel) {
        var ppobPaymentType = PpobProductType()
        ppobPaymentType.type = PpobProductType.QR_PAYMENT

        var ppobPayment = PpobPayment()
        ppobPayment.productName = "QR Payment"
        ppobPayment.ppobProductType = ppobPaymentType


        var ppobOttoagPaymentResponseModel = PpobOttoagPaymentResponseModel()
        var data = PaymentData()
        data.keyValueList = basePaymentResponseModel.data.keyValueList
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