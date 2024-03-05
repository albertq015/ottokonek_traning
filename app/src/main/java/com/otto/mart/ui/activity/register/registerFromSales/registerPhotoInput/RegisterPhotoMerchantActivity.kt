package com.otto.mart.ui.activity.register.registerFromSales.registerPhotoInput

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Request.PostAcquisitionRequest
import com.otto.mart.model.APIModel.Request.register.SignUpRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.SignupResponseModel
import com.otto.mart.model.APIModel.Response.register.SearchMerchantData
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.*
import com.otto.mart.ui.Partials.adapter.SpinnerAdapter
import com.otto.mart.ui.Partials.adapter.SpinnerData
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.login.OtpVerificationActivity
import com.otto.mart.ui.activity.register.StoreInformationActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.activity.syaratDanKetentuan.SnKActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.component.dialog.Popup
import kotlinx.android.synthetic.main.activity_register_photo_merchant.*
import kotlinx.android.synthetic.main.part_take_photo.view.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File

class RegisterPhotoMerchantActivity : AppActivity() {

    companion object {
        val KEY_DATA_POST_ACQUISITIO = "data_post_acquisitio"
    }

    private lateinit var discardPopup: Popup
    private val requestIDPhoto = 1
    private val requestStorePhoto = 2
    private val requestTNC = 3
    private val requestNewPin = 4
    var idCardPath: String? = null
    var storePath: String? = null
    private lateinit var merchantData: SearchMerchantData
    private var selectedIdType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_photo_merchant)
        initContent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                requestIDPhoto -> handleIdPhoto(data)
                requestStorePhoto -> handleStorePhoto(data)
                requestNewPin -> callRegisterApi(data)
                else -> if (isImageComplete() && isFormValid())
                    if (currentLocation){
                        //callPostAcquisitionApi()
                        gotoStoreInformation()
                    }
            }
        }
    }

    private fun initContent() {
        tvToolbarTitle.text = getString(R.string.title_register_photo_merchant)

        merchantData = Gson().fromJson(intent.getStringExtra("merchantData"), SearchMerchantData::class.java)

        discardPopup = Popup.getConfirmDialog(getString(R.string.text_confirmation), getString(R.string.message_discard_changes)).apply {
            negativeAction = ::onDiscardPopupYes
            negativeButton = this@RegisterPhotoMerchantActivity.getString(R.string.text_yes)
            positiveAction = ::onDiscardPopupNo
            positiveButton = this@RegisterPhotoMerchantActivity.getString(R.string.text_no)
            isHideBtnClose = true
        }

        btnToolbarBack.setOnClickListener { discardPopup.singleShow(supportFragmentManager, "discardPopup") }

        val data = mutableListOf<SpinnerData>().apply {
            add(SpinnerData("Government Issued ID", "government_id"))
            add(SpinnerData("Company ID", "company_id"))
            add(SpinnerData("CARD Membership ID", "card_member_id"))
        }

        spIdType.adapter = SpinnerAdapter(this, data, android.R.layout.simple_spinner_dropdown_item, SpinnerData("Select ID Type", "0"))
        spIdType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if(position >= 1){
                    selectedIdType = data[position - 1].code
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }

        }

        iclBtnIdPhoto.setOnClickListener { gotoTakePhoto("ktpimage", requestIDPhoto, true, R.layout.activity_id_photo, "ID Photo") }
        iclBtnStorePhoto.setOnClickListener { gotoTakePhoto("profileimage", requestStorePhoto, false, R.layout.activity_store_photo, "Store Photo") }
        iclIdPhoto.btnRetake.setOnClickListener { iclBtnIdPhoto.performClick() }
        iclStorePhoto.btnRetake.setOnClickListener { iclBtnStorePhoto.performClick() }

        btnNext.setOnClickListener {
            if (isValidInput) {
                gotoStoreInformation()
            }
        }
    }

    private val isValidInput: Boolean
        get() {
            var status = true

            if (selectedIdType == null) {
                "Select ID Type!".showToast(this)
                status = false
            } else if (etIdNumber.text.toString().equals("")) {
                "Select ID Number!".showToast(this)
                status = false
            } else if (!isImageComplete()) {
                status = false
            }

            return status
        }

    private fun gotoStoreInformation() {
        val postAcquisitionRequest = PostAcquisitionRequest(
                idCardPath,
                merchantData.phoneNumber,
                storePath,
                selectedIdType,
                getMyLastLocation().latitude,
                getMyLastLocation().longitude,
                etIdNumber.text.toString()
        )

        startActivity(Intent(this, StoreInformationActivity::class.java).apply {
            putExtra(SnKActivity.KEY_IS_FROM_REGISTER, true)
            putExtra(SnKActivity.KEY_URL_CONTENT, "https://otto-konek.s3-ap-southeast-1.amazonaws.com/tnc/otto_konek_tnc.html")
            putExtra(SnKActivity.KEY_IS_USE_BTN_CONFIRM, true)
            putExtra("merchantData", Gson().toJson(merchantData ))
            putExtra(KEY_DATA_POST_ACQUISITIO, Gson().toJson(postAcquisitionRequest))
        })
    }

    private fun gotoSnk() {
        startActivityForResult(Intent(this, SnKActivity::class.java).apply {
            putExtra(SnKActivity.KEY_IS_FROM_REGISTER, true)
            putExtra(SnKActivity.KEY_URL_CONTENT, "https://otto-konek.s3-ap-southeast-1.amazonaws.com/tnc/otto_konek_tnc.html")
            putExtra(SnKActivity.KEY_IS_USE_BTN_CONFIRM, true)
        }, requestTNC)
    }

    private fun gotoTakePhoto(key: String, requestCode: Int, cropped: Boolean, @LayoutRes resLayout: Int, title: String) {
        startActivityForResult(Intent(this, OpenCameraActivity::class.java).apply {
            putExtra("Key", key)
            putExtra("isCropEnable", cropped)
            putExtra("customLayout", resLayout)
            putExtra("title", title)
        }, requestCode)
    }

    private fun onDiscardPopupYes() = finish()

    private fun onDiscardPopupNo() = discardPopup.dismiss()
    private fun handleIdPhoto(intentData: Intent?) {
        intentData?.let { intent ->
            val path = intent.getStringExtra("ktpimage")
            path?.let {
                setImageToView(iclIdPhoto.ivPhoto, it)
                iclBtnIdPhoto.gone()
                iclIdPhoto.visible()
                idCardPath = path
            }
        }
    }


    private fun handleStorePhoto(intentData: Intent?) {
        intentData?.let { intent ->
            val path = intent.getStringExtra("profileimage")
            path?.let {
                setImageToView(iclStorePhoto.ivPhoto, it)
                iclBtnStorePhoto.gone()
                iclStorePhoto.visible()
                storePath = path
            }
        }
    }

    private fun gotoCreatePIN() {
        startActivityForResult(Intent(this, RegisterPinResetActivity::class.java).apply {
            putExtra("register", true)
        }, requestNewPin)
    }

    private fun callPostAcquisitionApi() {
        showProgressDialog(R.string.loading_message)
        val postAcquisitionRequest = PostAcquisitionRequest(
                getBase64Image(idCardPath),
                merchantData.phoneNumber,
                getBase64Image(storePath),
                selectedIdType,
                getMyLastLocation().latitude,
                getMyLastLocation().longitude,
                etIdNumber.text.toString()
        )

        OttoKonekAPI.postAcquisition(this, postAcquisitionRequest, object : ApiCallback<BaseResponseModel>() {
            override fun onResponseSuccess(body: BaseResponseModel?) {
                dismissProgressDialog()
                body?.let {
                    gotoCreatePIN()
                }
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                onApiResponseError()
            }
        })
    }

    private fun getBase64Image(imagePath: String?): String? {
        return ImageUtil.getByteArrayFromImageURL(imagePath, this)
    }

    private fun callRegisterApi(data: Intent?) {
        if (currentLocation) {
            data?.takeIf { it.hasExtra("pincode") }?.let {
                showProgressDialog(R.string.loading_message)
                val pincode = it.getStringExtra("pincode")
                val signupRequestModel = getSignUpRequest(pincode)

                OttoKonekAPI.signUp(this, signupRequestModel, object : ApiCallback<SignupResponseModel>() {
                    override fun onResponseSuccess(body: SignupResponseModel?) {
                        dismissProgressDialog()
                        if (isSuccess200) {
                            gotoOTP(body?.user_id, body?.requestId, pincode)
                        } else
                            ErrorDialog(this@RegisterPhotoMerchantActivity, this@RegisterPhotoMerchantActivity, false, false, body?.meta?.message, "").show()
                    }

                    override fun onApiServiceFailed(throwable: Throwable?) {
                        dismissProgressDialog()
                        onApiResponseError()
                    }
                })
            }
        }
    }

    private fun getSignUpRequest(pincode: String?): SignUpRequest =
            SignUpRequest(
                    merchantData.accountNumber,
                    merchantData.businessCategory?.toInt(),
                    pincode,
                    merchantData.bin,
                    merchantData.merchantAddress,
                    getMyLastLocation().latitude,
                    merchantData.municipality,
                    merchantData.barangay,
                    merchantData.merchantName,
                    merchantData.mid,
                    SessionManager.getFirebaseToken(),
                    merchantData.province,
                    false,
                    pincode,
                    merchantData.phoneNumber,
                    0,
                    merchantData.ownerName,
                    "",
                    getMyLastLocation().longitude,
                    merchantData.region
            )


    private fun gotoOTP(userId: Int?, requestId: String?, pincode: String?) {
        val intent = Intent(this, OtpVerificationActivity::class.java)
        intent.putExtra(OtpVerificationActivity.KEY_DATA_PHONE, merchantData.phoneNumber)
        intent.putExtra(OtpVerificationActivity.KEY_DATA_USER_ID, userId)
        intent.putExtra(OtpVerificationActivity.KEY_FROM_REGISTER, true)
        intent.putExtra("pin", pincode)
        intent.putExtra("data_reqId", requestId)
        startActivity(intent)
    }

    private fun setImageToView(view: ImageView, path: String) {
        val uri = Uri.parse(path)
        val file = File(uri.path)
        Glide.with(view).load(file).transform(RoundedCorners(8.px())).into(view)
    }

    fun isImageComplete(): Boolean {
        if (idCardPath == null || idCardPath?.isEmpty() == true) {
            showError("Please take id card picture")
            return false
        }

        if (storePath == null || storePath?.isEmpty() == true) {
            showError("Please take store picture")
            return false
        }

        return true
    }

    private fun isFormValid(): Boolean {
        if (etIdNumber.text.isNotEmpty()) return true

        showError("Id number must be not empty")
        return false
    }

    fun showError(errorMessage: String) {
        Popup().apply {
            title = errorMessage
            isHideBtnClose = true
            isHideMessage = true
        }.singleShow(supportFragmentManager, "error")
    }
}