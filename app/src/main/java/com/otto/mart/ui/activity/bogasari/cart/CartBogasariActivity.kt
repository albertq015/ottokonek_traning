package com.otto.mart.ui.activity.bogasari.cart

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.PaymentData
import com.otto.mart.model.APIModel.Request.bogasari.BogasariInquiryProduct
import com.otto.mart.model.APIModel.Request.bogasari.BogasariPaymentRequestModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.model.APIModel.Response.bogasari.BogasariPaymentResponseModel
import com.otto.mart.model.APIModel.Response.ppob.PpobInquiryResponse
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.presenter.dao.BogasariDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.DataUtil
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.ppob.PpobPaymentSuccessActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.activity_cart_bogasari.*
import kotlinx.android.synthetic.main.part_total_bogasari.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response
import java.util.*

class CartBogasariActivity : AppActivity() {

    companion object {
        private const val RC_PIN = 1
        private const val RC_PAYMENT = 2
    }

    private lateinit var adapter: CartBogasariAdapter
    private var grandTotalValue = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_bogasari)
        initContent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == RC_PIN) {
            callPaymentAPI(intent.getStringExtra("productCode"), intent.getStringExtra("refNum"), data?.getStringExtra("pincode")
                    ?: "")
        }
    }

    private fun initContent() {
        tvToolbarTitle.text = getString(R.string.button_confirm_payment)
        imgToolbarLeft.setOnClickListener { finish() }
        totalAction.setOnClickListener {
            val intent = Intent(this@CartBogasariActivity, RegisterPinResetActivity::class.java)
            intent.putExtra("confirm", true)
            startActivityForResult(intent, RC_PIN)
        }

        val collectedProduct = intent?.getParcelableArrayListExtra<BogasariInquiryProduct>("collectedProduct")!!
        adapter = CartBogasariAdapter(collectedProduct)
        productList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        productList.adapter = adapter

        collectedProduct.let {
            grandTotalValue = it.sumByDouble {
                if (it.itemQuantity ?: 0 < 1) {
                    1.times(it.itemUnitPrice ?: 0).toDouble()
                } else {
                    (it.itemQuantity ?: 0).times(it.itemUnitPrice ?: 0).toDouble()
                }
            }
        }

        grandTotal.text = DataUtil.convertCurrency(grandTotalValue)
        tv_saldo.text = intent.getStringExtra("wallet")
    }

    private fun callPaymentAPI(productCode: String?, refNum: String?, pin: String) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
        val model = BogasariPaymentRequestModel(
                productCode,
                SessionManager.getWalletID(),
                DataUtil.getDigit(grandTotal.text.toString()).toLong(),
                DataUtil.getDigit(grandTotal.text.toString()).toLong(),
                0,
                pin,
                refNum
        )

        BogasariDao(this).getBogasariPayment(model, BaseDao.getInstance(this, RC_PAYMENT).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        ProgressDialogComponent.dismissProgressDialog(this)

        if ((!response?.isSuccessful()!!) || ((br as BaseResponseModel).meta.code != 200)) {
            var errorMessage = getString(R.string.error_msg_something_wrong)
            br?.let {
                if ((br as BaseResponseModel) != null) {
                    errorMessage = br.meta.message
                }
            }
            val dialog = ErrorDialog(this, this@CartBogasariActivity, false, false, getString(R.string.error_msg_something_wrong), errorMessage)
            dialog.setOnDismissListener {
                finish()
            }
            dialog.show()
            return
        }

        br?.let {
            if (responseCode == RC_PAYMENT) {
                handlePayment(br as BogasariPaymentResponseModel)
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        ProgressDialogComponent.dismissProgressDialog(this)
        super.onApiFailureCallback(message, ac)
    }

    private fun handlePayment(bogasariPaymentResponseModel: BogasariPaymentResponseModel) {
//        val intent = Intent(this, BogasariTPActivity::class.java)
//        intent.putExtra("payment", bogasariInquiryResponseModel.data?.inquiryData)
//        startActivity(intent)

        val data = PpobOttoagPaymentResponseModel()
        data.data = PaymentData()
        data.data.keyValueList = bogasariPaymentResponseModel.data?.keyValueList

        val paymentData = PpobPayment()
        paymentData.totalPayment = grandTotalValue

        val response= PpobInquiryResponse()
        response.data=PpobInquiryResponse.Data()
        response.data.total=DataUtil.convertCurrency(grandTotalValue)

        val intent = Intent(this, PpobPaymentSuccessActivity::class.java)
        intent.putExtra("data", bogasariPaymentResponseModel.data?.keyValueList as ArrayList<out Parcelable>)
        intent.putExtra("isBogasari", true)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA, Gson().toJson(data))
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA,paymentData)
        intent.putExtra(AppKeys.KEY_PPOB_INQUIRY_DATA,Gson().toJson(response))
        startActivity(intent)
        finish()
    }
}
