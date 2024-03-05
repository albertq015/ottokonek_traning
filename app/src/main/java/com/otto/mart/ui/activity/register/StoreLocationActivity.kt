package com.otto.mart.ui.activity.register

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Request.PostAcquisitionRequest
import com.otto.mart.model.APIModel.Request.register.SignUpRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.SignupResponseModel
import com.otto.mart.model.APIModel.Response.register.SearchMerchantData
import com.otto.mart.model.APIModel.Response.storeLocation.BarangayResponse
import com.otto.mart.model.APIModel.Response.storeLocation.MunicipalityResponse
import com.otto.mart.model.APIModel.Response.storeLocation.ProvinceResponse
import com.otto.mart.model.APIModel.Response.storeLocation.RegionalResponse
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.*
import com.otto.mart.ui.Partials.adapter.SpinnerAdapter
import com.otto.mart.ui.Partials.adapter.SpinnerData
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.login.OtpVerificationActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.activity.register.registerFromSales.registerPhotoInput.RegisterPhotoMerchantActivity
import com.otto.mart.ui.activity.syaratDanKetentuan.SnKActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.activity_store_information.btnNext
import kotlinx.android.synthetic.main.activity_store_location.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

private var trycount = 0

class StoreLocationActivity : AppActivity() {

    private var mRegional = ""
    private var mProvince = ""
    private var mMunicipality = ""
    private var mBarangay = ""

    private val requestTNC = 3
    private val requestNewPin = 4

