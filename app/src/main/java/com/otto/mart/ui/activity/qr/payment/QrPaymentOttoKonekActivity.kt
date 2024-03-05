package com.otto.mart.ui.activity.qr.payment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.PaymentData
import com.otto.mart.model.APIModel.Request.PayQrInquiryRequestModel
import com.otto.mart.model.APIModel.Request.multibank.ValidateQrRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.model.APIModel.Response.balance.OttoKonekBalanceResponse
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.model.APIModel.Response.multibank.ValidateQrResponse
import com.otto.mart.model.APIModel.Response.qr.QrInquiryResponse
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.presenter.dao.TransactionDao
import com.otto.mart.support.util.*
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.cashOut.CashOutSelectAmountActivity
import com.otto.mart.ui.activity.multibank.*
import com.otto.mart.ui.activity.ppob.PpobPaymentSuccessActivity
import com.otto.mart.ui.activity.qr.DetailInquiryQrActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.activity.transaction.ProcessQRPaymentActivity
import com.otto.mart.ui.activity.transaction.withdraw.WithdrawActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.button_select_bank.*
import kotlinx.android.synthetic.main.content_cash_out_select_amount.*
import kotlinx.android.synthetic.main.content_qr_payment_otto_konek.*
import kotlinx.android.synthetic.main.content_qr_payment_otto_konek.btnNext
import kotlinx.android.synthetic.main.content_qr_payment_otto_konek.etAmount
import kotlinx.android.synthetic.main.content_qr_payment_otto_konek.tvAmountError
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class QrPaymentOttoKonekActivity : AppActivity() {

    companion object {
        val KEY_REQUEST_BANK = 300
        val KEY_BANK_DATA_LIST = "bank_data_list"
    }

    val API_KEY_REVENUE = 100
    val API_KEY_QR_INQUIRY = 102
    val API_KEY_QR_INQUIRY_VALIDATE = 101
    val API_KEY_QR_PAYMENT = 102


    private var mQRContent = ""
    var mPin: String = ""
    private var mPayableAmount: Double = 0.0
    private var mRrn: String = ""
    private var mQrInquiryResponseValidate: ValidateQrResponse? = null
    private var mQrInquiryResponse: QrInquiryResponse? = null
    private var mIsValidationEnable = false
    val presenter: ListLinkedBankAccountPresenter = ListLinkedBankAccountPresenterImpl(this)
    var accountNumber: String? = null
    var bin: String? = null
    var binFromList: String? = null
    var mTextWatcher: TextWatcher? = null
    var mAccountSelected: AccountListResponse.Data? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_payment_otto_konek)

        if (intent.hasExtra(ProcessQRPaymentActivity.KEY_QR_CONTENT)) {
            mQRContent = intent?.getStringExtra(ProcessQRPaymentActivity.KEY_QR_CONTENT)!!
        }

        initView()
        if (mQRContent != "") {
            val requestModel = ValidateQrRequest()
            requestModel.qrData = mQRContent
            callQrValidate(requestModel)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == AppKeys.KEY_INPUT_PIN) {

                if (data != null) {
                    mPin = data?.getStringExtra(AppKeys.KEY_PIN_CODE)!!
                    //callQrPayment()
                }
            }
            if (requestCode == KEY_REQUEST_BANK) {

                val amin = data?.getStringExtra(KEY_BANK_DATA_LIST)
                data?.let {
                    if (it.hasExtra(KEY_BANK_DATA_LIST)) {
                        mAccountSelected = Gson().fromJson(
                            it.getStringExtra(KEY_BANK_DATA_LIST),
                            AccountListResponse.Data::class.java
                        )

                        bin = mAccountSelected?.bin
                        accountNumber = mAccountSelected?.accountNumber

                        displayAccountList(mAccountSelected)
                    }
                }

//                data?.getIntExtra(KEY_BANK_DATA_LIST, -1).let {
//
//
//                    displayAccountList(
//                        mSelectedBank.data.,
//                        presenter.getDataAccount().value?.get(it!!)?.object1?.accountNumber,
//                        presenter.getDataAccount().value?.get(it!!)?.object2?.formatedBalance,
//                        presenter.getDataAccount().value?.get(it!!)?.object1?.logo
//                    )
//
//                    accountNumber =
//                        presenter.getDataAccount().value?.get(it!!)?.object1?.accountNumber
//                    bin = presenter.getDataAccount().value?.get(it!!)?.object1?.bin
//                }
            }
        }
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_qr_payment_otto_konek)

        etAmount.requestFocus()


        mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var amount = DataUtil.InputDecimal(charSequence.toString())

                etAmount.removeTextChangedListener(mTextWatcher)

                if (amount.equals("")) {
                    etAmount.setText("0")
                } else {
                    etAmount.setText(amount)
                    etAmount.setSelection(etAmount.length())
                }

                etAmount.addTextChangedListener(mTextWatcher)
                mIsValidationEnable = true

               // isValidAmount()
            }

            override fun afterTextChanged(editable: Editable) {}
        }

        etAmount.addTextChangedListener(mTextWatcher)

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }


        btnNext.setOnClickListener {
            mIsValidationEnable = true
            if (isValidAmount()) {

                val requestModel = PayQrInquiryRequestModel()
                requestModel.qrData = mQRContent
                requestModel.bin = bin
                requestModel.accountNumber = accountNumber
                callInquiryQr(requestModel)

            }
        }


        bankSelectLayout.setOnClickListener {
            gotoSelectBank()
        }
        btn_select_account.setOnClickListener {
            gotoSelectBank()
        }
    }

    private fun gotoSelectBank() {

        val intent = Intent(this, SelectAccountQrActivity::class.java)
        intent.putExtra("binFromList", binFromList)
        startActivityForResult(intent, KEY_REQUEST_BANK)
//        val intent = Intent(this, SelectAccountActivityForWithdraw::class.java)
//        intent.putExtra("dataAccount", Gson().toJson(mQrInquiryResponseValidate))
//        startActivityForResult(intent, CashOutSelectAmountActivity.KEY_REQUEST_BANK)

    }

    private fun inputPin() {
        val intent = Intent(this, RegisterPinResetActivity::class.java)
        intent.putExtra(AppKeys.KEY_INPUT_PIN_TYPE_INPUT, true)
        startActivityForResult(intent, AppKeys.KEY_INPUT_PIN)
    }

    fun isValidAmount(): Boolean {
        var amount = DataUtil.getLong(etAmount.text.toString())
        var binValidation = bin
        tvAmountError.gone()

        var status = true

        if (amount < 1) {
            status = false
            tvAmountError.text = "Minimum payment is PHP 1"
            tvAmountError.visible()
        } else if (accountNumber == null) {
            status = false
            "Select Your account!".showToast(this)
        }

        return status
    }

    private fun qrInquirySuccessValidate(qrValidateInquiryResponse: ValidateQrResponse) {
        mQrInquiryResponseValidate = qrValidateInquiryResponse


        if (qrValidateInquiryResponse.data.merchantInfo.transactionAmount == 0 ){

            binFromList = qrValidateInquiryResponse.data.issuerLinkedList[0].bin
            tvStoreName.text = qrValidateInquiryResponse.data.merchantInfo.merchantName

        }else{

            etAmount.isEnabled = false
            etAmount.setText(qrValidateInquiryResponse.data.merchantInfo.transactionAmount.toString())
            binFromList = qrValidateInquiryResponse.data.issuerLinkedList[0].bin
            tvStoreName.text = qrValidateInquiryResponse.data.merchantInfo.merchantName
        }
    }




    private fun qrPaymentSuccess(basePaymentResponseModel: BasePaymentResponseModel) {
        var ppobPaymentType = PpobProductType()
        ppobPaymentType.type = PpobProductType.QR_PAYMENT

        var ppobPayment = PpobPayment()
        ppobPayment.productName = "QR Payment"
        ppobPayment.ppobProductType = ppobPaymentType

        if (mPayableAmount == 0.0) {
            ppobPayment.totalPayment = DataUtil.FormattedCurrencyToDouble(etAmount.text.toString())
        } else {
            ppobPayment.totalPayment = mPayableAmount
        }


        var ppobOttoagPaymentResponseModel = PpobOttoagPaymentResponseModel()
        var data = PaymentData()
        data.keyValueList = basePaymentResponseModel.data.keyValueList
        ppobOttoagPaymentResponseModel.data = data

        val intent = Intent(this, PpobPaymentSuccessActivity::class.java)
        intent.putExtra(
            "merchantName",
            mQrInquiryResponseValidate?.data?.merchantInfo?.merchantName
        )
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, ppobPayment)
        intent.putExtra(
            AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA,
            Gson().toJson(ppobOttoagPaymentResponseModel)
        )
        startActivity(intent)
    }

    private fun getRevenue() {
        TransactionDao(this).revenue(BaseDao.getInstance(this, API_KEY_REVENUE).callback)
    }

    private fun callQrValidate(requestModel: ValidateQrRequest) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()
        TransactionDao(this).payQrValidate(
            requestModel,
            BaseDao.getInstance(this, API_KEY_QR_INQUIRY_VALIDATE).callback
        )
    }

    fun callInquiryQr(requestModel: PayQrInquiryRequestModel) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()
        TransactionDao(this).payQrQrisInquiry(
            requestModel,
            BaseDao.getInstance(this, API_KEY_QR_INQUIRY).callback
        )


    }

    //yang ini di update
    private fun qrInquirySuccess(qrInquiryResponse: QrInquiryResponse) {
        mQrInquiryResponse = qrInquiryResponse


//        if (qrInquiryResponse.data.transactionAmount != 0.0) {
//            etAmount.isEnabled = false
//            etAmount.setText(DataUtil.InputDecimal(qrInquiryResponse.data.transactionAmount.toString()))
//        }

      //  tvStoreName.text = qrInquiryResponse.data.merchantName

        val intent = Intent(this, DetailInquiryQrActivity::class.java)
        intent.putExtra(AppKeys.KEY_INQUIRY_QR, Gson().toJson(qrInquiryResponse))
        intent.putExtra("dataBank",Gson().toJson(mAccountSelected))
        intent.putExtra("amount",etAmount.text.toString())
        startActivity(intent)
    }

