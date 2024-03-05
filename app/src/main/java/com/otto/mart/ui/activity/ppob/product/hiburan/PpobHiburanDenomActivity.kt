package com.otto.mart.ui.activity.ppob.product.hiburan

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.core.content.ContextCompat
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.OttoagDenomModel
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel
import com.otto.mart.model.APIModel.Request.ppob.PpobInquiryRequest
import com.otto.mart.model.APIModel.Request.ppob.PpobUserGuideRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagDenomResponseModel
import com.otto.mart.model.APIModel.Response.ppob.PpobInquiryResponse
import com.otto.mart.model.APIModel.Response.ppob.PpobUserGuideResponse
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.presenter.dao.PpobDao
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.ppob.PpobHiburanDenomAdapter
import com.otto.mart.ui.activity.PpobActivity
import com.otto.mart.ui.activity.ppob.PpobConfirmationActivity
import com.otto.mart.ui.activity.ppob.setup.PpobPaymentMethodSetup
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.content_ppob_hiburan_denom.*
import kotlinx.android.synthetic.main.toolbar.btnToolbarBack
import kotlinx.android.synthetic.main.toolbar.tvToolbarTitle
import kotlinx.android.synthetic.main.toolbar_ppob.btnToolbarRight
import kotlinx.android.synthetic.main.toolbar_ppob.imgToolbarRight
import retrofit2.Response

class PpobHiburanDenomActivity : PpobActivity() {

    val API_KEY_PPOB_DENOM_LIST: Int = 100
    val API_KEY_PPOB_USER_GUIDE: Int = 101
    val KEY_PPOB_INQUIRY: Int = 102

    private var mPpobProductType: PpobProductType = PpobProductType()
    var mSelectedProvider: OttoagDenomModel? = null
    var mSelectedDenom: OttoagDenomModel? = null

