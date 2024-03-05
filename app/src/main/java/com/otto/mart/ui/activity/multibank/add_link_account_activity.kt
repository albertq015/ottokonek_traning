package com.otto.mart.ui.activity.multibank

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Request.multibank.IssuerLinkedInquiryRequest
import com.otto.mart.model.APIModel.Response.multibank.AccountTypeListResponse
import com.otto.mart.model.APIModel.Response.multibank.PartnerBankListResponse
import com.otto.mart.model.APIModel.Response.multibank.TransferMultiBankConfrimResponse
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.ApiCallback
import com.otto.mart.support.util.showToast
import com.otto.mart.support.util.widget.dialog.ConfirmationDialogBottomSheet
import com.otto.mart.ui.Partials.adapter.SpinnerAdapter
import com.otto.mart.ui.Partials.adapter.SpinnerData
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import kotlinx.android.synthetic.main.activity_add_bank_account.*
import kotlinx.android.synthetic.main.activity_add_link_account.*
import kotlinx.android.synthetic.main.activity_add_link_account.btnNext
import kotlinx.android.synthetic.main.activity_add_link_account.sp_bank_name
import kotlinx.android.synthetic.main.toolbar.*

class AddLinkAccountActivity : AppActivity() {
    private var mBankName = ""
    private var mAccountType = ""
    private val KEY_API_ADD_LINK_ACCOUNT = 450


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_link_account)

        init()
        callGetBankName()


    }

    fun callPin() {
        val jenk = Intent(this, RegisterPinResetActivity::class.java)
        jenk.putExtra("AddAccountLink", true)
        jenk.putExtra("accountNumber", tv_account_number.text.toString())
        jenk.putExtra("accountType", mAccountType)
        jenk.putExtra("bankName", mBankName)
        startActivityForResult(jenk, KEY_API_ADD_LINK_ACCOUNT)
    }

    fun init() {
        tvToolbarTitle.text = "Add Link Account"
        imgToolbarLeft.setOnClickListener {
            onBackPressed()
        }

        btnNext.setOnClickListener {
            if (isValidInput) {
                callApiIssuerLinkedInquiry()
            }


        }

    }

    fun callApiIssuerLinkedInquiry(){
        showProgressDialog(getString(R.string.loading_message))
        val request = IssuerLinkedInquiryRequest()
        request.accountNumber = tv_account_number.text.toString()
        request.mid = SessionManager.getPrefLogin().mid

        OttoKonekAPI.issuerLinkedInquiry(
            this,request,
            object  : ApiCallback<TransferMultiBankConfrimResponse?>(){
                override fun onResponseSuccess(body: TransferMultiBankConfrimResponse?) {
                    dismissProgressDialog()

                    if (body?.meta?.code == 200){
                        displayConfirmationDialog()
                    }else{
                        onApiResponseError(body?.meta?.message)
                    }

                }

                override fun onApiServiceFailed(throwable: Throwable?) {
                    dismissProgressDialog()
                    onApiResponseError()
                }

            }
        )
    }

    private fun displayConfirmationDialog() {
        val dialog = ConfirmationDialogBottomSheet(this, supportFragmentManager)
        dialog.setIsShowIcon(false)
        dialog.setTitle("Add Link Account")
        dialog.setMessage("Are you sure want to add this link account?")
        dialog.setPositiveButton("Yes")
        dialog.setNegativeButton("No")
        dialog.setNegativeAction {
            dialog.isHidden
        }
        dialog.setPositiveAction {
            callPin()
        }
        dialog.show()
    }

    private val isValidInput: Boolean
        get() {
            var status = true

            if (mBankName.equals("")) {
                "Select Bank Name!".showToast(this)
                status = false
            } else if (mAccountType.equals("")) {
                "Select Account Type!".showToast(this)
                status = false
            } else if (tv_account_number.text.isEmpty()) {
                "Please Input Account Number!".showToast(this)
                status = false
            } else if (tv_account_number.text.length <= 15) {
                "Account Number must be 16 Digit!".showToast(this)
                status = false
            }


            return status
        }

    private fun callGetBankName() {
        showProgressDialog(R.string.loading_message)

        OttoKonekAPI.getListBankPartner(this, object : ApiCallback<PartnerBankListResponse?>() {

            override fun onResponseSuccess(body: PartnerBankListResponse?) {
                callGetAccountType()
                dismissProgressDialog()

                body?.let {
                    displayBankName(it.data)
                }

            }

            override fun onApiServiceFailed(throwable: Throwable) {
                dismissProgressDialog()
                onApiResponseError()
            }
        });


    }

    private fun displayBankName(bankName: MutableList<PartnerBankListResponse.Data>) {
        val dataList = mutableListOf<SpinnerData>()

        for (data in bankName) {
            dataList.add(SpinnerData(data.partnerName, data.bin))
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
                    mBankName = dataList[position - 1].code

                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
    }


    private fun callGetAccountType() {
        showProgressDialog(R.string.loading_message)

        OttoKonekAPI.getListAccountType(this, object : ApiCallback<AccountTypeListResponse?>() {


            override fun onApiServiceFailed(throwable: Throwable) {
                dismissProgressDialog()
                onApiResponseError()
            }

            override fun onResponseSuccess(body: AccountTypeListResponse?) {
                dismissProgressDialog()
                body?.let {
                    displayAccountType(it.data)
                }
            }
        });


    }

    private fun displayAccountType(bankName: MutableList<AccountTypeListResponse.Data>) {
        val dataList = mutableListOf<SpinnerData>()

        for (data in bankName) {
            dataList.add(SpinnerData(data.name, data.code))
        }

        sp_account_type.adapter = SpinnerAdapter(
            this, dataList,
            android.R.layout.simple_spinner_dropdown_item,
            SpinnerData("Select Account Type", "0")
        )

        sp_account_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (position >= 1) {
                    mAccountType = dataList[position - 1].code

                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
    }
}