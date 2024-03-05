package com.otto.mart.ui.activity.transaction.transferToBank

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel
import com.otto.mart.model.APIModel.Request.TransferToBankConfirmationRequestModel
import com.otto.mart.model.APIModel.Request.TransferToBankInquiryRequestModel
import com.otto.mart.model.APIModel.Request.transfer.TransferBankInquiryRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.OmzetStatResponseModel
import com.otto.mart.model.APIModel.Response.TransferBankInquiryResponseModel
import com.otto.mart.model.APIModel.Response.WalletResponseModel
import com.otto.mart.model.APIModel.Response.bank.BankAccountListResponseModel
import com.otto.mart.model.APIModel.Response.transfer.TransferBankInquiryResponse
import com.otto.mart.presenter.dao.ProfileDao
import com.otto.mart.presenter.dao.TransactionDao
import com.otto.mart.presenter.dao.WalletDao
import com.otto.mart.presenter.dao.deposit.TransferDepositDao
import com.otto.mart.presenter.dao.transfer.TransferBankDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.formValidation.FormValidation
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.ppob.PpobKomisiAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.ppob.PpobPaymentSuccessActivity
import com.otto.mart.ui.activity.ppob.setup.PpobProductTypeSetup
import com.otto.mart.ui.activity.profile.BankListActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.activity.transaction.transferToBank.TransferBankConfirmationActivity.Companion.KEY_TRANSACTION_TYPE
import com.otto.mart.ui.activity.transaction.transferToBank.TransferBankConfirmationActivity.TRANSACTION_TYPE
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.content_transfer_bank.*
import kotlinx.android.synthetic.main.fragment_payment_method.recyclerview
import kotlinx.android.synthetic.main.ppob_button_provider.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class TransferBankActivity : AppActivity() {

    var KEY_API_GET_BANK_LIST = 100
    var KEY_API_TRANSFER_BANK_INQUIRY = 101
    var KEY_API_TRANSFER_BANK_PAYMENT = 102
    val KEY_OMZET_STAT: Int = 103
    var KEY_API_TRANSFER_BANK_AJ_INQUIRY = 104
    var KEY_API_TRANSFER_DEPOSIT_AJ_INQUIRY = 105

    var KEY_CODE_SELECT_BANK = 200

    var mBalance = 0
    var mPin: String = ""

    var mTextWatcher: TextWatcher? = null

    private var mIsValidationEnable = false
    private var mBankSelected: BankAccountModel? = null

    private var mTransactionType: TRANSACTION_TYPE? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_bank)

        if (intent.hasExtra(KEY_TRANSACTION_TYPE)) {
            mTransactionType = intent.getSerializableExtra(KEY_TRANSACTION_TYPE) as TRANSACTION_TYPE?
        }

        initView()
        initRecyclerView()
        displayAmountOption()
    }

    override fun onResume() {
        super.onResume()
        if (mTransactionType == TRANSACTION_TYPE.AJ) {
            getOmzet()
        } else {
            getWalletInfo()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppKeys.KEY_INPUT_PIN) {
                if (data != null) {
                    mPin = data?.getStringExtra(AppKeys.KEY_PIN_CODE)!!
                    callTransferInquiry()
                }
            } else if (requestCode == KEY_CODE_SELECT_BANK) {
                if (data?.getIntExtra("bankId", -1) != null) {
                    mBankSelected?.bankPos = data.getIntExtra("bankId", -1)
                }

                mBankSelected?.approvalStatus = data?.getStringExtra("approval_status")
                mBankSelected?.bankCode = data?.getStringExtra("bank_code")
                mBankSelected?.bankName = data?.getStringExtra("bankName")
                mBankSelected?.accountNumber = data?.getStringExtra("norek")
                mBankSelected?.accountOwner = data?.getStringExtra("name")

                displayBank(mBankSelected)
            }
        }
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_transfer_bank)

        if (mTransactionType == TRANSACTION_TYPE.AJ) {
            tvBalanceLabel.text = getString(R.string.text_omzet)
            refNumLayout.visible()
        }

        isFormValid

        btnToolbarBack.setOnClickListener { onBackPressed() }

        mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var amount = DataUtil.getInt(etAmount.text.toString().replace(getString(R.string.text_currency), ""))

                etAmount.removeTextChangedListener(mTextWatcher)
                setAmount(amount)
                etAmount.setSelection(etAmount.text.length)

                if (amount == 0) {
                    etAmount.setText("")
                } else {
                    mIsValidationEnable = true
                    if (amount < 100000) {
                        tvAmountError.text = getString(R.string.text_min_transfer_100)
                        tvAmountError.visible()
                    } else {
                        tvAmountError.text = ""
                        tvAmountError.gone()
                    }
                }

                etAmount.addTextChangedListener(mTextWatcher)
                isFormValid
            }

            override fun afterTextChanged(editable: Editable) {}
        }

        etAmount.addTextChangedListener(mTextWatcher)

        tvSelectProvider.text = getString(R.string.button_add_bank_account)
        btnProvider.setOnClickListener {
            val intent = Intent(this, BankListActivity::class.java)
            intent.putExtra("isAddNew", true)
            intent.putExtra("isFromTransfer", true)
            startActivityForResult(intent, KEY_CODE_SELECT_BANK)
        }

        btnSelectBank.setOnClickListener {
            val intent = Intent(this, BankListActivity::class.java)
            intent.putExtra("isFromTransfer", true)
            startActivityForResult(intent, KEY_CODE_SELECT_BANK)
        }

        btnSubmit.setOnClickListener {
            mIsValidationEnable = true
            if (isFormValid) {
                if (mTransactionType == TRANSACTION_TYPE.AJ) {
                    callTransferBankAjInquiry()
                } else {
                    callTransferInquiry()
                }
            }
        }
    }

    private fun initRecyclerView() {
        recyclerview.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        recyclerview.setLayoutManager(linearLayoutManager)
    }

    private fun dislayBalance(balance: String) {
        var balanceOttoCash = balance.replace("IDR", "")
                .replace(",", "")
                .replace(" ", "")

        mBalance = Integer.valueOf(balanceOttoCash)

        tvBalance.text = DataUtil.convertCurrency(Integer.valueOf(balanceOttoCash))
        etAmount.setText("")
    }

    private fun displayAmountOption() {
        var amountList = mutableListOf<Int>()
        amountList.add(100000)
        amountList.add(300000)
        amountList.add(500000)
        amountList.add(1000000)

        val adapter = PpobKomisiAdapter(this, amountList)
        recyclerview.adapter = adapter

        adapter?.setmOnViewClickListener(object : PpobKomisiAdapter.OnViewClickListener {
            override fun onViewClickListener(item: Int, position: Int) {
                if (item > mBalance) {
                    setAmount(mBalance)
                } else {
                    setAmount(item)
                }
            }
        })
    }

    private fun inputPin() {
        val intent = Intent(this, RegisterPinResetActivity::class.java)
        intent.putExtra(AppKeys.KEY_INPUT_PIN_TYPE_INPUT, true)
        startActivityForResult(intent, AppKeys.KEY_INPUT_PIN)
    }

    /**
     * method to validate Leziz NU form
     * */
    private val isFormValid: Boolean
        get() {

            var status = false
            if (!mIsValidationEnable) {
                btnSubmit.background = ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)
                return false
            }

            var amount = etAmount.text.toString()
            var amountInt = DataUtil.getInt(etAmount.text.toString().replace(getString(R.string.text_currency), ""))

            tvAmountError.gone()

            if (!FormValidation(this).isRequired(amount)) {
                tvAmountError.text = getString(R.string.transfer_bank_msg_amount_is_required)
                tvAmountError.visible()
                status = false
            } else if (amountInt < 100000) {
                tvAmountError.text = getString(R.string.text_min_transfer_100)
                tvAmountError.visible()
                status = false
            } else if (btnSelectBank.visibility == View.GONE) {
                status = false
            } else {
                status = true
            }

            if (status) {
                btnSubmit.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
            } else {
                btnSubmit.background = ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)
            }

            return status
        }

    private fun setAmount(amount: Int) {
        var validAmount = 0

        if (amount > mBalance) {
            validAmount = mBalance
        } else {
            validAmount = amount
        }

        etAmount.setText(DataUtil.convertCurrency(validAmount))
        etAmount.setSelection(etAmount.length())
    }

    private fun displayBank(bank: BankAccountModel?) {
        if (bank != null) {

            mBankSelected = bank

            tvBankAccounttName.text = bank?.accountOwner
            tvBankAccountNumber.text = bank?.bankName + " - " + bank?.accountNumber

            bankLayout.gone()
            btnSelectBank.visible()
            isFormValid
        } else {
            bankLayout.visible()
            btnSelectBank.gone()
            isFormValid
        }

        viewAnimator.displayedChild = 1
    }

    private fun getOmzetStatSuccess(omzetStatResponseModel: OmzetStatResponseModel) {
        var balanceOttoCash = omzetStatResponseModel.all_omset.replace("IDR", "")
                .replace(",", "")
                .replace(" ", "")

        mBalance = Integer.valueOf(balanceOttoCash)
        tvBalance.text = omzetStatResponseModel.all_omset
        etAmount.setText("")
        viewAnimator.displayedChild = 1
    }

    private fun inquirySuccess(transferBankInquiryResponseModel: TransferBankInquiryResponseModel) {
        val intent = Intent(this, TransferBankConfirmationActivity::class.java)
        intent.putExtra(TransferBankConfirmationActivity.KEY_DATA_INQUIRY, Gson().toJson(transferBankInquiryResponseModel))
        intent.putExtra(TransferBankConfirmationActivity.KEY_DATA_BANK, Gson().toJson(mBankSelected))
        intent.putExtra(TransferBankConfirmationActivity.KEY_DATA_AMOUNT, DataUtil.convertLongFromCurrency(etAmount.getText().toString()))
        intent.putExtra(AppKeys.KEY_AMOUNT, etAmount.text.toString())
        startActivity(intent)
    }

    private fun inquiryAjSuccess(transferBankInquiryResponse: TransferBankInquiryResponse) {
        val intent = Intent(this, TransferBankConfirmationActivity::class.java)
        intent.putExtra(TransferBankConfirmationActivity.KEY_DATA_INQUIRY, Gson().toJson(transferBankInquiryResponse))
        intent.putExtra(TransferBankConfirmationActivity.KEY_DATA_BANK, Gson().toJson(mBankSelected))
        intent.putExtra(TransferBankConfirmationActivity.KEY_DATA_AMOUNT, DataUtil.convertLongFromCurrency(etAmount.getText().toString()))
        intent.putExtra(AppKeys.KEY_AMOUNT, etAmount.text.toString())
        intent.putExtra(KEY_TRANSACTION_TYPE, mTransactionType)
        startActivity(intent)
    }

    private fun paymentSuccess(basePaymentResponseModel: BasePaymentResponseModel) {
        val intent = Intent(this, PpobPaymentSuccessActivity::class.java)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, PpobProductTypeSetup().getProductTransferBank())
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA, Gson().toJson(basePaymentResponseModel))

        startActivity(intent)
    }


    //region API Call

    private fun getWalletInfo() {
        WalletDao(this).emoneySummary(BaseDao.getInstance(this@TransferBankActivity, AppKeys.API_KEY_WALLET_INFO).callback)
    }

    private fun getBankList() {
        ProfileDao(this).getBankList(BaseDao.getInstance(this, KEY_API_GET_BANK_LIST).callback)
    }

    private fun getOmzet() {
        TransactionDao(this).omzetStatus(BaseDao.getInstance(this, KEY_OMZET_STAT).callback)
    }

    private fun callTransferInquiry() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

        val requestModel = TransferToBankInquiryRequestModel()
        requestModel.amount = DataUtil.convertLongFromCurrency(etAmount.getText().toString())
        requestModel.bank_code = mBankSelected?.bankCode
        requestModel.customer_reference = mBankSelected?.accountNumber
        requestModel.description = "10"
        WalletDao(this).getTfBankInquiry(requestModel, BaseDao.getInstance(this, KEY_API_TRANSFER_BANK_INQUIRY).callback)
    }

    private fun callTransferBankAjInquiry() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

        val requestModel = TransferBankInquiryRequest()
        requestModel.amount = DataUtil.convertLongFromCurrency(etAmount.getText().toString()).toDouble()
        requestModel.bankCode = mBankSelected?.bankCode
        requestModel.bankAccount = mBankSelected?.accountNumber
        requestModel.custRefNumber = etRefNum.text.toString()

        TransferBankDao(this).transferBankInquiry(requestModel, BaseDao.getInstance(this, KEY_API_TRANSFER_BANK_AJ_INQUIRY).callback)
    }

    private fun callTransferDepositAjInquiry() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

        val requestModel = TransferBankInquiryRequest()
        requestModel.amount = DataUtil.convertLongFromCurrency(etAmount.getText().toString()).toDouble()
        requestModel.bankCode = mBankSelected?.bankCode
        requestModel.bankAccount = mBankSelected?.accountNumber
        requestModel.custRefNumber = etRefNum.text.toString()

        TransferDepositDao(this).transferDepositInquiry(requestModel, BaseDao.getInstance(this, KEY_API_TRANSFER_DEPOSIT_AJ_INQUIRY).callback)
    }

    private fun callPayment(transferBankInquiryResponseModel: TransferBankInquiryResponseModel) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

        val requestModel = TransferToBankConfirmationRequestModel()
        requestModel.amount = DataUtil.convertLongFromCurrency(etAmount.getText().toString())
        requestModel.bank_code = mBankSelected?.bankCode
        requestModel.customer_reference = mBankSelected?.accountNumber
        requestModel.description = ""
        requestModel.pin = mPin
        requestModel.receiver_name = mBankSelected?.accountOwner
        requestModel.wallet_id = SessionManager.getWalletID()

        WalletDao(this).getTfBankConf(requestModel, BaseDao.getInstance(this, KEY_API_TRANSFER_BANK_PAYMENT).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        when (responseCode) {
            AppKeys.API_KEY_WALLET_INFO -> if ((br as BaseResponseModel).meta.code == 200) {
                if ((br as WalletResponseModel).data.size > 0) {
                    dislayBalance(br.data[0].balance)
                }
                if (mBankSelected == null) {
                    getBankList()
                }
            } else {
                val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                dialog.show()
            }
            KEY_OMZET_STAT -> if ((br as BaseResponseModel).meta.code == 200) {
                getOmzetStatSuccess((br as OmzetStatResponseModel))
                if (mBankSelected == null) {
                    getBankList()
                }
            } else {
                val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                dialog.setOnDismissListener {
                    getOmzet()
                }
                dialog.show()
            }
            KEY_API_GET_BANK_LIST -> if ((br as BaseResponseModel).meta.code == 200) {
                if ((br as BankAccountListResponseModel).data.account.size > 0) {
                    displayBank(br.data.account[0])
                } else {
                    displayBank(null)
                }
            } else {
                val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                dialog.setOnDismissListener {
                    finish()
                }
                dialog.show()
            }
            KEY_API_TRANSFER_BANK_INQUIRY -> if ((br as BaseResponseModel).meta.code == 200) {
                inquirySuccess((br as TransferBankInquiryResponseModel))
            } else {
                val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                dialog.show()
            }
            KEY_API_TRANSFER_BANK_AJ_INQUIRY -> if ((br as BaseResponseModel).meta.code == 200) {
                inquiryAjSuccess((br as TransferBankInquiryResponse))
            } else {
                val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                dialog.show()
            }
            KEY_API_TRANSFER_BANK_PAYMENT -> if ((br as BaseResponseModel).meta.code == 200) {
                paymentSuccess((br as BasePaymentResponseModel))
            } else {
                val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                dialog.setOnDismissListener {
                    finish()
                }
                dialog.show()
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        onApiResponseError()
    }

    //endregion API Call

}