//    private fun callQrPayment() {
//        btnNext.isEnabled = false
//        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
//            .show()
//
//        val requestModel = QrPaymentRequest()
//
//        mQrInquiryResponse?.let {
//            requestModel.requestId = it.data.merchantInfo?.requestId
//            requestModel.tipFeeFixed = it.data.merchantInfo?.tipFeeFixed
//            requestModel.accountNumber = accountNumber
//            requestModel.bin = bin
//
//            if (it.data.merchantInfo?.transactionAmount == 0.0) {
//                requestModel.transactionAmount =
//                    DataUtil.FormattedCurrencyToDouble(etAmount.text.toString())
//            } else {
//                requestModel.transactionAmount = it.data.merchantInfo?.transactionAmount
//            }
//        }
//
//        TransactionDao(this).payQrPayment(
//            requestModel,
//            BaseDao.getInstance(this, API_KEY_QR_PAYMENT).callback
//        )
//    }

    override fun onApiResponseCallback(
        br: BaseResponse?,
        responseCode: Int,
        response: Response<*>?
    ) {
        ProgressDialogComponent.dismissProgressDialog(this)
        validateApiResponse(br)
        super.onApiResponseCallback(br, responseCode, response)
        when (responseCode) {
            API_KEY_REVENUE ->
                if ((br as BaseResponseModel).meta.code == 200) {
                    val ottoKonekBalanceResponse = br as OttoKonekBalanceResponse?
                    if (ottoKonekBalanceResponse != null) {

                    }
                }
            API_KEY_QR_INQUIRY_VALIDATE ->
                if ((br as BaseResponseModel).meta.code == 200) {

                    qrInquirySuccessValidate(br as ValidateQrResponse)
                } else {
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.setOnDismissListener {
                        finish()
                    }
                    if (!this.isFinishing) {
                        dialog.show()
                    }
                }
            API_KEY_QR_INQUIRY ->
                if ((br as BaseResponseModel).meta.code == 200) {

                    qrInquirySuccess(br as QrInquiryResponse)
                } else {
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.setOnDismissListener {
                        finish()
                    }
                    if (!this.isFinishing) {
                        dialog.show()
                    }
                }
            API_KEY_QR_PAYMENT -> {
                if ((br as BaseResponseModel).meta.code == 200) {
                    qrPaymentSuccess((br as BasePaymentResponseModel))
                } else {
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.setOnDismissListener {
                        finish()
                    }
                    if (!this.isFinishing) {
                        dialog.show()
                    }
                }
                btnNext.isEnabled = true
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        onApiResponseError()
    }


    private fun displayAccountList(
        bank: AccountListResponse.Data?
    ) {
        mAccountSelected = bank

        Log.d("hasilnyaa", bank?.accountName.toString() + " ajsndjkasn")

        if (mAccountSelected != null) {

            tvBankName.text = bank?.accountName
            tvBankOwnerName.text = DataUtil.setFormatAccountNumber(bank?.accountNumber)
            Glide.with(iv_logo_bank)
                .load(bank?.logo)
                .fitCenter()
                .into(iv_logo_bank)
            bankSelectLayout.gone()
            bankSelectedLayout.visible()
            //  isValidInput
        } else {
            bankSelectLayout.visible()
            bankSelectedLayout.gone()
        }
    }
}