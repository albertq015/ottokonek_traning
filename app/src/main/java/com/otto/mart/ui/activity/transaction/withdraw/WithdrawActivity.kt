package com.otto.mart.ui.activity.transaction.withdraw

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel
import com.otto.mart.model.APIModel.Request.transfer.TransferBankInquiryRequest
import com.otto.mart.model.APIModel.Request.transfer.TransferLimitRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.balance.OttoKonekBalanceResponse
import com.otto.mart.model.APIModel.Response.bank.BankAccountListResponseModel
import com.otto.mart.model.APIModel.Response.multibank.AccounlistModel
import com.otto.mart.model.APIModel.Response.transfer.TransferBankInquiryResponse
import com.otto.mart.model.APIModel.Response.transfer.TransferLimitResponse
import com.otto.mart.model.localmodel.omzet.WithdrawMethod
import com.otto.mart.presenter.dao.ProfileDao
import com.otto.mart.presenter.dao.WalletDao
import com.otto.mart.presenter.dao.deposit.TransferDepositDao
import com.otto.mart.presenter.dao.transfer.TransferBankDao
import com.otto.mart.support.util.*
import com.otto.mart.support.util.formValidation.FormValidation
import com.otto.mart.support.util.pref.Pref
import com.otto.mart.ui.Partials.adapter.withdraw.WithdrawMethodAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.cashOut.CashOutSelectAmountActivity.Companion.KEY_BANK_DATA
import com.otto.mart.ui.activity.multibank.ListLinkedBankAccountPresenter
import com.otto.mart.ui.activity.multibank.ListLinkedBankAccountPresenterImpl
import com.otto.mart.ui.activity.multibank.SelectAccountActivityForWithdraw
import com.otto.mart.ui.activity.profile.BankActivity
import com.otto.mart.ui.activity.profile.BankSelectionActivity
import com.otto.mart.ui.activity.transaction.balance.OmzetActivity
import com.otto.mart.ui.activity.transaction.transferToBank.TransferBankConfirmationActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.content_cash_out_select_amount.*
import kotlinx.android.synthetic.main.content_withdraw.*
import kotlinx.android.synthetic.main.content_withdraw.etAmount
import kotlinx.android.synthetic.main.content_withdraw.tvAmountError
import kotlinx.android.synthetic.main.item_list_account_bank.view.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.withdraw_method.*
import retrofit2.Response

class WithdrawActivity : AppActivity() {

    companion object {
        val KEY_REQUEST_BANK = 100
        val KEY_BANK_DATA_LIST = "bank_data_list"
        val KEY_CASH_OUT_INPUT_DATA = "cash_input_data"

        val KEY_BA_POS = "POSITEMDATA"

    }

    var KEY_API_GET_BANK_LIST = 100
    val KEY_OMZET_STAT: Int = 101
    val KEY_GET_BALANCE: Int = 102
    var KEY_API_TRANSFER_BANK_AJ_INQUIRY = 103
    var KEY_API_TRANSFER_DEPOSIT_AJ_INQUIRY = 104
    var KEY_API_TRANSFER_LIMIT = 105
    var KEY_API_WITHDRAW_REVENUE_INQUIRY = 106

    var KEY_CODE_SELECT_BANK = 200

    var mPin: String = ""
    var mBalance: Double = 0.0
    private var isFromOmzet = false

    private var mBankSelected: BankAccountModel? = null
    private var mIsValidationEnable = false
    private var mBankSelectedAccount: AccounlistModel? = null

    var mTextWatcher: TextWatcher? = null

    var mWithdrawMethodList = mutableListOf<WithdrawMethod>()
    var mSelectedWithdrawMethod: WithdrawMethod? = null

    var mBankLimit: TransferLimitResponse.Data.Limit? = null
    var mOttoCashLimit: TransferLimitResponse.Data.Limit? = null