    var postAcquisitionRequest = PostAcquisitionRequest()
    var merchantData = SearchMerchantData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_location)

        if (intent.hasExtra(RegisterPhotoMerchantActivity.KEY_DATA_POST_ACQUISITIO)) {
            var dataJson =
                intent.getStringExtra(RegisterPhotoMerchantActivity.KEY_DATA_POST_ACQUISITIO)
            postAcquisitionRequest = Gson().fromJson(dataJson, PostAcquisitionRequest::class.java)
        }

        merchantData =
            Gson().fromJson(intent.getStringExtra("merchantData"), SearchMerchantData::class.java)

        initLocation()
        callGetAllRegion()
        initProvince()
        initMunicipality()
        initBarangay()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                requestNewPin -> callRegisterApi(data)
                else -> if (currentLocation) callPostAcquisitionApi()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        btnNext.visibility = View.VISIBLE
    }

    private fun initLocation() {
        tvToolbarTitle.text = "Store Location"

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        btnNext.setOnClickListener {
            if (isValidInput) {
                btnNext.gone()
                gotoSnk()
            }
        }
    }

    private val isValidInput: Boolean
        get() {
            var status = true

            if (mRegional.equals("")) {
                "Select regional!".showToast(this)
                status = false
            } else if (mProvince.equals("")) {
                "Select province!".showToast(this)
                status = false
            } else if (mMunicipality.equals("")) {
                "Select municipality!".showToast(this)
                status = false
            } else if (mBarangay.equals("")) {
                "Select barangay!".showToast(this)
                status = false
            }

            return status
        }

    private fun displayRegional(regionals: MutableList<RegionalResponse.Data>) {
        val dataList = mutableListOf<SpinnerData>()

        for (data in regionals) {
            dataList.add(SpinnerData(data.name, data.code))
        }

        spRegional.adapter = SpinnerAdapter(
            this, dataList,
            android.R.layout.simple_spinner_dropdown_item,
            SpinnerData("Select regional", "0")
        )

        spRegional.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (position >= 1) {
                    mRegional = dataList[position - 1].code
                    callGetProvince(mRegional)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
    }

    private fun initProvince() {
        val dataList = mutableListOf<SpinnerData>()

        spProvince.adapter = SpinnerAdapter(
            this, dataList,
            android.R.layout.simple_spinner_dropdown_item,
            SpinnerData("Select province", "0")
        )
    }

    private fun displayProvince(provinces: MutableList<ProvinceResponse.Data>) {
        val dataList = mutableListOf<SpinnerData>()

        for (province in provinces) {
            dataList.add(SpinnerData(province.name, province.code))
        }

        spProvince.adapter = SpinnerAdapter(
            this, dataList,
            android.R.layout.simple_spinner_dropdown_item,
            SpinnerData("Select province", "0")
        )

        spProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (position >= 1) {
                    mProvince = dataList[position - 1].code
                    callGetMunicipality(mProvince)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
    }

    private fun initMunicipality() {
        val dataList = mutableListOf<SpinnerData>()

        spMunicipality.adapter = SpinnerAdapter(
            this, dataList,
            android.R.layout.simple_spinner_dropdown_item,
            SpinnerData("Select municipality", "0")
        )
    }

    private fun displayMunicipality(municipalities: MutableList<MunicipalityResponse.Data>) {
        val dataList = mutableListOf<SpinnerData>()

        for (municipality in municipalities) {
            dataList.add(SpinnerData(municipality.name, municipality.code))
        }

        spMunicipality.adapter = SpinnerAdapter(
            this, dataList,
            android.R.layout.simple_spinner_dropdown_item,
            SpinnerData("Select municipality", "0")
        )

        spMunicipality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (position >= 1) {
                    mMunicipality = dataList[position - 1].code
                    callGetBarangays(mMunicipality)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
    }

    private fun initBarangay() {
        val dataList = mutableListOf<SpinnerData>()

        spBarangay.adapter = SpinnerAdapter(
            this, dataList,
            android.R.layout.simple_spinner_dropdown_item,
            SpinnerData("Select barangay", "0")
        )
    }

    private fun displayBarangay(barngays: MutableList<BarangayResponse.Data>) {
        val dataList = mutableListOf<SpinnerData>()

        for (barngay in barngays) {
            dataList.add(SpinnerData(barngay.name, barngay.code))
        }

        spBarangay.adapter = SpinnerAdapter(
            this, dataList,
            android.R.layout.simple_spinner_dropdown_item,
            SpinnerData("Select barangay", "0")
        )

        spBarangay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (position >= 1) {
                    mBarangay = dataList[position - 1].code
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
    }

    private fun gotoStoreInformation() {
        startActivityForResult(Intent(this, StoreInformationActivity::class.java).apply {
            putExtra(SnKActivity.KEY_IS_FROM_REGISTER, true)
            putExtra(
                SnKActivity.KEY_URL_CONTENT,
                "https://otto-konek.s3-ap-southeast-1.amazonaws.com/tnc/otto_konek_tnc.html"
            )
            putExtra(SnKActivity.KEY_IS_USE_BTN_CONFIRM, true)
        }, requestTNC)
    }

    private fun gotoSnk() {
        startActivityForResult(Intent(this, SnKActivity::class.java).apply {
            putExtra(SnKActivity.KEY_IS_FROM_REGISTER, true)
            putExtra(
                SnKActivity.KEY_URL_CONTENT,
                "https://otto-konek.s3-ap-southeast-1.amazonaws.com/tnc/otto_konek_tnc.html"
            )
            putExtra(SnKActivity.KEY_IS_USE_BTN_CONFIRM, true)
        }, requestTNC)
    }

    private fun gotoCreatePIN() {
        startActivityForResult(Intent(this, RegisterPinResetActivity::class.java).apply {
            putExtra("register", true)
        }, requestNewPin)
    }

    private fun getSignUpRequest(pincode: String?): SignUpRequest =
        SignUpRequest(
            merchantData.accountNumber,
            postAcquisitionRequest.business_category?.toInt(),
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

    private fun getBase64Image(imagePath: String?): String? {
        return ImageUtil.getByteArrayFromImageURL(imagePath, this)
    }


    private fun callGetAllRegion() {
        showProgressDialog(R.string.loading_message)

        OttoKonekAPI.getRegional(this, object : ApiRoseCallback<RegionalResponse>() {
            override fun onResponseSuccess(body: RegionalResponse?) {
                dismissProgressDialog()

                if (isSuccess200) {
                    body?.let {
                        displayRegional(it.data)
                    }


//                while (!isSuccess200 && tryCount < 5) {
//
//                    tryCount++;
//                    // retry the request
//                  callGetAllRegion()
//                }

                }
                //  alert(body?.msg)

            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                //onApiResponseError()
                retryRegionApi("Server Error")
            }
        })


    }


    private fun retryRegionApi(message: String) {
        if (trycount <= 2) {
            trycount++
            Handler(Looper.getMainLooper()).postDelayed({
                callGetAllRegion()
            }, 1000)

        } else {

            ErrorDialog(
                this@StoreLocationActivity,
                this@StoreLocationActivity,
                false,
                false,
                message,
                ""
            ).show()
        }

    }


    private fun callGetProvince(code: String) {
        showProgressDialog(R.string.loading_message)

        OttoKonekAPI.getProvince(this, code, object : ApiRoseCallback<ProvinceResponse>() {
            override fun onResponseSuccess(body: ProvinceResponse?) {
                dismissProgressDialog()
                if (isSuccess200) {
                    body?.let {
                        displayProvince(it.data)
                    }
                } else
                //  alert(body?.msg)
                    ErrorDialog(
                        this@StoreLocationActivity,
                        this@StoreLocationActivity,
                        false,
                        false,
                        body?.msg,
                        ""
                    ).show()
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                onApiResponseError()
            }
        })
    }

    private fun callGetMunicipality(code: String) {
        showProgressDialog(R.string.loading_message)

        OttoKonekAPI.getMunicipality(
            this,
            code,
            object : ApiRoseCallback<MunicipalityResponse>() {
                override fun onResponseSuccess(body: MunicipalityResponse?) {
                    dismissProgressDialog()
                    if (isSuccess200) {
                        body?.let {
                            displayMunicipality(it.data)
                        }
                    } else
                    //   alert(body?.msg)
                        ErrorDialog(
                            this@StoreLocationActivity,
                            this@StoreLocationActivity,
                            false,
                            false,
                            body?.msg,
                            ""
                        ).show()
                }

                override fun onApiServiceFailed(throwable: Throwable?) {
                    dismissProgressDialog()
                    onApiResponseError()
                }
            })
    }

    private fun callGetBarangays(code: String) {
        showProgressDialog(R.string.loading_message)

        OttoKonekAPI.getBarangays(this, code, object : ApiRoseCallback<BarangayResponse>() {
            override fun onResponseSuccess(body: BarangayResponse?) {
                dismissProgressDialog()
                if (isSuccess200) {
                    body?.let {
                        displayBarangay(it.data)
                    }
                } else
                // alert(body?.msg)
                    ErrorDialog(
                        this@StoreLocationActivity,
                        this@StoreLocationActivity,
                        false,
                        false,
                        body?.msg,
                        ""
                    ).show()
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                onApiResponseError()
            }
        })
    }

    private fun callPostAcquisitionApi() {
        showProgressDialog(R.string.loading_message)
        if (!postAcquisitionRequest.id_card_image?.contains("data:image/jpg;base64")!! && !postAcquisitionRequest.merchant_image?.contains(
                "data:image/jpg;base64"
            )!!
        ) {
            postAcquisitionRequest.id_card_image =
                getBase64Image(postAcquisitionRequest.id_card_image)
            postAcquisitionRequest.merchant_image =
                getBase64Image(postAcquisitionRequest.merchant_image)
        }
        postAcquisitionRequest.region = mRegional
        postAcquisitionRequest.province = mProvince
        postAcquisitionRequest.municipality = mMunicipality
        postAcquisitionRequest.barangay = mBarangay


        OttoKonekAPI.postAcquisition(
            this,
            postAcquisitionRequest,
            object : ApiCallback<BaseResponseModel>() {
                override fun onResponseSuccess(body: BaseResponseModel?) {
                    dismissProgressDialog()
                    btnNext.visible()
                    body?.let {
                        gotoCreatePIN()
                    }
                }

                override fun onApiServiceFailed(throwable: Throwable?) {
                    dismissProgressDialog()
                    btnNext.visible()
                    onApiResponseError()
                }

            })
    }

    private fun callRegisterApi(data: Intent?) {
        if (currentLocation) {
            data?.takeIf { it.hasExtra("pincode") }?.let {
                showProgressDialog(R.string.loading_message)
                val pincode = it.getStringExtra("pincode")
                val signupRequestModel = getSignUpRequest(pincode)

                OttoKonekAPI.signUp(
                    this,
                    signupRequestModel,
                    object : ApiCallback<SignupResponseModel>() {
                        override fun onResponseSuccess(body: SignupResponseModel?) {
                            dismissProgressDialog()
                            if (isSuccess200) {
                                gotoOTP(body?.user_id, body?.requestId, pincode)
                            }

//                            else if (body?.meta?.code == 99){
//                                showErrorDialog("Ini masuk ke otp menu")
//                            }

                            else {
                                ErrorDialog(
                                    this@StoreLocationActivity,
                                    this@StoreLocationActivity,
                                    false,
                                    false,
                                    body?.meta?.message,
                                    ""
                                ).show()

                            }

                        }

                        override fun onApiServiceFailed(throwable: Throwable?) {
                            dismissProgressDialog()
                            onApiResponseError()
                        }
                    })
            }
        }
    }

    private fun alert(message: String?) {

        val alertDialogBuilder = AlertDialog.Builder(
            this@StoreLocationActivity
        )
        alertDialogBuilder.setIcon(R.drawable.icon_warning)
        // set dialog message
        alertDialogBuilder
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Close") { dialog, id -> // if this button is clicked, close
                // current activity
                this@StoreLocationActivity.finish()
            }


        // create alert dialog
        val alertDialog = alertDialogBuilder.create()

        // show it
        alertDialog.show()
    }

}