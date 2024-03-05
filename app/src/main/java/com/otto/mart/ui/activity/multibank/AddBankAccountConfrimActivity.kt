package com.otto.mart.ui.activity.multibank

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Request.multibank.AddBankAccountRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BasePaymentResponseModel
import com.otto.mart.model.APIModel.Response.multibank.TransferBankInquiryMultiBankResponse
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.ApiCallback
import com.otto.mart.support.util.widget.dialog.AlertDialogBottomSheet
import com.otto.mart.support.util.widget.dialog.ConfirmationDialogBottomSheet
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.add_bank_account_confrim_activity.*
import kotlinx.android.synthetic.main.toolbar.*

class AddBankAccountConfrimActivity : AppActivity() {

    var dataResponse = TransferBankInquiryMultiBankResponse()
    var bin: String = ""
    var bankName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_bank_account_confrim_activity)


        if (intent.hasExtra("data")) {
            dataResponse = Gson().fromJson(
                intent.getStringExtra("data"),
                TransferBankInquiryMultiBankResponse::class.java
            )
            bin = intent?.getStringExtra("bin").toString()
            bankName = intent?.getStringExtra("bankName").toString()

        }

        init()
    }

    fun init() {
        tvToolbarTitle.text = "Add bank Account"
        tvAccountHolderName.text = dataResponse.data?.destinationAccountName
        tvBankAccountNumber.text = dataResponse.data?.destinationAccountNumber
        tvBankAccountName.text = bankName


        imgToolbarLeft.setOnClickListener {
            onBackPressed()
        }
        btnSubmit.setOnClickListener {

            displayConfirmationDialog()
        }

    }

    fun callApiAddBankAccount() {
        showProgressDialog(R.string.loading_message)
        val request = AddBankAccountRequest()
        request.accountName = tvAccountHolderName.text.toString()
        request.accountNumber = tvBankAccountNumber.text.toString()
        request.bankCode = bin
        request.mid = SessionManager.getPrefLogin().mid
        request.notes = ""

        OttoKonekAPI.addBankAccount(
            this,
            request,
            object : ApiCallback<BasePaymentResponseModel>() {
                override fun onResponseSuccess(body: BasePaymentResponseModel?) {
                    dismissProgressDialog()
                    if (isSuccess200) {
                        displaySuccesDialog()
                    } else {
                        showErrorDialog("Error 400 Response")
                    }


                }

                override fun onApiServiceFailed(throwable: Throwable?) {
                    dismissProgressDialog()
                    showErrorDialog("Something Wrong")

                }

            })
    }

    private fun displayConfirmationDialog() {
        val dialog = ConfirmationDialogBottomSheet(this, supportFragmentManager)
        dialog.setIsShowIcon(false)
        dialog.setTitle("Add Bank Account")
        dialog.setMessage("Are you sure want to add this bank account?")
        dialog.setPositiveButton("Yes")
        dialog.setNegativeButton("No")
        dialog.setNegativeAction {
            dialog.isHidden
        }
        dialog.setPositiveAction {
            callApiAddBankAccount()
        }
        dialog.show()
    }

    private fun displaySuccesDialog() {
        val dialog = AlertDialogBottomSheet(this, supportFragmentManager)
        dialog.setTitle("Bank Added successfully")
        dialog.setMessage("You have successfully added a bank")
        dialog.setActionButton {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        dialog.show()
    }
}