    val MIN_LIMIT_WITHDRAW_INSTANT = 250000L
    val MIN_LIMIT_WITHDRAW_SKN = 250000L
    val MIN_LIMIT_TRANSFER_OMZET = 250000L
    val MIN_LIMIT_WITHDRAW_DEPOSIT_INSTANT = 250000L
    val MIN_LIMIT_WITHDRAW_DEPOSIT_SKN = 250000L
    val MAX_LIMIT_WITHDRAW_DEPOSIT_INSTANT = 7500000L
    val MAX_LIMIT_WITHDRAW_DEPOSIT_SKN = 7500000L

    var mMinLimit = 0L
    var mMaxLimit = 0L
    var mAdminFee = 0.0

    val presenter: ListLinkedBankAccountPresenter = ListLinkedBankAccountPresenterImpl(this)
    var tryCountBalance: Int = 0
    var nameAccount: String? = null
    var numberAccount: String? = null
    var binAccount: String? = null

    private var hasBankBeenSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw)

        if (intent.hasExtra(OmzetActivity.KEY_IS_FROM_OMZET)) {
            isFromOmzet = intent.getBooleanExtra(OmzetActivity.KEY_IS_FROM_OMZET, false)
        }

        initView()
        initRecyclerview()
//        getTransferLimit()
        viewAnimator.displayedChild = 1
    }

    override fun onResume() {
        super.onResume()
        getBalance()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == KEY_CODE_SELECT_BANK) {

            } else if (requestCode == KEY_REQUEST_BANK) {
                data?.getIntExtra(KEY_BA_POS, -1).let {
                    hasBankBeenSelected = true
                    displayDataAccount(
                        presenter.getDataAccount().value?.get(it!!)?.object1?.binName,
                        presenter.getDataAccount().value?.get(it!!)?.object1?.accountNumber,
                        presenter.getDataAccount().value?.get(it!!)?.object2?.formatedBalance,
                        presenter.getDataAccount().value?.get(it!!)?.object1?.logo
                    )

                    nameAccount = presenter.getDataAccount().value?.get(it!!)?.object1?.binName
                    numberAccount =
                        presenter.getDataAccount().value?.get(it!!)?.object1?.accountNumber
                    binAccount = presenter.getDataAccount().value?.get(it!!)?.object1?.bin
                }

                data?.let {
                    if (it.hasExtra(KEY_BANK_DATA)) {
                        mBankSelectedAccount = Gson().fromJson(
                            it.getStringExtra(
                                KEY_BANK_DATA
                            ), AccounlistModel::class.java
                        )
                        displayBankAccount(mBankSelectedAccount)
                    }
                }
                isFormValid
            }
        }
    }

    private fun initView() {
        presenter.loadBanklistAPI()
        if (isFromOmzet) {
            tvToolbarTitle.text = getString(R.string.title_activity_withdraw)
            selectBankContainer.gone()

            mBalance = DataUtil.FormattedCurrencyToDouble(
                Pref.getPreference().getString(AppKeys.PREF_LAST_OMZET)
            )

            tvLabelBalance.text = getString(R.string.text_account_label)
            tvBalance.setText(Pref.getPreference().getString(AppKeys.PREF_LAST_OMZET))
        } else {
            tvToolbarTitle.text = getString(R.string.label_transfer_title)

            mBalance = DataUtil.FormattedCurrencyToDouble(
                Pref.getPreference().getString(AppKeys.PREF_LAST_BALANCE)
            )

            tvLabelBalance.text = getString(R.string.text_deposite)
            tvBalance.setText(Pref.getPreference().getString(AppKeys.PREF_LAST_BALANCE))
        }

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var amount = DataUtil.InputDecimal(charSequence.toString())

                etAmount.removeTextChangedListener(mTextWatcher)

                if (amount.equals("")) {
                    etAmount.setText("")
                } else {
                    etAmount.setText(amount)
                    etAmount.setSelection(etAmount.length())
                }

                etAmount.addTextChangedListener(mTextWatcher)
                mIsValidationEnable = true
                isFormValid
//                mIsValidationEnable = true
//                isFormValid
            }

            override fun afterTextChanged(editable: Editable) {}
        }

        etAmount.addTextChangedListener(mTextWatcher)
        etPhone.onChange { isFormValid }

        selectBankButton.setOnClickListener {
            gotoBankList()
        }

        rekeningLayout.setOnClickListener {
            gotoBankList()
        }

        btnSubmit.setOnClickListener {
            if (isFormValid) {
                if (isFromOmzet) {
                    withdrawRevenue()
                } else {
                    callTransferBankOttokonekInquiry()
                }
            }
        }

        card_selected_bank.setOnClickListener {
            gotoMultiBank()
        }
    }

    private fun gotoMultiBank() {
        val intent = Intent(this, SelectAccountActivityForWithdraw::class.java)
        startActivityForResult(intent, KEY_REQUEST_BANK)
    }

    private fun gotoBankList() {
        val intent = Intent(this, BankActivity::class.java)
        intent.putExtra(BankSelectionActivity.SELECTED_ID, mBankSelected?.id)
        intent.putExtra(OmzetActivity.KEY_IS_FROM_OMZET, isFromOmzet)
        startActivityForResult(intent, KEY_CODE_SELECT_BANK)
    }

    private fun initRecyclerview() {
        rvWithdrawMethod.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        rvWithdrawMethod.setLayoutManager(linearLayoutManager)
        ViewCompat.setNestedScrollingEnabled(rvWithdrawMethod, false)
    }

    private fun setAmount(amount: Int) {
        var validAmount = 0
        val minBalance = 500


        if ((amount + mAdminFee) > (mBalance - minBalance)) {
            validAmount = mBalance.toInt() - minBalance.toInt() - mAdminFee.toInt()
        } else {
            validAmount = amount
        }
//
//        val amount = DataUtil.InputDecimal(item.amount.toString().replace(",", "."))
//            .replace(mContext.getString(R.string.text_currency), "").trim()

        etAmount.setText(DataUtil.InputDecimal(validAmount.toString()))
        etAmount.setSelection(etAmount.length())
    }