    var mDenomList = mutableListOf<OttoagDenomModel>()
    var mPpobPayment: PpobPayment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppob_hiburan_denom)

        if (intent.hasExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA)) {
            mPpobProductType = intent.getParcelableExtra<Parcelable>(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA) as PpobProductType
        }

        if (intent.hasExtra(AppKeys.KEY_PPOB_PROVIDER_DATA)) {
            mSelectedProvider = Gson().fromJson(intent.getStringExtra(AppKeys.KEY_PPOB_PROVIDER_DATA), OttoagDenomModel::class.java)
        }

        initData()
        initView()
        initRecyclerView()
        getDenomList()
        getUserGuide()
    }

    private fun initData() {
        mPpobPayment = PpobPayment(
                "0", "",
                0, null,
                null,
                null,
                false,
                mPpobProductType,
                PpobPaymentMethodSetup().getPaymentMethod().get(0),
                null,
                null,
                null
        )
    }

    private fun initView() {
        tvToolbarTitle.text = mPpobProductType.name
        imgToolbarRight.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_all_menu_ppob))
        btnToolbarRight.visible()

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        btnToolbarRight.setOnClickListener {
            showAllPpobMenu(mPpobProductType.name)
        }

        btnNext.setOnClickListener {
            callInquiry()
        }
    }

    private fun initRecyclerView() {
        recyclerview.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        recyclerview.setLayoutManager(linearLayoutManager)
    }

    private fun displayDenomList(denomination: MutableList<OttoagDenomModel>) {
        mDenomList = denomination

        val adapter = PpobHiburanDenomAdapter(this, denomination)
        adapter.itemSelected = ::denomSelected
        recyclerview.adapter = adapter
        viewAnimator.displayedChild = 1
    }

    private fun denomSelected(denom: OttoagDenomModel, position: Int) {
        mSelectedDenom = denom

        var i = 0
        for (ottoagDenomModel in mDenomList) {
            if (i == position) {
                mDenomList.get(i).isSelected = !denom.isSelected
            } else {
                mDenomList.get(i).isSelected = false
            }
            i++
        }

        if (!mDenomList.get(position).isSelected) {
            mSelectedDenom = null
        }

        val adapter = recyclerview.adapter
        adapter!!.notifyDataSetChanged()

        if (mSelectedDenom == null) {
            btnNext.isEnabled = false
            btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)
        } else {
            btnNext.isEnabled = true
            btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
        }
    }

    private fun displayUserGuide(ppobUserGuideResponse: PpobUserGuideResponse) {
        tvInstructionTitle.text = ppobUserGuideResponse.data.operator
        tvInstructionDesc.text = ppobUserGuideResponse.data.user_guide
        instructionLayout.visible()
    }

    private fun inquirySuccess(ppobInquiryResponse: PpobInquiryResponse) {
        var keyValueList = mutableListOf<WidgetBuilderModel>()

        var produk = WidgetBuilderModel()
        produk.key = "Produk"
        produk.value = ppobInquiryResponse.data.list_detail.name
        keyValueList.add(produk)

        var nominal = WidgetBuilderModel()
        nominal.key = "Nominal"
        nominal.value = DataUtil.convertCurrency(ppobInquiryResponse.data.list_detail.salesPrice)
        keyValueList.add(nominal)

        var adminFee = WidgetBuilderModel()
        adminFee.key = "Admin Fee"
        adminFee.value = DataUtil.convertCurrency(ppobInquiryResponse.data.admin_fee)
        keyValueList.add(adminFee)

        ppobInquiryResponse.data.key_value_list = keyValueList

        val intent = Intent(this, PpobConfirmationActivity::class.java)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, mPpobPayment)
        intent.putExtra(AppKeys.KEY_PPOB_INQUIRY_DATA, Gson().toJson(ppobInquiryResponse))
        startActivity(intent)
    }


    //region API Call

    private fun getDenomList() {
        val productName = mPpobProductType.code?.toUpperCase() + "DENOM"
        PpobDao(this).billerProductListCashback(productName, "", mSelectedProvider?.product_code, "", BaseDao.getInstance(this, API_KEY_PPOB_DENOM_LIST).callback)
    }

    private fun getUserGuide() {
        val ppobUserGuide = PpobUserGuideRequest()
        ppobUserGuide.operator = mSelectedProvider?.product_code
        PpobDao(this).userGuide(ppobUserGuide, BaseDao.getInstance(this, API_KEY_PPOB_USER_GUIDE).callback)
    }

    private fun callInquiry() {
        mPpobPayment?.let {
            ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

            val requestModel = PpobInquiryRequest()
            requestModel.cust_id = it.custNumber
            requestModel.product_code = mSelectedDenom?.product_code ?: ""
            requestModel.type = it.ppobProductType?.code

            PpobDao(this).ppobInquiry(requestModel, BaseDao.getInstance(this, KEY_PPOB_INQUIRY).callback)
        }
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        if (br == null) {
            dialogServiceNotAvailableError()
            return
        }

        ProgressDialogComponent.dismissProgressDialog(this)
        super.onApiResponseCallback(br, responseCode, response)
        if (response != null) {
            if (response.isSuccessful) {
                when (responseCode) {
                    API_KEY_PPOB_DENOM_LIST -> if ((br as BaseResponseModel).meta.code == 200) {
                        displayDenomList((br as PpobOttoagDenomResponseModel).data.denomination)
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.setOnDismissListener {
                            finish()
                        }
                        dialog.show()
                    }
                    API_KEY_PPOB_USER_GUIDE -> if ((br as BaseResponseModel).meta.code == 200) {
                        displayUserGuide((br as PpobUserGuideResponse))
                    }
                    KEY_PPOB_INQUIRY -> if ((br as BaseResponseModel).meta.code == 200) {
                        inquirySuccess(br as PpobInquiryResponse)
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.setOnDismissListener {
                            finish()
                        }
                        dialog.show()
                    }
                }
            }
        }
    }

    override fun onApiResponseError() {
        onApiResponseError()
    }

    //endregion API Call
}
