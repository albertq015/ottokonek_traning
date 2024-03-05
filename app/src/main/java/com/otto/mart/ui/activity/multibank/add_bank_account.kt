package com.otto.mart.ui.activity.multibank

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Request.multibank.AddBankInquiryRequest
import com.otto.mart.model.APIModel.Request.multibank.InqueryTransferRequest
import com.otto.mart.model.APIModel.Response.multibank.PartnerBankListResponse
import com.otto.mart.model.APIModel.Response.multibank.TransferBankInquiryMultiBankResponse
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.ApiCallback
import com.otto.mart.support.util.showToast
import com.otto.mart.ui.Partials.adapter.SpinnerAdapter
import com.otto.mart.ui.Partials.adapter.SpinnerData
import com.otto.mart.ui.activity.AppActivity
import kotlinx.android.synthetic.main.activity_add_bank_account.*
import kotlinx.android.synthetic.main.activity_add_link_account.*
import kotlinx.android.synthetic.main.activity_add_link_account.btnNext
import kotlinx.android.synthetic.main.activity_add_link_account.sp_bank_name
import kotlinx.android.synthetic.main.toolbar.*

class AddBankAccountActivity : AppActivity() {

    var mBankName = ""
    var mBin = ""
    var tryCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bank_account)

        init()
        callGetBankName()
    }

    fun init() {
        imgToolbarLeft.setOnClickListener {
            onBackPressed()
        }
        tvToolbarTitle.text = "Add Bank Account"
        btnNext.setOnClickListener {

            if (isValidInput) {
                inqueryBankAdd()
            }

        }
    }

    private val isValidInput: Boolean
        get() {
            var status = true

            if (mBankName.equals("")) {
                "Select Bank Name!".showToast(this)
                status = false
            } else if (et_account_number.text.length <= 15) {
                "Account Number must be 16 Digit!".showToast(this)
                status = false
            }
            return status
        }

    fun displayBankName(bankName: MutableList<PartnerBankListResponse.Data>) {
        val dataList = mutableListOf<SpinnerData>()

        for (data in bankName) {
            dataList.add(SpinnerData(data.partnerName, data.bin))
            mBankName = data.partnerName
            mBin = data.bin
        }

        sp_bank_name.adapter = SpinnerAdapter(
            this, dataList,
            android.R.layout.simple_spinner_dropdown_item,
            SpinnerData("Select Bank Name", "0")
        )

        sp_bank_name.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (position >= 1) {
                    mBankName = dataList[position - 1].name
                    mBin = dataList[position -1].code

                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
    }

    private fun callGetBankName() {
        showProgressDialog(R.string.loading_message)

        OttoKonekAPI.getListBankPartner(this, object : ApiCallback<PartnerBankListResponse?>() {


            override fun onApiServiceFailed(throwable: Throwable) {
                dismissProgressDialog()
                onApiResponseError()
                while (tryCount < 4) {

                    tryCount++;
                    // retry the request
                    callGetBankName()
                }
            }

            override fun onResponseSuccess(body: PartnerBankListResponse?) {
                dismissProgressDialog()
                body?.let {
                    displayBankName(it.data)
                }
            }
        });
    }

    fun inqueryBankAdd() {
        showProgressDialog(getString(R.string.loading_message))
        var accountNumberDestination = et_account_number.text.toString()
        val request = AddBankInquiryRequest()
        request.bin = mBin
//        request.currency = ""
//        request.destinationAccountName = ""
        request.destinationAccountNumber = accountNumberDestination
//        request.destinationInstitutionId = ""
//        request.destinationInstitutionName = ""
//        request.narrative = ""
//        request.sourceAccountNumber = SessionManager.getPrefLogin().account_number
//        request.transactionAmount = 0

        OttoKonekAPI.addBankInquery(
            this,
            request,
            object : ApiCallback<TransferBankInquiryMultiBankResponse>() {
                override fun onResponseSuccess(body: TransferBankInquiryMultiBankResponse?) {

                    dismissProgressDialog()
                    if (isSuccess200) {
                        body?.let {
                            gotoConfrim(mBin, it)
                        }
                    } else {
                        onApiResponseError(body?.meta?.message)
                    }


                }

                override fun onApiServiceFailed(throwable: Throwable?) {
                    dismissProgressDialog()
                    onApiResponseError()
                }

            })
    }


    fun gotoConfrim(bin: String, bankName: TransferBankInquiryMultiBankResponse) {
        val intent = Intent(
            this@AddBankAccountActivity,
            AddBankAccountConfrimActivity::class.java
        )
        intent.putExtra("bin", bin)
        intent.putExtra("data", Gson().toJson(bankName))
        intent.putExtra("bankName", mBankName)

        startActivity(intent)
    }
}