//    private fun setAmount(amount: String) {
//        val inputAmount = amount.replace("[^\\d.]".toRegex(), "").toDouble()
//
//        var validAmount = 0.0
//        val minBalance = 0.0
//
//        if ((inputAmount + mAdminFee) > (mBalance - minBalance)) {
//            validAmount = mBalance - minBalance - mAdminFee
//        } else {
//            validAmount = inputAmount
//        }
//
//        if (amount.contains(".")) {
//            val separatedOrigin = amount.split("\\.".toRegex()).toTypedArray()
//            val separated = validAmount.toString().split("\\.".toRegex()).toTypedArray()
//
//            var partDecimalOrigin: String? = ""
//            var partDecimal: String? = ""
//
//            if (separatedOrigin.size > 1) {
//                if (separatedOrigin[1] != null && !separatedOrigin[1].isEmpty()) {
//                    partDecimalOrigin = if (separatedOrigin[1].length > 2) {
//                        separatedOrigin[1].substring(0, 2)
//                    } else {
//                        separatedOrigin[1]
//                    }
//                }
//            }
//
//            if (separated.size > 1) {
//                if (separated[1] != null && !separated[1].isEmpty()) {
//                    partDecimal = if (separated[1].length > 2) {
//                        separated[1].substring(0, 2)
//                    } else {
//                        separated[1]
//                    }
//                }
//            }
//
//            if (partDecimal.equals(partDecimalOrigin)) {
//                etAmount.setText(DataUtil.InputDecimal(validAmount.toString()))
//            } else {
//                if (partDecimal.equals("0")) {
//                    etAmount.setText(DataUtil.InputDecimal((separated[0] + ".")))
//                } else {
//                    etAmount.setText(DataUtil.InputDecimal(validAmount.toString()))
//                }
//            }
//        } else {
//            etAmount.setText(DataUtil.InputDecimal(validAmount.toString().replace(".0", "")))
//        }
//
//        etAmount.setSelection(etAmount.length())
//    }

    private fun getOmzetBalanceSuccess(ottoKonekBalanceResponse: OttoKonekBalanceResponse) {
        if (ottoKonekBalanceResponse != null) {

            tvBalance.setText(ottoKonekBalanceResponse.data?.formatted_balance)

            //Save Omzet
            Pref.getPreference().putString(AppKeys.PREF_LAST_OMZET, tvBalance.text.toString())

            ottoKonekBalanceResponse.data?.balance?.let {
                mBalance = it
            }
        }
    }

    private fun getDepositBalanceSuccess(ottoKonekBalanceResponse: OttoKonekBalanceResponse) {
        tvBalance.setText(ottoKonekBalanceResponse.data?.formatted_balance)

        //Save Last Balance
        Pref.getPreference().putString(AppKeys.PREF_LAST_BALANCE, tvBalance.text.toString())

        ottoKonekBalanceResponse.data?.balance?.let {
            mBalance = it
        }
    }

    private fun displayBankAccount(bank: AccounlistModel?) {
        if (bank != null) {

            mBankSelectedAccount = bank

            if (isFromOmzet) {
                //  tvBankAccounttName.text = bank?.
            } else {

                tvBankAccounttName.text = mBankSelected?.accountName
            }

            // tvBankAccountNumber.text = bank?.bankName + " - " + bank?.accountNumber

            selectBankButton.gone()
            rekeningLayout.visible()
        } else {
            selectBankButton.visible()
            rekeningLayout.gone()
        }
    }

    fun displayDataAccount(bankName: String?, numberAccount: String?, balance: String?,logo : String?) {

        selectBankButton.gone()
        card_selected_bank.gone()
        rekeningLayout.visible()
        rekeningLayout.setOnClickListener {
            gotoMultiBank()
        }


        tvBankAccountNumber.text = DataUtil.setFormatAccountNumber(numberAccount)
        tvBankAccounttName.text = bankName

        Glide.with(iv_logo_bank.context)
            .load(logo)
            .error(com.otto.mart.R.drawable.icon_blank)
            .placeholder(com.otto.mart.R.drawable.icon_blank)
            .into(iv_logo_bank)
    }

    private fun displayBank(bank: BankAccountModel?) {
        if (bank != null) {

            if (bank.id == BankSelectionActivity.BANK_VA_ID) {
                incWithdrawMethod.gone()
                transferVaNoteLayout.visible()
                refNumLayout.gone()
            } else {
//                incWithdrawMethod.visible()
                transferVaNoteLayout.gone()
            }

            mBankSelected = bank

            if (isFromOmzet) {
                tvBankAccounttName.text = bank.accountOwner
            } else {
                tvBankAccounttName.text = bank.accountName
            }

            tvBankAccountNumber.text = bank.bankName + " - " + bank.accountNumber

            selectBankButton.gone()
            rekeningLayout.visible()
        } else {
            selectBankButton.visible()
            rekeningLayout.gone()
        }
    }

    fun setTransactionLimit() {
        mBankSelected?.let {
            mSelectedWithdrawMethod?.let {
                mMinLimit = it.minLimit
                mMaxLimit = it.maxLimit
                mAdminFee = it.adminFee.toDouble()
            }
        }
    }


    /**
     * method to validate Leziz NU form
     * */
    private val isFormValid: Boolean
        get() {
            var status = true
            if (!mIsValidationEnable || !hasBankBeenSelected) {
                btnSubmit.background =
                    ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)
                return false
            }

            var amount = etAmount.text.toString()
            var amountInt = DataUtil.CurrencyToDouble(amount)

            tvAmountError.gone()
            tvPhoneError.gone()

            if (isFromOmzet) {
                if (!FormValidation(this).isRequired(amount)) {
                    tvAmountError.text = getString(R.string.transfer_bank_msg_amount_is_required)
                    tvAmountError.visible()
                    status = false
                }

                if (amount.equals("PHP 0")) {

                    status = false
                }


            } else {


                if (!FormValidation(this).isRequired(amount)) {
                    tvAmountError.text = getString(R.string.transfer_bank_msg_amount_is_required)
                    tvAmountError.visible()
                    status = false
                } else if (mMinLimit != 0L) {
                    if (amountInt < mMinLimit) {
                        tvAmountError.text =
                            "Minimal transfer " + DataUtil.convertCurrency(mMinLimit)
                        tvAmountError.visible()
                        status = false
                    }
                } else if (mMaxLimit != 0L) {
                    if (amountInt > mMaxLimit) {
                        tvAmountError.text =
                            "Maksimal transfer " + DataUtil.convertCurrency(mMaxLimit)
                        tvAmountError.visible()
                        status = false
                    }
                } else if ((phoneLayout.visibility == View.VISIBLE) && (!etPhone.text.toString()
                        .equals(""))
                ) {
                    if (!FormValidation(this).isValidMobilePhone(etPhone.text.toString())) {
                        tvPhoneError.text = getString(R.string.ppob_msg_phone_is_not_valid)
                        tvPhoneError.visible()
                        status = false
                    }
                } else if (mBankSelected == null) {
                    status = false
                }

            }

            if (status) {
                btnSubmit.background =
                    ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
            } else {
                btnSubmit.background =
                    ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)
            }

            return status
        }

    private fun getTopUpVaWithdrawMethod(): WithdrawMethod {
        var minLimit = 0L
        var maxLimit = 0L

        mOttoCashLimit?.let {
            minLimit = it.min_limit.replace(".", "").toLong()
            maxLimit = it.max_limit.replace(".", "").toLong()
        }

        var withdraw = WithdrawMethod(
            WithdrawMethod.METHOD.VA,
            getString(R.string.text_topup_va),
            getString(R.string.text_topup_va),
            minLimit,
            maxLimit,
            0,
            true, true
        )

        return withdraw
    }

    private fun displayWithDrawMethod() {
        mWithdrawMethodList.clear()

        var minLimit = 0L
        var maxLimit = 0L

        mBankLimit?.let {
            minLimit = it.min_limit.replace(".", "").toLong()
            maxLimit = it.max_limit.replace(".", "").toLong()
        }

        if (isFromOmzet) {

            val instant = WithdrawMethod(
                WithdrawMethod.METHOD.INSTANT,
                getString(R.string.text_instant_max_5k),
                getString(R.string.text_instant_transfer),
                minLimit, maxLimit,
                5000,
                true, true
            )
            val add = mWithdrawMethodList.add(instant)

            val nextDay = WithdrawMethod(
                WithdrawMethod.METHOD.SKN,
                getString(R.string.text_next_day_2k),
                getString(R.string.text_next_day_after_transfer),
                minLimit, maxLimit,
                2000,
                false, false
            )
            mWithdrawMethodList.add(nextDay)

            //Set default transfer method
            if (mSelectedWithdrawMethod == null || mBankSelected?.id == BankSelectionActivity.BANK_VA_ID) {
                withdrawMethodSelected(getTopUpVaWithdrawMethod(), -1)
            } else {
                withdrawMethodSelected(instant, 0)
            }
        } else {
            val instantDeposit = WithdrawMethod(
                WithdrawMethod.METHOD.DEPOSIT_INSTANT,
                getString(R.string.text_instant_max_6500),
                getString(R.string.text_instant_transfer),
                minLimit, maxLimit,
                6500,
                true, true
            )
            mWithdrawMethodList.add(instantDeposit)

            val nextDayDeposit = WithdrawMethod(
                WithdrawMethod.METHOD.DEPOSIT_SKN,
                getString(R.string.text_next_day_upcoming),
                getString(R.string.text_next_day_after_transfer),
                minLimit, maxLimit,
                0,
                false, false
            )
            mWithdrawMethodList.add(nextDayDeposit)


            //Set default transfer method
            withdrawMethodSelected(instantDeposit, 0)
        }

        val adapter = WithdrawMethodAdapter(this, mWithdrawMethodList)
        adapter.itemSelected = ::withdrawMethodSelected
        rvWithdrawMethod.adapter = adapter
    }

    fun withdrawMethodSelected(withdrawMethod: WithdrawMethod, position: Int) {
        mSelectedWithdrawMethod = withdrawMethod
        setTransactionLimit()

        if ((position >= 0)) {
            var i = 0
            for (withdrawMethod in mWithdrawMethodList) {
                mWithdrawMethodList[i].isSelected = false
                i++
            }

            withdrawMethod.isSelected = true
            mWithdrawMethodList[position] = withdrawMethod
            rvWithdrawMethod.adapter?.notifyDataSetChanged()

            if (mSelectedWithdrawMethod?.method == WithdrawMethod.METHOD.INSTANT) {
                refNumLayout.gone()
            } else {
                refNumLayout.gone()
            }
        }

        etAmount.text = etAmount.text
        isFormValid
    }

    private fun transferOmzet() {
        val intent = Intent(this, TransferBankConfirmationActivity::class.java)
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_DATA_BANK,
            Gson().toJson(mBankSelected)
        )
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_DATA_AMOUNT,
            DataUtil.convertLongFromCurrency(etAmount.getText().toString())
        )
        intent.putExtra(AppKeys.KEY_AMOUNT, etAmount.text.toString())
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_TRANSACTION_TYPE,
            TransferBankConfirmationActivity.TRANSACTION_TYPE.OMZET
        )
        startActivity(intent)
    }

    private fun sknTransfer() {
        val intent = Intent(this, TransferBankConfirmationActivity::class.java)
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_DATA_BANK,
            Gson().toJson(mBankSelected)
        )
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_DATA_AMOUNT,
            DataUtil.convertLongFromCurrency(etAmount.getText().toString())
        )
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_WITTHDRAW_METHOD,
            Gson().toJson(mSelectedWithdrawMethod)
        )
        intent.putExtra(AppKeys.KEY_AMOUNT, etAmount.text.toString())
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_TRANSACTION_TYPE,
            TransferBankConfirmationActivity.TRANSACTION_TYPE.SKN
        )
        startActivity(intent)
    }

    private fun withdrawRevenue() {
        var amount = DataUtil.FormattedCurrencyToDouble(etAmount.getText().toString())

        val intent = Intent(this, TransferBankConfirmationActivity::class.java)
        intent.putExtra(TransferBankConfirmationActivity.KEY_DATA_AMOUNT, amount)
        intent.putExtra(TransferBankConfirmationActivity.KEY_NAMA_BANK, nameAccount)
        intent.putExtra(TransferBankConfirmationActivity.KEY_NUMBER_BANK, numberAccount)
        intent.putExtra(TransferBankConfirmationActivity.KEY_BIN_BANK, binAccount)

        intent.putExtra(
            TransferBankConfirmationActivity.KEY_TRANSACTION_TYPE,
            TransferBankConfirmationActivity.TRANSACTION_TYPE.REVENUE
        )
        startActivity(intent)
    }

    private fun inquiryAjSuccess(transferBankInquiryResponse: TransferBankInquiryResponse) {
        val intent = Intent(this, TransferBankConfirmationActivity::class.java)
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_DATA_INQUIRY,
            Gson().toJson(transferBankInquiryResponse)
        )
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_DATA_BANK,
            Gson().toJson(mBankSelected)
        )
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_DATA_AMOUNT,
            DataUtil.convertLongFromCurrency(etAmount.getText().toString())
        )
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_WITTHDRAW_METHOD,
            Gson().toJson(mSelectedWithdrawMethod)
        )
        intent.putExtra(AppKeys.KEY_AMOUNT, etAmount.text.toString())
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_TRANSACTION_TYPE,
            TransferBankConfirmationActivity.TRANSACTION_TYPE.AJ
        )
        startActivity(intent)
    }

    private fun inquiryDepositAjSuccess(transferBankInquiryResponse: TransferBankInquiryResponse) {
        val intent = Intent(this, TransferBankConfirmationActivity::class.java)
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_DATA_INQUIRY,
            Gson().toJson(transferBankInquiryResponse)
        )
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_DATA_BANK,
            Gson().toJson(mBankSelected)
        )
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_DATA_AMOUNT,
            DataUtil.convertLongFromCurrency(etAmount.getText().toString())
        )
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_WITTHDRAW_METHOD,
            Gson().toJson(mSelectedWithdrawMethod)
        )
        intent.putExtra(AppKeys.KEY_AMOUNT, etAmount.text.toString())
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_TRANSACTION_TYPE,
            TransferBankConfirmationActivity.TRANSACTION_TYPE.DEPOSIT
        )
        startActivity(intent)
    }

    private fun inquiryOttokonekSuccess(transferBankInquiryResponse: TransferBankInquiryResponse) {
        val intent = Intent(this, TransferBankConfirmationActivity::class.java)
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_DATA_INQUIRY,
            Gson().toJson(transferBankInquiryResponse)
        )
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_DATA_BANK,
            Gson().toJson(mBankSelected)
        )
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_DATA_AMOUNT,
            DataUtil.CurrencyToDouble(etAmount.text.toString())
        )
        intent.putExtra(AppKeys.KEY_AMOUNT, etAmount.text.toString())
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_TRANSACTION_TYPE,
            TransferBankConfirmationActivity.TRANSACTION_TYPE.TRANSFER
        )
        startActivity(intent)
    }


    private fun getBalance() {
        if (isFromOmzet) {
            callApiReveneu()
            // TransactionDao(this).revenue(BaseDao.getInstance(this, KEY_GET_BALANCE).callback)
        } else {
            WalletDao(this).balance(BaseDao.getInstance(this, KEY_GET_BALANCE).callback)
        }
    }

    fun callApiReveneu() {
        OttoKonekAPI.revenue(this, object : ApiCallback<OttoKonekBalanceResponse>() {
            override fun onResponseSuccess(body: OttoKonekBalanceResponse?) {
                dismissProgressDialog()
                if (isSuccess200) {

                } else {
                    while (!isSuccess200 && tryCountBalance < 3) {

                        tryCountBalance++;
                        // retry the request
                        callApiReveneu()
                    }
                }
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                onApiResponseError()
            }

        })
    }


    private fun getBankList() {
        ProfileDao(this).getBankList(BaseDao.getInstance(this, KEY_API_GET_BANK_LIST).callback)
    }

    private fun getTransferLimit() {
        val KEY_DEPOSIT = "DEPOSIT"
        val KEY_OMZET = "OMZET"

        val requestModel = TransferLimitRequest()
        requestModel.key = KEY_DEPOSIT

        if (isFromOmzet) {
            requestModel.key = KEY_OMZET
        }

        TransferBankDao(this).transferLimit(
            requestModel,
            BaseDao.getInstance(this, KEY_API_TRANSFER_LIMIT).callback
        )
    }

    private fun callTransferBankAjInquiry() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        val requestModel = TransferBankInquiryRequest()
        requestModel.amount =
            DataUtil.convertLongFromCurrency(etAmount.getText().toString()).toDouble()
        requestModel.bankCode = mBankSelected?.bankCode
        requestModel.bankAccount = mBankSelected?.accountNumber
        requestModel.custRefNumber = etRefNum.text.toString()

        TransferBankDao(this).transferBankInquiry(
            requestModel,
            BaseDao.getInstance(this, KEY_API_TRANSFER_BANK_AJ_INQUIRY).callback
        )
    }

    private fun callTransferBankOttokonekInquiry() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        val requestModel = TransferBankInquiryRequest()
        requestModel.amount = DataUtil.CurrencyToDouble(etAmount.text.toString())
        requestModel.bankCode = mBankSelected?.bankCode
        requestModel.bankAccount = mBankSelected?.accountNumber
        requestModel.custRefNumber = etRefNum.text.toString()
        requestModel.accountName = mBankSelected?.accountName

