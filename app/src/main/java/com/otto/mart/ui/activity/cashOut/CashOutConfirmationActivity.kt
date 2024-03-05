package com.otto.mart.ui.activity.cashOut

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel
import com.otto.mart.model.APIModel.Request.cashOut.CashOutInputConfirmationRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.cashOut.CashOutInput
import com.otto.mart.model.APIModel.Response.cashOut.CashOutInputConfirmationResponse
import com.otto.mart.model.APIModel.Response.cashOut.CashOutQr
import com.otto.mart.presenter.dao.CashOutDao
import com.otto.mart.support.util.DataUtil
import com.otto.mart.ui.Partials.adapter.ppob.PpobPaymentDetailAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.content_ppob_confirmation.*
import kotlinx.android.synthetic.main.content_setting.recyclerview
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class CashOutConfirmationActivity : AppActivity() {

    val KEY_API_CASH_OUT_INPUT_CONFIRMATION = 100

    var mPin = ""

    var mCashOutInput: CashOutInput? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_out_confirmation)

        //collect our intent
        mCashOutInput =
            intent.getParcelableExtra<Parcelable>(CashOutSelectAmountActivity.KEY_CASH_OUT_INPUT_DATA) as CashOutInput

        initView()
        initRecyclerview()
        displayPaymentInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppKeys.KEY_INPUT_PIN) {
                if (data != null) {
                    mPin = data?.getStringExtra(AppKeys.KEY_PIN_CODE)!!
                    callCashOutInputConfirmation()
                }
            }
        }
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_cash_out_confirmation)

        imgToolbarLeft.setOnClickListener {
            onBackPressed()
        }

        btnNext.setOnClickListener {
            inputPin()
        }
    }

    private fun initRecyclerview() {
        recyclerview.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun inputPin() {
        val intent = Intent(this, RegisterPinResetActivity::class.java)
        intent.putExtra(AppKeys.KEY_INPUT_PIN_TYPE_INPUT, true)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivityForResult(intent, AppKeys.KEY_INPUT_PIN)
    }


    private fun displayPaymentInfo() {
        var confirmationDataList = mutableListOf<WidgetBuilderModel>()

        val dateTime = WidgetBuilderModel()
        dateTime.key = "Date and Time"
        dateTime.value = mCashOutInput?.date_time
        confirmationDataList.add(dateTime)

        val customerId = WidgetBuilderModel()
        customerId.key = "Customer ID"
        customerId.value = mCashOutInput?.customer_id
        confirmationDataList.add(customerId)

        val custName = WidgetBuilderModel()
        custName.key = "Customer Name"
        custName.value = mCashOutInput?.customer_name
        confirmationDataList.add(custName)

        val accNumber = WidgetBuilderModel()
        accNumber.key = "Account Number"
        accNumber.value = mCashOutInput?.accountBank + " - " + mCashOutInput?.account_number
        confirmationDataList.add(accNumber)

        val amount = WidgetBuilderModel()
        amount.key = "Amount"
        amount.value = DataUtil.InputDecimal(mCashOutInput?.amount.toString())
        confirmationDataList.add(amount)

        val adapter = PpobPaymentDetailAdapter(this, confirmationDataList)
        adapter.orientation = PpobPaymentDetailAdapter.VERTICAL
        recyclerview.adapter = adapter
    }

    private fun cashOutConfirrmationSuccess(cashOutInputConfirmationResponse: CashOutInputConfirmationResponse) {
        val intent = Intent(this, CashOutQrDetailActivity::class.java)
        intent.putExtra(
            CashOutQrDetailActivity.KEY_CASH_OUT_DATA,
            cashOutInputConfirmationResponse.data
        )
        intent.putExtra(CashOutQrDetailActivity.KEY_IS_FROM_CONFIRMATION, true)
        startActivity(intent)
    }

    //region API Call

    private fun callCashOutInputConfirmation() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
            .show()

        val cashOutInputConfirmationRequest = CashOutInputConfirmationRequest()
        cashOutInputConfirmationRequest.account_number = mCashOutInput?.account_number
        cashOutInputConfirmationRequest.bin = mCashOutInput?.bin
        cashOutInputConfirmationRequest.amount = mCashOutInput?.amount?.toLong()
        cashOutInputConfirmationRequest.pin = mPin
        cashOutInputConfirmationRequest.rrn = mCashOutInput?.rrn

        CashOutDao(this).cashOutInputConfirmation(
            cashOutInputConfirmationRequest,
            BaseDao.getInstance(this, KEY_API_CASH_OUT_INPUT_CONFIRMATION).callback
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
                KEY_API_CASH_OUT_INPUT_CONFIRMATION -> if ((br as BaseResponseModel).meta.code == 200) {
                    (br as CashOutInputConfirmationResponse)?.let {
                        cashOutConfirrmationSuccess(it)
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