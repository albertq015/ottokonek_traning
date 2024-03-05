package com.otto.mart.ui.activity.cashOut

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel
import com.otto.mart.model.APIModel.Request.cashOut.CashOutInputRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.cashOut.CashOutAmount
import com.otto.mart.model.APIModel.Response.cashOut.CashOutInput
import com.otto.mart.model.APIModel.Response.cashOut.CashOutInputResponse
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.presenter.dao.CashOutDao
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.formValidation.FormValidation
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.showToast
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.cashOut.CashOutAmountAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.multibank.*
import com.otto.mart.ui.activity.transaction.withdraw.WithdrawActivity.Companion.KEY_BA_POS
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.button_select_bank.*
import kotlinx.android.synthetic.main.content_cash_out_select_amount.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class CashOutSelectAmountActivity : AppActivity() {

    companion object {
        val KEY_REQUEST_BANK = 442
        val KEY_REQUEST_ACCOUNT_LIST = 300
        val KEY_BANK_DATA = "bank_data"
        val KEY_CASH_OUT_INPUT_DATA = "cash_input_data"
        val KEY_ACCOUNT_LIST = "post_item_data_cashout"
    }

    val KEY_API_CASH_OUT_INPUT = 200

    private var mIsValidationEnable = false
    val mAmountList = mutableListOf<CashOutAmount>()
    var selectedAmount: CashOutAmount? = null
    var mAmount = 0L
    var mBankSelected: BankAccountModel? = null
    var mAccountSelected: AccountListResponse? = null
    var mTextWatcher: TextWatcher? = null
    val presenter: ListLinkedBankAccountPresenter = ListLinkedBankAccountPresenterImpl(this)
    var accountNumber: String? = null
    var bin: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_out_select_amount)

        initView()
        initRecyclerview()
        displayAmountOption()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == KEY_REQUEST_BANK) {

                data?.let {
                    if (it.hasExtra(KEY_BANK_DATA)) {
                        mBankSelected = Gson().fromJson(
                            it.getStringExtra(KEY_BANK_DATA),
                            BankAccountModel::class.java
                        )
                        displayBank(mBankSelected)
                    }
                }
                data?.getIntExtra(KEY_BA_POS, -1).let {
                    displayAccountList(
                        presenter.getDataAccount().value?.get(it!!)?.object1?.binName,
                        presenter.getDataAccount().value?.get(it!!)?.object1?.accountNumber,
                        presenter.getDataAccount().value?.get(it!!)?.object2?.formatedBalance,
                        presenter.getDataAccount().value?.get(it!!)?.object1?.logo
                    )

                    accountNumber =
                        presenter.getDataAccount().value?.get(it!!)?.object1?.accountNumber
                    bin = presenter.getDataAccount().value?.get(it!!)?.object1?.bin
                }
            }
        }
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_cash_out_select_amount)
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
                isValidInput
            }

            override fun afterTextChanged(editable: Editable) {}
        }
        etAmount.addTextChangedListener(mTextWatcher)

        imgToolbarLeft.setOnClickListener {
            onBackPressed()
        }

        bankSelectLayout.setOnClickListener {
            gotoSelectBank()
        }

        btn_select_account.setOnClickListener {
            gotoSelectBank()
        }


        btnNext.setOnClickListener {
            mIsValidationEnable = true


            if (isValidInput) {
                callCashOutInput()
            }
        }
    }

    private fun initRecyclerview() {
        val glmOasisMenu = GridLayoutManager(this, 3)
        recyclerview.setLayoutManager(glmOasisMenu)
    }

    private val isValidInput: Boolean
        get() {


            var status = true
            val minAmount = 100
            val maxAmount = 10000
            val multipleAmount = 100


            var amount = 0L

            if (!etAmount.text.toString().equals("")) {
                amount = etAmount.text.toString().replace(getString(R.string.text_currency), "")
                    .replace(",", "").trim().toLong()
            }

            tvAmountError.gone()
            if (selectedAmount?.amount == null) {
                "Select Your Amount!".showToast(this)
                status = false
            } else if (amountLayout.visibility == View.VISIBLE) {
                if (!FormValidation(this).isRequired(amount.toString())) {
                    tvAmountError.text = getString(R.string.transfer_bank_msg_amount_is_required)
                    tvAmountError.visible()
                    status = false
                } else if (amount < minAmount) {
                    tvAmountError.text = "Minimun amount is " + DataUtil.convertCurrency(minAmount)
                    tvAmountError.visible()
                    status = false
                } else if (amount > maxAmount) {
                    tvAmountError.text = "Maximum amount is " + DataUtil.convertCurrency(maxAmount)
                    tvAmountError.visible()
                    status = false
                } else if (amount % multipleAmount != 0L) {
                    tvAmountError.text =
                        "Amount must be a multiple of " + DataUtil.convertCurrency(multipleAmount)
                    tvAmountError.visible()
                    status = false
                } else if (accountNumber == null) {
                    "Select bank account!".showToast(this)
                    status = false
                }
            } else if (accountNumber == null) {
                "Select bank account!".showToast(this)
                status = false

            } else {
                status = true
            }


            return status
        }

    private fun displayBank(bank: BankAccountModel?) {
        if (bank != null) {
            mBankSelected = bank
            tvBankName.text = bank.bankName
            tvBankOwnerName.text = DataUtil.setFormatAccountNumber(bank?.accountNumber)

            Glide.with(iv_logo_bank.context)
                .load(bank.logo)
                .into(iv_logo_bank)
            bankSelectLayout.gone()
            bankSelectedLayout.visible()
            //  isValidInput
        } else {
            bankSelectLayout.visible()
            bankSelectedLayout.gone()
        }
    }

    private fun displayAccountList(
        bankName: String?,
        numberAccount: String?,
        balance: String?,
        logo: String?
    ) {


        if (bankName != null) {

            tvBankName.text =
                bankName + " | " + DataUtil.setFormatAccountNumber(numberAccount)
            tvBankOwnerName.visibility = View.GONE
            Glide.with(iv_logo_bank.context)
                .load(logo)
                .into(iv_logo_bank)
            bankSelectLayout.gone()
            bankSelectedLayout.visible()
            //  isValidInput
        } else {
            bankSelectLayout.visible()
            bankSelectedLayout.gone()
        }
    }

    private fun displayAmountOption() {

        mAmountList.add(CashOutAmount("100", false))
        mAmountList.add(CashOutAmount("500", false))
        mAmountList.add(CashOutAmount("1,000", false))
        mAmountList.add(CashOutAmount("2,000", false))
        mAmountList.add(CashOutAmount("5,000", false))
        mAmountList.add(CashOutAmount("10,000", false))
        mAmountList.add(CashOutAmount("Other\nAmount", false))

        var adapter = CashOutAmountAdapter(this, mAmountList)
        recyclerview.adapter = adapter

        adapter.setmOnViewClickListener(object : CashOutAmountAdapter.OnViewClickListener {
            override fun onViewClickListener(data: CashOutAmount, position: Int) {
                amountSelected(data, position)
            }
        })
    }

    private fun amountSelected(data: CashOutAmount, position: Int) {
        selectedAmount = data

        if (position == (mAmountList.size - 1)) {
            amountLayout.visible()
        } else {
            amountLayout.gone()
        }

        var i = 0
        for (ottoagDenomModel in mAmountList) {
            if (i == position) {
                data.isSelected?.let {
                    mAmountList.get(i).isSelected = !it

                    mAmountList.get(i).isSelected?.let {
                        if (it) {
                            selectedAmount = data
                        } else {
                            selectedAmount = null
                        }
                    }
                }
            } else {
                mAmountList.get(i).isSelected = false
            }
            i++
        }

        val adapter = recyclerview.adapter
        adapter?.notifyDataSetChanged()
        mIsValidationEnable = true
        // isValidInput
    }

    private fun gotoSelectBank() {
        val intent = Intent(this, SelectAccountCashOutActivity::class.java)
        startActivityForResult(intent, KEY_REQUEST_BANK)
//        val intent = Intent(this, CashOutBankListActivity::class.java)
//        startActivityForResult(intent, KEY_REQUEST_BANK)
    }

    private fun cashOutInputSuccess(cashOutInput: CashOutInput) {
        cashOutInput.amount = mAmount
        val intent = Intent(this, CashOutConfirmationActivity::class.java)
        intent.putExtra(KEY_CASH_OUT_INPUT_DATA, cashOutInput)
        startActivity(intent)
    }


    //region API Call

    private fun callCashOutInput() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        if (amountLayout.visibility == View.VISIBLE) {
            mAmount = DataUtil.convertLongFromCurrency(etAmount.getText().toString())
        } else {
            mAmount = DataUtil.convertLongFromCurrency(selectedAmount?.amount)
        }

        val cashOutInputRequest = CashOutInputRequest()
        cashOutInputRequest.amount = mAmount
        cashOutInputRequest.account_number = accountNumber
        cashOutInputRequest.bin = bin

        CashOutDao(this).cashOutInput(
            cashOutInputRequest,
            BaseDao.getInstance(this, KEY_API_CASH_OUT_INPUT).callback
        )
    }

    override fun onApiResponseCallback(
        br: BaseResponse?,
        responseCode: Int,
        response: Response<*>?
    ) {
        ProgressDialogComponent.dismissProgressDialog(this)
        validateApiResponse(br)
        if (response != null && response.isSuccessful) {
            when (responseCode) {
                KEY_API_CASH_OUT_INPUT -> if ((br as BaseResponseModel).meta.code == 200) {
                    (br as CashOutInputResponse).data?.let {
                        cashOutInputSuccess(it)
                    }
                } else {
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.show()
                }
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        onApiResponseError()
    }
    //endregion API Call
}