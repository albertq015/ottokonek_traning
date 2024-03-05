package com.otto.mart.ui.activity.qr.show

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import com.otto.mart.R
import com.otto.mart.model.APIModel.Request.QrStringRequestModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.QrStringResponseModel
import com.otto.mart.presenter.dao.BENIDAO
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.BitmapUtil
import com.otto.mart.support.util.DeviceUtil
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.showToast
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.login.LoginActivity
import com.otto.mart.ui.activity.qr.ShareQrActivity
import com.otto.mart.ui.activity.qr.scan.ScanQrActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.activity.transaction.alfamart.AlfamartShowPaymentCodeActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import github.nisrulz.screenshott.ScreenShott
import kotlinx.android.synthetic.main.content_show_qr.*
import kotlinx.android.synthetic.main.qr_payment_tab_menu.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response
import java.io.OutputStream

class ShowQrActivity : AppActivity() {

    var KEY_IS_SHARE = "is_share"

    val API_KEY_GET_STRING_QR = 100

    private val RC_PIN_SHOW_QR = 2011

    private var merchid: String? = null

    private var isShare = false

    override fun onCreate(savedInstanceState: Bundle?) {
        disableScreenshot()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_qr)

        if (intent.hasExtra(KEY_IS_SHARE)) {
            isShare = intent.getBooleanExtra(KEY_IS_SHARE, false)
        }

        initView()
        displayMerchantInfo()
    }

    override fun onResume() {
        super.onResume()
        disableScreenshot()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RC_PIN_SHOW_QR -> {
                    val intent = Intent(this, AlfamartShowPaymentCodeActivity::class.java)
                    intent.putExtra(AlfamartShowPaymentCodeActivity.KEY_IS_OTTOCASH, false)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_show_qr)

        tvTabQrCode.setTextColor(ContextCompat.getColor(this, R.color.white))
        tabQrCode.setBackground(ContextCompat.getDrawable(this, R.drawable.blue_rounded_12_bg))

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        btnTokenOttoPay.setOnClickListener {
            val jenk = Intent(this, RegisterPinResetActivity::class.java)
            jenk.putExtra("confirm", true)
            startActivityForResult(jenk, RC_PIN_SHOW_QR)
        }

        btnInputAmount.setOnClickListener {
            startActivity(Intent(this, ShowDynamicQrActivity::class.java))
        }

        if (SessionManager.getQrContent() == "") {
            callGetQrString(null, null)
        } else {
            displayQr(SessionManager.getQrContent())
        }

        tabScanQr.setOnClickListener {
            startActivity(Intent(this, ScanQrActivity::class.java))
            finish()
        }

        tabToken.setOnClickListener {
            val jenk = Intent(this, RegisterPinResetActivity::class.java)
            jenk.putExtra("confirm", true)
            startActivityForResult(jenk, RC_PIN_SHOW_QR)
        }

        btnShareQr.setOnClickListener {
            val intent = Intent(this, ShareQrActivity::class.java)
            intent.putExtra(ShareQrActivity.KEY_QR_CONTENT, SessionManager.getQrContent())
            intent.putExtra(ShareQrActivity.KEY_ACTION_TYPE, ShareQrActivity.ACTION.SHARE)
            startActivity(intent)
        }

        btnUnduhQr.setOnClickListener {
            val intent = Intent(this, ShareQrActivity::class.java)
            intent.putExtra(ShareQrActivity.KEY_QR_CONTENT, SessionManager.getQrContent())
            intent.putExtra(ShareQrActivity.KEY_ACTION_TYPE, ShareQrActivity.ACTION.DOWNLOAD)
            startActivity(intent)
        }

        if (isShare) {
            bottomLayout.gone()
            Handler().postDelayed({
                shareReceipt()
                finish()
            }, 500)
        }

        tvBinName.text = SessionManager.getPrefLogin().binName + " QR"
    }

    private fun displayQr(qrContent: String?) {
        imgQR.setImageBitmap(BitmapUtil.generateBitmap(qrContent, 9, DeviceUtil.dpToPx(300), DeviceUtil.dpToPx(300)))
        viewAnimator.displayedChild = 1
    }


    private fun displayMerchantInfo() {
        tvMPAN.gone()

        merchid = SessionManager.getUserProfile().merchant_id
        if (merchid == null) {
            "Terjadi kesalahan pada server, Mohon login ulang".showToast(this)
            SessionManager.logout()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val merchantName = SessionManager.getUserProfile().merchant_name
                ?: SessionManager.getUserProfile().merchant_name ?: ""
        tvMerchantName.text = SessionManager.getPrefLogin().merchant_name
//        SessionManager.getNMID()?.takeIf { it.isNotEmpty() }?.run { tvNMID.text = "NMID : ${this}" }
        tvMPAN.text = SessionManager.getMPAN()
//        tvCode.text = SessionManager.getTID()
    }

    private fun getQrStringSucess(qrStringResponseModel: QrStringResponseModel) {
        SessionManager.setQrContent(qrStringResponseModel.qr_string)
        displayQr(qrStringResponseModel.qr_string)
    }

    private fun shareReceipt() {
        // View with spaces as per constraints
        val bitmapView = ScreenShott.getInstance().takeScreenShotOfView(masterLayout)
        //val bitmapView = ScreenShott.getInstance().takeScreenShotOfRootView(masterLayout)
        //val bitmapView = BitmapUtil.getBitmapFromView(masterLayout, shareLayout.getChildAt(0).height, shareLayout.getChildAt(0).width)

        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val outstream: OutputStream?
        try {
            outstream = contentResolver.openOutputStream(uri!!)
            bitmapView.compress(Bitmap.CompressFormat.JPEG, 100, outstream)
            outstream!!.close()
        } catch (e: Exception) {
            System.err.println(e.toString())
        }

        share.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(share, getString(R.string.ppob_msg_share_it)))
    }


    //region API Call

    private fun callGetQrString(merchid: String?, payment: String?) {
        val requestModel = QrStringRequestModel()
        requestModel.amount = 0.0
        requestModel.feeAmount = 0
        requestModel.feePercentage = 0
        requestModel.storeLabel = SessionManager.getMerchantName()
        requestModel.bill_ref_num = ""

        BENIDAO(this).qrString(requestModel, BaseDao.getInstance(this, API_KEY_GET_STRING_QR).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        super.onApiResponseCallback(br, responseCode, response)
        if (responseCode == API_KEY_GET_STRING_QR) {
            if ((br as BaseResponseModel).meta.code == 200) {
                getQrStringSucess((br as QrStringResponseModel))
            } else {
                val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                dialog.show()
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        onApiResponseError()
        val errr = ErrorDialog(this, this, false, false, message, message)
        errr.show()

    }

    //endregion API Call
}