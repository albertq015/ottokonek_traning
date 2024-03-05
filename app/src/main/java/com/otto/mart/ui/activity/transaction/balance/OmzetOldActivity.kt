package com.otto.mart.ui.activity.transaction.balance

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.PaymentData
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel
import com.otto.mart.model.APIModel.Request.OmzetSaldoRequestModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.OmzetSaldoResponseModel
import com.otto.mart.model.APIModel.Response.OmzetStatResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.presenter.dao.TransactionDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.visible
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.ppob.PpobPaymentSuccessActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.activity.transaction.history.HistoryActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.fragment.transaction.balance.TransferOmzetFragment
import kotlinx.android.synthetic.main.content_omzet_old.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class OmzetOldActivity : AppActivity() {

    val KEY_OMZET_STAT: Int = 100
    val KEY_TRANSFER_OMZET: Int = 101

    val mTransferOmzetFragment = TransferOmzetFragment()
    var mAmount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_omzet_old)

        initView()
        getOmzet()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppKeys.KEY_INPUT_PIN) {
                if (data != null) {
                    val pin = data.getStringExtra(AppKeys.KEY_PIN_CODE)
                    callTransferOmzet(pin!!)
                }
            }
        }
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_omzet)
        imgToolbarRight.setImageResource(R.drawable.icon_button_history)
        btnToolbarRight.visible()

        btnToolbarRight.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra(HistoryActivity.KEY_TITLE, HistoryActivity.TAB_LABEL_OMZET)
            startActivity(intent)
        }

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        btnTransfer.setOnClickListener {
            showTransferOmzetDialog()
        }
    }

    private fun inputPin() {
        val intent = Intent(this, RegisterPinResetActivity::class.java)
        intent.putExtra(AppKeys.KEY_INPUT_PIN_TYPE_INPUT, true)
        startActivityForResult(intent, AppKeys.KEY_INPUT_PIN)
    }

    private fun showTransferOmzetDialog() {
        mTransferOmzetFragment.show(supportFragmentManager, mTransferOmzetFragment.getTag())
        mTransferOmzetFragment.setOmzet(DataUtil.getInt(tvOmzet.text.toString()))
        mTransferOmzetFragment.setTransferOmzet(::transferOmzet)
    }

    private fun transferOmzet(amount: Int){
        mAmount = amount
        inputPin()
    }

    private fun getOmzetStatSuccess(omzetStatResponseModel: OmzetStatResponseModel) {
        tvOmzetNonTunai.text = omzetStatResponseModel.all_omset.replace("IDR", "").replace(",", ".")
        tvOmzet.text = omzetStatResponseModel.all_omset.replace("IDR", "").replace(",", ".")
        tvOmzetNonTunaiSmall.text = omzetStatResponseModel.all_omset.replace("IDR", "").replace(",", ".")
        tvOmzetTunai.text = omzetStatResponseModel.all_omset.replace("IDR", "").replace(",", ".")
        viewAnimator.displayedChild = 1
    }

    private fun transferOmzetSuccess(omzetSaldoResponseModel: OmzetSaldoResponseModel) {
        var ppobPaymentType = PpobProductType()
        ppobPaymentType.type = PpobProductType.QR_PAYMENT

        var ppobPayment = PpobPayment()
        ppobPayment.productName = "Transfer Omzet"
        ppobPayment.ppobProductType = ppobPaymentType
        ppobPayment.totalPayment = mAmount.toDouble()


        //Key Value List
        var keyValueList = mutableListOf<WidgetBuilderModel>()

        val serviceName = WidgetBuilderModel()
        serviceName.key = "Jenis Layanan"
        serviceName.value = "Pencairan Pendapatan Non-Tunai"
        keyValueList.add(serviceName)

        val nominal = WidgetBuilderModel()
        nominal.key = "Nominal"
        nominal.value = DataUtil.convertCurrency(mAmount.toLong())
        keyValueList.add(nominal)

        //End Key Value List//Key Value List

        var ppobOttoagPaymentResponseModel = PpobOttoagPaymentResponseModel()
        var data = PaymentData()
        data.keyValueList = keyValueList
        ppobOttoagPaymentResponseModel.data = data

        val intent = Intent(this, PpobPaymentSuccessActivity::class.java)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, ppobPayment)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA, Gson().toJson(ppobOttoagPaymentResponseModel))
        startActivity(intent)
    }


    //region API Call

    private fun getOmzet() {
        TransactionDao(this).omzetStatus(BaseDao.getInstance(this, KEY_OMZET_STAT).callback)
    }

    private fun callTransferOmzet(pin: String){
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

        var model = OmzetSaldoRequestModel()
        model.pin = pin
        model.userId = SessionManager.getUserId()
        model.amount = mAmount.toLong()

        TransactionDao(this).transOmzetToWallet(this, model, BaseDao.getInstance(this, KEY_TRANSFER_OMZET).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        ProgressDialogComponent.dismissProgressDialog(this)
        super.onApiResponseCallback(br, responseCode, response)
        when (responseCode) {
            KEY_OMZET_STAT -> if ((br as BaseResponseModel).meta.code == 200) {
                getOmzetStatSuccess((br as OmzetStatResponseModel))
            } else {
                val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                dialog.setOnDismissListener {
                    getOmzet()
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
        }
    }

    override fun onApiResponseError() {
        onApiResponseError()
    }

    //endregion
}
