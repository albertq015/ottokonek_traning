package com.otto.mart.ui.activity.multibank

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel
import com.otto.mart.model.APIModel.Request.multibank.InqueryTransferRequest
import com.otto.mart.model.APIModel.Response.multibank.AccountListResponse
import com.otto.mart.model.APIModel.Response.multibank.TransferBankInquiryMultiBankResponse
import com.otto.mart.support.util.*
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.cashOut.CashOutBankListActivity
import kotlinx.android.synthetic.main.button_select_bank.*
import kotlinx.android.synthetic.main.content_button_bank_to.*
import kotlinx.android.synthetic.main.content_transfer.*
import kotlinx.android.synthetic.main.toolbar.*

class TransferMenuAccountActivity : AppActivity() {
    companion object {
        val KEY_REQUEST_BANK_TO = 102
        val KEY_REQUEST_LINK_TO = 202

        val KEY_BANK_DATA_TO = "bank_data"
        val KEY_LINK_DATA_TO = "link_data"
    }

    var nameAccount = ""
    var logo = ""
    var numberAccount = ""
    var bin = ""
    var mBankSelected: BankAccountModel? = null
    var mAccountSelected: AccountListResponse.Data? = null

    var destinationAccountNumber = ""
    var destinationInstitutionId = ""
    var destinationInstitutionName = ""
    var binCheck :Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_account)

        nameAccount = intent?.getStringExtra("name").toString()
        numberAccount = intent?.getStringExtra("number").toString()
        logo = intent?.getStringExtra("logo").toString()
        bin = intent?.getStringExtra("bin").toString()

        init()
    }


    fun init() {
        bankSelectLayout.gone()
        bankSelectedLayout.visible()
        tvToolbarTitle.setText("Transfer")
        imgToolbarLeft.setOnClickListener {
            onBackPressed()
        }
        Glide.with(iv_logo_bank)
            .load(logo)
            .into(iv_logo_bank)

        tvBankName.setText(nameAccount)
        tvBankOwnerName.setText(DataUtil.setFormatAccountNumber(numberAccount))


        bankSelectedLayout.setOnClickListener {
            val intent = Intent(this, SelectedAccountListActivity::class.java)
            startActivityForResult(intent, KEY_REQUEST_BANK_TO)
        }
        bankSelectLayoutToBank.setOnClickListener {
            val intent = Intent(this, CashOutBankListActivity::class.java)
            intent.putExtra("transferBank", "transfer")
            intent.putExtra("bin", bin)
            startActivityForResult(intent, KEY_REQUEST_LINK_TO)
        }

        bankSelectedLayoutToBank.setOnClickListener {

            val intent = Intent(this, CashOutBankListActivity::class.java)
            intent.putExtra("transferBank", "transfer")
            intent.putExtra("bin", bin)
            startActivityForResult(intent, KEY_REQUEST_LINK_TO)
        }

        btnSubmit.setOnClickListener {
            if (isValidInput) {
                callApiTransferBank()
            }
        }


    }

    private val isValidInput: Boolean
        get() {
            var status = true

            if (etAmount.text.isEmpty()) {
                "Input Amount".showToast(this)
                status = false
            } else if (mBankSelected == null) {
                "Select Account!".showToast(this)
                status = false
            }
            return status
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //bank
        if (requestCode == KEY_REQUEST_BANK_TO) {

            data?.let {
                if (it.hasExtra(KEY_LINK_DATA_TO)) {
                    mAccountSelected = Gson().fromJson(
                        it.getStringExtra(
                            KEY_LINK_DATA_TO
                        ), AccountListResponse.Data::class.java
                    )
                    binCheck = true
                    displayLinkToBank(mAccountSelected)
                }
            }
            //akun
        } else if (requestCode == KEY_REQUEST_LINK_TO) {

            data?.let {
                if (it.hasExtra(KEY_BANK_DATA_TO)) {

                    mBankSelected = Gson().fromJson(
                        it.getStringExtra(
                            KEY_BANK_DATA_TO
                        ), BankAccountModel::class.java
                    )
                    displayBankToBank(mBankSelected)

                }
            }
        }
    }


    private fun displayLinkToBank(bank: AccountListResponse.Data?) {
        if (bank != null) {

            if(binCheck){
                bankSelectLayoutToBank.visible()
                bankSelectedLayoutToBank.gone()

                mAccountSelected = bank
                bin = bank.bin
                numberAccount =bank.accountNumber
                tvBankName.text = bank.accountName
                tvBankOwnerName.text = DataUtil.setFormatAccountNumber(bank?.accountNumber)
                Glide.with(iv_logo_bank)
                    .load(bank.logo)
                    .into(iv_logo_bank)
                bankSelectLayout.gone()
                bankSelectedLayout.visible()
            }else{
                mAccountSelected = bank
                bin = bank.bin
                tvBankName.text = bank.accountName
                tvBankOwnerName.text = DataUtil.setFormatAccountNumber(bank?.accountNumber)
                Glide.with(iv_logo_bank)
                    .load(bank.logo)
                    .into(iv_logo_bank)
                bankSelectLayout.gone()
                bankSelectedLayout.visible()
            }
        } else {
            bankSelectLayout.visible()
            bankSelectedLayout.gone()
        }
    }

    private fun displayBankToBank(bank: BankAccountModel?) {
        if (bank != null) {
            mBankSelected = bank

            destinationAccountNumber = bank.accountNumber ?: ""
            destinationInstitutionId = bank.bankCode ?: ""
            destinationInstitutionName = bank.bankName ?: ""

            tvBankNameToBank.text = bank.bankName
            tvBankOwnerNameToBank.text = DataUtil.setFormatAccountNumber(bank?.accountNumber)
            Glide.with(iv_logo_bankToBank)
                .load(bank.logo)
                .into(iv_logo_bankToBank)
            bankSelectLayoutToBank.gone()
            bankSelectedLayoutToBank.visible()


        } else {

            bankSelectLayoutToBank.visible()
            bankSelectedLayoutToBank.gone()
        }
    }

    fun callApiTransferBank() {
        showProgressDialog(getString(R.string.loading_message))
        val request = InqueryTransferRequest()
        request.bin = bin
        request.currency = ""
        request.destinationAccountName = ""
        request.destinationAccountNumber = destinationAccountNumber
        request.destinationInstitutionId = destinationInstitutionId
        request.destinationInstitutionName = destinationInstitutionName
        request.narrative = etDescription.text.toString()
        request.sourceAccountNumber = numberAccount
        request.transactionAmount = Integer.parseInt(etAmount.text.toString())

        OttoKonekAPI.inqueryTransferBank(
            this,
            request,
            object : ApiCallback<TransferBankInquiryMultiBankResponse>() {
                override fun onResponseSuccess(body: TransferBankInquiryMultiBankResponse?) {

                    dismissProgressDialog()
                    if (isSuccess200) {
                        body?.let {
                            gotoDetail(it)
                        }

                    } else {
                        onApiResponseError("Something Wrong")
                    }
                }

                override fun onApiServiceFailed(throwable: Throwable?) {
                    dismissProgressDialog()
                    onApiResponseError()
                }

            })
    }

    fun gotoDetail(data: TransferBankInquiryMultiBankResponse) {
        val intent = Intent(this, DetailConfirmationTransferActivity::class.java)
        intent.putExtra("accountName", destinationInstitutionName)
        intent.putExtra("accountNumber", numberAccount)
        intent.putExtra("bin", bin)
        intent.putExtra("data", Gson().toJson(data))
        startActivity(intent)

    }

}



