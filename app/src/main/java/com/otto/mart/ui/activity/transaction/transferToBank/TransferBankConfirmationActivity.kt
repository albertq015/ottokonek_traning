package com.otto.mart.ui.activity.transaction.transferToBank

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.PaymentData
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel
import com.otto.mart.model.APIModel.Request.OmzetSaldoRequestModel
import com.otto.mart.model.APIModel.Request.TransferToBankConfirmationRequestModel
import com.otto.mart.model.APIModel.Request.multibank.WithdrawRequest
import com.otto.mart.model.APIModel.Request.revenue.WithdrawRevenueRequest
import com.otto.mart.model.APIModel.Request.transfer.TransferBankPaymentRequest
import com.otto.mart.model.APIModel.Request.transfer.TransferSknRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.OmzetSaldoResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.model.APIModel.Response.TransferBankInquiryResponseModel
import com.otto.mart.model.APIModel.Response.multibank.TransferMultiBankConfrimResponse
import com.otto.mart.model.APIModel.Response.revenue.WithdrawRevenueResponse
import com.otto.mart.model.APIModel.Response.transfer.TransferBankInquiryResponse
import com.otto.mart.model.localmodel.omzet.WithdrawMethod
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.presenter.dao.RevenueDao
import com.otto.mart.presenter.dao.TransactionDao
import com.otto.mart.presenter.dao.WalletDao
import com.otto.mart.presenter.dao.deposit.TransferDepositDao
import com.otto.mart.presenter.dao.transfer.TransferBankDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.*
import com.otto.mart.ui.Partials.adapter.ppob.PpobPaymentDetailAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.ppob.PpobPaymentSuccessActivity
import com.otto.mart.ui.activity.ppob.setup.PpobProductTypeSetup
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.content_ppob_confirmation.recyclerview
import kotlinx.android.synthetic.main.content_transfer_bank_confirmation.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class TransferBankConfirmationActivity : AppActivity() {

    companion object {
        val KEY_DATA_INQUIRY = "data_inquiry"
        val KEY_DATA_BANK = "data_bank"
        val KEY_DATA_AMOUNT = "data_amount"
        val KEY_WITTHDRAW_METHOD = "withdraw_method"
        val KEY_TRANSACTION_TYPE = "transaction_type"
        val KEY_WITHDRAW_REVENUE_INQUIRY_DATA = "withdraw_revenue_inquiry_data"
        val KEY_NAMA_BANK = "data_name"
        val KEY_NUMBER_BANK = "data_number"
        val KEY_BIN_BANK = "data_bin"
    }

    enum class TRANSACTION_TYPE {
        AJ,
        OMZET,
        SKN,
        DEPOSIT,
        REVENUE,
        TRANSFER
    }

    var mTransactionType: TRANSACTION_TYPE? = null

    var KEY_API_TRANSFER_BANK_PAYMENT = 102
    var KEY_API_TRANSFER_BANK_AJ_PAYMENT = 103
    var KEY_API_TRANSFER_BANK_AJ_CHECK_STATUS = 104
    val KEY_TRANSFER_OMZET: Int = 105
    val KEY_API_TRANSFER_BANK_SKN: Int = 106
    var KEY_API_TRANSFER_DEPOSIT_AJ_PAYMENT = 107
    var KEY_API_TRANSFER_DEPOSIT_AJ_CHECK_STATUS = 108
    var KEY_API_WITHDRAW_REVENUE_PAYMENT = 109

    private var mTransferBankInquiryResponseModel = TransferBankInquiryResponseModel()
    private var mTransferBankInquiryResponse = TransferBankInquiryResponse()
    var mBankSelected: BankAccountModel? = null

    var mAmount: Long = 0
    var mWithdrawAmount: Double = 0.0
    var mPin: String = ""

    var withdrawMethod: WithdrawMethod? = null
    var mAccountName: String? = null
    var mAccountNumber: String? = null
    var mBin: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_bank_confirmation)
        initData()
        initView()
        initRecyclerView()
        displayConfirmatipnData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppKeys.KEY_INPUT_PIN) {
                if (data != null) {
                    mPin = data?.getStringExtra(AppKeys.KEY_PIN_CODE)!!
                    when (mTransactionType) {
                        TRANSACTION_TYPE.AJ -> {
                            callAjPayment(mTransferBankInquiryResponse, mPin)
                        }
                        TRANSACTION_TYPE.OMZET -> {
                            callTransferOmzet(mPin)
                        }
                        TRANSACTION_TYPE.SKN -> {
                            callTransferSkn(mPin)
                        }
                        TRANSACTION_TYPE.DEPOSIT -> {
                            callDepositAjPayment(mTransferBankInquiryResponse, mPin)
                        }
                        TRANSACTION_TYPE.REVENUE -> {
                            callApiConfrim(mPin)
                           // callWithdrawRevenuePayment(mPin)
                        }
                        TRANSACTION_TYPE.TRANSFER -> callOttokonekPayment(
                            mTransferBankInquiryResponse,
                            mPin
                        )
                        else -> {
                            callPayment(mTransferBankInquiryResponseModel)
                        }
                    }
                }
            }
        }
    }

    private fun initData() {
        if (intent.hasExtra(KEY_TRANSACTION_TYPE)) {
            mTransactionType =
                intent.getSerializableExtra(KEY_TRANSACTION_TYPE) as TRANSACTION_TYPE?
        }

        if (intent.hasExtra(KEY_NUMBER_BANK) || intent.hasExtra(KEY_NAMA_BANK)) {
            mAccountName = intent.getStringExtra(KEY_NAMA_BANK)
            mAccountNumber = intent?.getStringExtra(KEY_NUMBER_BANK)
            mBin = intent?.getStringExtra(KEY_BIN_BANK)

        }

        if (intent.hasExtra(KEY_DATA_INQUIRY)) {
            if (mTransactionType == TRANSACTION_TYPE.AJ || mTransactionType == TRANSACTION_TYPE.DEPOSIT || mTransactionType == TRANSACTION_TYPE.TRANSFER) {
                mTransferBankInquiryResponse = Gson().fromJson(
                    intent.getStringExtra(KEY_DATA_INQUIRY),
                    TransferBankInquiryResponse::class.java
                )
            } else {
                mTransferBankInquiryResponseModel = Gson().fromJson(
                    intent.getStringExtra(KEY_DATA_INQUIRY),
                    TransferBankInquiryResponseModel::class.java
                )
            }
        }

        if (intent.hasExtra(KEY_DATA_BANK)) {
            mBankSelected =
                Gson().fromJson(intent.getStringExtra(KEY_DATA_BANK), BankAccountModel::class.java)
        }

        if (intent.hasExtra(KEY_WITTHDRAW_METHOD)) {
            withdrawMethod = Gson().fromJson(
                intent.getStringExtra(KEY_WITTHDRAW_METHOD),
                WithdrawMethod::class.java
            )
        }
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_transfer_bank_confirmation)

        if (intent.hasExtra(KEY_DATA_AMOUNT)) {
            mWithdrawAmount = intent.getDoubleExtra(KEY_DATA_AMOUNT, 0.0)
        }

        if (mTransactionType == TRANSACTION_TYPE.OMZET || mTransactionType == TRANSACTION_TYPE.SKN) {
            tvToolbarTitle.text = getString(R.string.title_activity_withdraw_confirmation)
        }

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        if (mTransactionType == TRANSACTION_TYPE.REVENUE) {
            // cvTotal.gone()
            btnSubmit.setText("Confirm")
        }

        btnSubmit.setOnClickListener {
            if (mTransactionType == TRANSACTION_TYPE.REVENUE) {
                inputPin()
            } else if (btnSubmit.text.toString().equals(getString(R.string.button_pay), true)) {
                inputPin()
            } else {
                if (mTransactionType == TRANSACTION_TYPE.AJ) {
                    checkTransferStatus()
                } else {
                    checkTransferDepositStatus()
                }
            }
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

    private fun displayConfirmatipnData() {
        var confirmationDataList = mutableListOf<WidgetBuilderModel>()

        when (mTransactionType) {
            TRANSACTION_TYPE.AJ -> {
                var bankOwner = WidgetBuilderModel()
                bankOwner.key = getString(R.string.text_withdraw_destination)
                bankOwner.value = mBankSelected?.bankName
                confirmationDataList.add(bankOwner)

                var noRek = WidgetBuilderModel()
                noRek.key = getString(R.string.text_account_number)
                noRek.value = mBankSelected?.accountNumber
                confirmationDataList.add(noRek)

                var accountName = WidgetBuilderModel()
                accountName.key = getString(R.string.text_account_name)
                accountName.value = mBankSelected?.accountOwner
                confirmationDataList.add(accountName)

                withdrawMethod?.let {
                    var method = WidgetBuilderModel()
                    method.key = getString(R.string.text_transfer_method)
                    method.value = it.name
                    confirmationDataList.add(method)
                }


                //Payment Details
                val adminFee = 5000
                tvAmount.text = DataUtil.convertCurrency(mAmount)
                tvAdminFee.text = DataUtil.convertCurrency(adminFee)
                tvTotal.text = DataUtil.convertCurrency(mAmount + adminFee)
                paymentDetailsLayout.visible()
            }
            TRANSACTION_TYPE.TRANSFER -> {
                var bankOwner = WidgetBuilderModel()
                bankOwner.key = getString(R.string.text_withdraw_destination)
                bankOwner.value = mBankSelected?.bankName
                confirmationDataList.add(bankOwner)

                var noRek = WidgetBuilderModel()
                noRek.key = getString(R.string.text_account_number)
                noRek.value = mBankSelected?.accountNumber
                confirmationDataList.add(noRek)

                var accountName = WidgetBuilderModel()
                accountName.key = getString(R.string.text_account_name)
                accountName.value = mBankSelected?.accountName
                confirmationDataList.add(accountName)

                withdrawMethod?.let {
                    var method = WidgetBuilderModel()
                    method.key = getString(R.string.text_transfer_method)
                    method.value = it.name
                    confirmationDataList.add(method)
                }


                //Payment Details
                tvAmount.text = DataUtil.convertCurrency(mWithdrawAmount)
                tvAdminFee.gone()
                tvLabelFee.gone()
                tvLabelTotal.gone()
                tvTotal.text = DataUtil.convertCurrency(mWithdrawAmount)
                paymentDetailsLayout.visible()
            }
            TRANSACTION_TYPE.OMZET -> {
                var bankOwner = WidgetBuilderModel()
                bankOwner.key = getString(R.string.text_withdraw_destination)
                bankOwner.value = mBankSelected?.bankName
                confirmationDataList.add(bankOwner)

                var noRek = WidgetBuilderModel()
                noRek.key = getString(R.string.text_account_number)
                noRek.value = mBankSelected?.accountNumber
                confirmationDataList.add(noRek)

                var accountName = WidgetBuilderModel()
                accountName.key = getString(R.string.text_account_name)
                accountName.value = mBankSelected?.accountOwner
                confirmationDataList.add(accountName)

                //Payment Details
                val adminFee = 0
                tvAmount.text = DataUtil.convertCurrency(mAmount)
                tvAdminFee.text = DataUtil.convertCurrency(adminFee)
                tvTotal.text = DataUtil.convertCurrency(mAmount + adminFee)
                paymentDetailsLayout.visible()
            }
            TRANSACTION_TYPE.SKN -> {
                var bankOwner = WidgetBuilderModel()
                bankOwner.key = getString(R.string.text_withdraw_destination)
                bankOwner.value = mBankSelected?.bankName
                confirmationDataList.add(bankOwner)

                var noRek = WidgetBuilderModel()
                noRek.key = getString(R.string.text_account_number)
                noRek.value = mBankSelected?.accountNumber
                confirmationDataList.add(noRek)

                var accountName = WidgetBuilderModel()
                accountName.key = getString(R.string.text_account_name)
                accountName.value = mBankSelected?.accountOwner
                confirmationDataList.add(accountName)

                withdrawMethod?.let {
                    var method = WidgetBuilderModel()
                    method.key = getString(R.string.text_transfer_method)
                    method.value = it.name
                    confirmationDataList.add(method)
                }

                //Payment Details
                val adminFee = 2000
                tvAmount.text = DataUtil.convertCurrency(mAmount)
                tvAdminFee.text = DataUtil.convertCurrency(adminFee)
                tvTotal.text = DataUtil.convertCurrency(mAmount + adminFee)
                paymentDetailsLayout.visible()
            }
            TRANSACTION_TYPE.DEPOSIT -> {
                var bankOwner = WidgetBuilderModel()
                bankOwner.key = getString(R.string.text_withdraw_destination)
                bankOwner.value = mBankSelected?.bankName
                confirmationDataList.add(bankOwner)

                var noRek = WidgetBuilderModel()
                noRek.key = getString(R.string.text_account_number)
                noRek.value = mBankSelected?.accountNumber
                confirmationDataList.add(noRek)

                var accountName = WidgetBuilderModel()
                accountName.key = getString(R.string.text_account_name)
                accountName.value = mBankSelected?.accountName
                confirmationDataList.add(accountName)

                withdrawMethod?.let {
                    var method = WidgetBuilderModel()
                    method.key = getString(R.string.text_transfer_method)
                    method.value = it.name
                    confirmationDataList.add(method)
                }


                //Payment Details
                val adminFee = 6500
                tvAmount.text = DataUtil.convertCurrency(mAmount)
                tvAdminFee.text = DataUtil.convertCurrency(adminFee)
                tvTotal.text = DataUtil.convertCurrency(mAmount + adminFee)
                paymentDetailsLayout.visible()
            }
            TRANSACTION_TYPE.REVENUE -> {
                textView36.text = "Withdraw Amount"
                tvLabelFee.text = "Withdraw fee "
                tvTotal.text = "Total Payment"

                var bankOwner = WidgetBuilderModel()
                bankOwner.key = "Withdraw to"
                bankOwner.value = "Linked Account"
                confirmationDataList.add(bankOwner)

                var accountName = WidgetBuilderModel()
                accountName.key = "To Account"
                accountName.value = mAccountName + " - " + mAccountNumber


                confirmationDataList.add(accountName)

                //Payment Details
                val adminFee = 0
                tvAmount.text = DataUtil.InputDecimal(mWithdrawAmount.toString())
                tvAdminFee.text = DataUtil.InputDecimal(adminFee.toString())
                tvTotal.text = DataUtil.InputDecimal((mWithdrawAmount + adminFee).toString())
                paymentDetailsLayout.visible()
            }
            else -> {
                var noRek = WidgetBuilderModel()
                noRek.key = getString(R.string.text_withdraw_destination)
                noRek.value = mBankSelected?.accountNumber
                confirmationDataList.add(noRek)

                var bankOwner = WidgetBuilderModel()
                bankOwner.key = getString(R.string.label_recipient_name)
                bankOwner.value = mBankSelected?.accountOwner
                confirmationDataList.add(bankOwner)

                var amount = WidgetBuilderModel()
                amount.key = getString(R.string.label_amount)
                amount.value = DataUtil.convertCurrency(mAmount)
                confirmationDataList.add(amount)

                var total = WidgetBuilderModel()
                total.key = getString(R.string.text_payment_fee)
                total.value = DataUtil.convertCurrency(mTransferBankInquiryResponseModel.fee)
                confirmationDataList.add(total)
            }
        }

        val adapter = PpobPaymentDetailAdapter(this, confirmationDataList)
        adapter.orientation = PpobPaymentDetailAdapter.VERTICAL
        recyclerview.adapter = adapter
    }

    private fun inputPin() {
        val intent = Intent(this, RegisterPinResetActivity::class.java)
        intent.putExtra(AppKeys.KEY_INPUT_PIN_TYPE_INPUT, true)
        startActivityForResult(intent, AppKeys.KEY_INPUT_PIN)
    }

    private fun paymentSuccess(basePaymentResponseModel: BasePaymentResponseModel) {
        var ppobProductType = PpobProductType()
        ppobProductType.type = PpobProductType.TRANSFER_BANK

        var ppobPayment = PpobPayment()
        ppobPayment.productName = getString(R.string.text_transfer_ke_bank)
        ppobPayment.ppobProductType = PpobProductTypeSetup().getProductTransferBank()
        ppobPayment.totalPayment = mAmount.toDouble()
        ppobPayment.ppobProductType = ppobProductType
        ppobPayment.totalPayment = mAmount.toDouble()

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

    private fun paymentAjSuccess(basePaymentResponseModel: BasePaymentResponseModel) {

        var ppobPayment = PpobPayment()

        if (mTransactionType == TRANSACTION_TYPE.AJ) {
            ppobPayment.productName = getString(R.string.text_transfer_ke_bank)
        } else if (mTransactionType == TRANSACTION_TYPE.DEPOSIT) {
            ppobPayment.productName = getString(R.string.text_transfer_deposite)
        }

        ppobPayment.ppobProductType = PpobProductTypeSetup().getProductTransferBank()
        ppobPayment.totalPayment = mAmount.toDouble()

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

    private fun paymentOttoKonekSuccess(basePaymentResponseModel: BasePaymentResponseModel) {

        var ppobPayment = PpobPayment().apply {
            ppobProductType = PpobProductTypeSetup().getProductTransferBank()
            totalPayment = mAmount.toDouble()
        }

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


    private fun withdrawReveenuePaymentSuccess(withdrawRevenueResponse: WithdrawRevenueResponse) {
        var ppobPayment = PpobPayment()

        ppobPayment.productName = "Withdraw Revenue"

        ppobPayment.ppobProductType = PpobProductTypeSetup().getProductTransferBank()
        ppobPayment.totalPayment = mAmount.toDouble()

        var ppobOttoagPaymentResponseModel = PpobOttoagPaymentResponseModel()
        var data = PaymentData()
        data.keyValueList = withdrawRevenueResponse.data?.key_value_list
        ppobOttoagPaymentResponseModel.data = data

        val intent = Intent(this, PpobPaymentSuccessActivity::class.java)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, ppobPayment)
        intent.putExtra(
            AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA,
            Gson().toJson(ppobOttoagPaymentResponseModel)
        )
        startActivity(intent)
    }

    private fun transferPending() {
        btnSubmit.text = getString(R.string.button_check_status)
    }

    private fun transferOmzetSuccess(omzetSaldoResponseModel: OmzetSaldoResponseModel) {
        var ppobPaymentType = PpobProductType()
        ppobPaymentType.type = PpobProductType.QR_PAYMENT

        var ppobPayment = PpobPayment()
        ppobPayment.productName = getString(R.string.text_transfer_omzet)
        ppobPayment.ppobProductType = ppobPaymentType
        ppobPayment.totalPayment = mAmount.toDouble()

        var ppobOttoagPaymentResponseModel = PpobOttoagPaymentResponseModel()
        var data = PaymentData()
        data.keyValueList = omzetSaldoResponseModel.data.key_value_list
        ppobOttoagPaymentResponseModel.data = data

        val intent = Intent(this, PpobPaymentSuccessActivity::class.java)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, ppobPayment)
        intent.putExtra(
            AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA,
            Gson().toJson(ppobOttoagPaymentResponseModel)
        )
        startActivity(intent)
    }


    //region API Call

    private fun callPayment(transferBankInquiryResponseModel: TransferBankInquiryResponseModel) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        val requestModel = TransferToBankConfirmationRequestModel()
        requestModel.amount = mAmount
        requestModel.bank_code = mBankSelected?.bankCode
        requestModel.customer_reference = mBankSelected?.accountNumber
        requestModel.description = ""
        requestModel.pin = mPin
        requestModel.receiver_name = mBankSelected?.accountOwner
        requestModel.wallet_id = SessionManager.getWalletID()

        WalletDao(this).getTfBankConf(
            requestModel,
            BaseDao.getInstance(this, KEY_API_TRANSFER_BANK_PAYMENT).callback
        )
    }

    private fun callOttokonekPayment(
        transferBankInquiryResponse: TransferBankInquiryResponse,
        pin: String?
    ) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        val requestModel = TransferBankPaymentRequest()
        requestModel.amount = mWithdrawAmount
        requestModel.rrn = transferBankInquiryResponse.data?.rrn.toString()
        requestModel.pin = pin

        OttoKonekAPI.transferBankPayment(
            this,
            requestModel,
            object : ApiCallback<BasePaymentResponseModel>() {
                override fun onResponseSuccess(body: BasePaymentResponseModel?) {
                    dismissProgressDialog()
                    body?.let { body ->
                        if (isSuccess200) {
                            paymentOttoKonekSuccess(body)
                        } else {
                            showErrorDialog(body.meta?.message)
                        }
                    }
                }

                override fun onApiServiceFailed(throwable: Throwable?) {
                    dismissProgressDialog()
                    onApiResponseError()
                }
            })
    }

    private fun callAjPayment(
        transferBankInquiryResponse: TransferBankInquiryResponse,
        pin: String
    ) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        val requestModel = TransferBankPaymentRequest()
        requestModel.amount = mAmount.toDouble()
        requestModel.rrn = transferBankInquiryResponse.data?.rrn.toString()
        requestModel.pin = pin

        TransferBankDao(this).transferBankPayment(
            requestModel,
            BaseDao.getInstance(this, KEY_API_TRANSFER_BANK_AJ_PAYMENT).callback
        )
    }

    private fun callDepositAjPayment(
        transferBankInquiryResponse: TransferBankInquiryResponse,
        pin: String
    ) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        val requestModel = TransferBankPaymentRequest()
        requestModel.amount = mAmount.toDouble()
        requestModel.rrn = transferBankInquiryResponse.data?.rrn.toString()
        requestModel.pin = pin
        requestModel.latitude = myLastLocation.latitude
        requestModel.longitude = myLastLocation.longitude
        requestModel.ip = SystemUtil.getLocalIpAddress()

        TransferDepositDao(this).transferDepositPayment(
            requestModel,
            BaseDao.getInstance(this, KEY_API_TRANSFER_DEPOSIT_AJ_PAYMENT).callback
        )
    }

    private fun checkTransferStatus() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        val requestModel = TransferBankPaymentRequest()
        requestModel.rrn = mTransferBankInquiryResponse.data?.rrn.toString()

        TransferBankDao(this).checkTransferStatus(
            requestModel,
            BaseDao.getInstance(this, KEY_API_TRANSFER_BANK_AJ_CHECK_STATUS).callback
        )
    }

    private fun checkTransferDepositStatus() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        val requestModel = TransferBankPaymentRequest()
        requestModel.rrn = mTransferBankInquiryResponse.data?.rrn.toString()
        requestModel.latitude = myLastLocation.latitude
        requestModel.longitude = myLastLocation.longitude
        requestModel.ip = SystemUtil.getLocalIpAddress()

        TransferDepositDao(this).checkTransferDepositStatus(
            requestModel,
            BaseDao.getInstance(this, KEY_API_TRANSFER_BANK_AJ_CHECK_STATUS).callback
        )
    }

    private fun callTransferOmzet(pin: String) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        var model = OmzetSaldoRequestModel()
        //  model.pin = pin
        //  model.userId = SessionManager.getUserId()
        model.amount = mAmount

        TransactionDao(this).transOmzetToWallet(
            this,
            model,
            BaseDao.getInstance(this, KEY_TRANSFER_OMZET).callback
        )
    }

    private fun callTransferSkn(mPin: String) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        mBankSelected?.let {
            val requestModel = TransferSknRequest()
            requestModel.account_number = it.accountNumber
            requestModel.bank_name = it.bankName
            requestModel.bank_code = it.bankCode
            requestModel.amount = mAmount
            requestModel.currency_code = "IDR"
            requestModel.customer_name = it.accountOwner
            requestModel.description = ""
            requestModel.submitted_at = DateUtil.getCurrentDate("yyyy-MM-dd'T'HH:mm:ssXXX")

            TransferBankDao(this).transferSkn(
                requestModel,
                BaseDao.getInstance(this, KEY_API_TRANSFER_BANK_SKN).callback
            )
        }
    }

    private fun callWithdrawRevenuePayment(mPin: String?) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        val requestModel = WithdrawRevenueRequest()
        requestModel.amount = mWithdrawAmount
        requestModel.pin = mPin

        RevenueDao(this).withdrawRevenuePayment(
            requestModel,
            BaseDao.getInstance(this, KEY_API_WITHDRAW_REVENUE_PAYMENT).callback
        )
    }

    override fun onApiResponseCallback(
        br: BaseResponse?,
        responseCode: Int,
        response: Response<*>?
    ) {
        if (response != null) {
            if (response.isSuccessful) {
                when (responseCode) {
                    KEY_API_TRANSFER_BANK_PAYMENT -> if ((br as BaseResponseModel).meta.code == 200) {
                        paymentSuccess((br as BasePaymentResponseModel))
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.setOnDismissListener {
                            finish()
                        }
                        dialog.show()
                    }
                    KEY_API_TRANSFER_BANK_AJ_PAYMENT -> if ((br as BaseResponseModel).meta.code == 200) {
                        paymentAjSuccess((br as BasePaymentResponseModel))
                    } else if (br.meta.code == 202) {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.show()
                        transferPending()
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.setOnDismissListener {
                            finish()
                        }
                        dialog.show()
                    }
                    KEY_API_TRANSFER_BANK_AJ_CHECK_STATUS -> if ((br as BaseResponseModel).meta.code == 200) {
                        paymentAjSuccess((br as BasePaymentResponseModel))
                    } else if (br.meta.code == 202) {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.show()
                        transferPending()
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.setOnDismissListener {
                            finish()
                        }
                        dialog.show()
                    }
                    KEY_TRANSFER_OMZET -> if ((br as BaseResponseModel).meta.code == 200) {
                        transferOmzetSuccess((br as OmzetSaldoResponseModel))
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.setOnDismissListener {
                            finish()
                        }
                        dialog.show()
                    }
                    KEY_API_TRANSFER_BANK_SKN -> if ((br as BaseResponseModel).meta.code == 200) {
                        paymentAjSuccess((br as BasePaymentResponseModel))
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.setOnDismissListener {
                            finish()
                        }
                        dialog.show()
                    }
                    KEY_API_TRANSFER_DEPOSIT_AJ_PAYMENT -> if ((br as BaseResponseModel).meta.code == 200) {
                        paymentAjSuccess((br as BasePaymentResponseModel))
                    } else if (br.meta.code == 202) {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.show()
                        transferPending()
                    }
                    KEY_API_WITHDRAW_REVENUE_PAYMENT -> if ((br as BaseResponseModel).meta.code == 200) {
                        withdrawReveenuePaymentSuccess((br as WithdrawRevenueResponse))
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.show()
                        transferPending()
                    }
                }
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        onApiResponseError()
    }

    fun callApiConfrim(mPin: String?) {

        showProgressDialog(getString(R.string.loading_message))
        val request = WithdrawRequest()
        request.accountNumber = mAccountNumber
        request.amount = mWithdrawAmount
        request.bin = mBin
        request.pin = mPin
        OttoKonekAPI.withdraw(
            this,
            request,
            object : ApiCallback<TransferMultiBankConfrimResponse>() {
                override fun onResponseSuccess(body: TransferMultiBankConfrimResponse?) {
                    dismissProgressDialog()
                    body?.let { body ->

                        if (isSuccess200) {
                            paymentWithdrawSucces(body)
                        } else {
                            showErrorDialog(body.meta?.message)
                        }
                    }
                }

                override fun onApiServiceFailed(throwable: Throwable?) {
                    dismissProgressDialog()
                    onApiResponseError()
                }

            })
    }

    private fun paymentWithdrawSucces(basePaymentResponseModel: TransferMultiBankConfrimResponse) {

        var ppobPayment = PpobPayment().apply {
            ppobProductType = PpobProductTypeSetup().getProductTransferBank()
            totalPayment = mAmount.toDouble()
        }

        var ppobOttoagPaymentResponseModel = PpobOttoagPaymentResponseModel()
        var data = PaymentData()
        data.keyValueList = basePaymentResponseModel.data?.keyValueList
        ppobOttoagPaymentResponseModel.data = data

        val intent = Intent(this, PpobPaymentSuccessActivity::class.java)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, ppobPayment)
        intent.putExtra(
            AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA,
            Gson().toJson(ppobOttoagPaymentResponseModel)
        )
        startActivity(intent)
    }

    //endregion API Call
}