//        TransferBankDao(this).transferBankInquiry(requestModel, BaseDao.getInstance(this, KEY_API_TRANSFER_BANK_AJ_INQUIRY).callback)

        OttoKonekAPI.transferBankInquiry(
            this,
            requestModel,
            object : ApiCallback<TransferBankInquiryResponse>() {
                override fun onResponseSuccess(body: TransferBankInquiryResponse?) {
                    dismissProgressDialog()

                    body?.let { body ->
                        if (isSuccess200) {
                            inquiryOttokonekSuccess(body)
                        } else {
                            val dialog = ErrorDialog(
                                this@WithdrawActivity,
                                this@WithdrawActivity,
                                false,
                                false,
                                body.meta?.message,
                                ""
                            )
                            dialog.show()
                        }
                    }
                }

                override fun onApiServiceFailed(throwable: Throwable?) {
                    dismissProgressDialog()
                    onApiResponseError()
                }
            })
    }

    private fun callTransferDepositAjInquiry() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        val requestModel = TransferBankInquiryRequest()
        requestModel.amount =
            DataUtil.convertLongFromCurrency(etAmount.getText().toString()).toDouble()
        requestModel.bankCode = mBankSelected?.bankCode
        requestModel.bankAccount = mBankSelected?.accountNumber
        requestModel.custRefNumber = etRefNum.text.toString()
        requestModel.phone = etPhone.text.toString()
        requestModel.latitude = myLastLocation.latitude
        requestModel.longitude = myLastLocation.longitude
        requestModel.ip = SystemUtil.getLocalIpAddress()

        TransferDepositDao(this).transferDepositInquiry(
            requestModel,
            BaseDao.getInstance(this, KEY_API_TRANSFER_DEPOSIT_AJ_INQUIRY).callback
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
                    KEY_GET_BALANCE -> if ((br as BaseResponseModel).meta.code == 200) {
                        if (isFromOmzet) {
                            getOmzetBalanceSuccess((br as OttoKonekBalanceResponse))
                        } else {
                            getDepositBalanceSuccess((br as OttoKonekBalanceResponse))
                        }
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.setOnDismissListener {
//                            getBalance()
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
                    KEY_API_TRANSFER_BANK_AJ_INQUIRY -> if ((br as BaseResponseModel).meta.code == 200) {
                        inquiryAjSuccess((br as TransferBankInquiryResponse))
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.show()
                    }
                    KEY_API_TRANSFER_DEPOSIT_AJ_INQUIRY -> if ((br as BaseResponseModel).meta.code == 200) {
                        inquiryDepositAjSuccess((br as TransferBankInquiryResponse))
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.show()
                    }
                    KEY_API_TRANSFER_LIMIT -> if ((br as BaseResponseModel).meta.code == 200) {
                        if (isFromOmzet) {
                            mBankLimit = (br as TransferLimitResponse).data?.bank
                            mOttoCashLimit = br.data?.ottocash
                        } else {
                            mBankLimit = (br as TransferLimitResponse).data?.deposit
                        }

                        displayWithDrawMethod()
                        viewAnimator.displayedChild = 1
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.setOnDismissListener {
                            finish()
                        }
                        dialog.show()
                    }
                }
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        onApiResponseError()
    }


    //endregion API Call
}
