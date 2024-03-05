package com.otto.mart.ui.activity.ppob

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.OttoagDenomModel
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel
import com.otto.mart.model.APIModel.Request.FavoriteAddRequestModel
import com.otto.mart.model.APIModel.Request.PpobOttoagDenomRequestModel
import com.otto.mart.model.APIModel.Request.PpobOttoagInquiryRequestModel
import com.otto.mart.model.APIModel.Request.ppob.PpobInquiryRequest
import com.otto.mart.model.APIModel.Request.voucherGame.VoucherGameDenomRequestModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.FavoriteResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagDenomResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagInquiryResponseModel
import com.otto.mart.model.APIModel.Response.ppob.PpobInquiryResponse
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.model.localmodel.ppob.PpobPaymentMethod
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.presenter.dao.PpobDao
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.notNull
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.ppob.PpobDenomAdapter
import com.otto.mart.ui.Partials.adapter.ppob.PpobPaymentDetailAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.ppob.setup.PpobPaymentMethodSetup
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.fragment.ppob.PpobPaymentMethodFragment
import com.otto.mart.ui.fragment.ppob.PpobSetKomisiFragment
import kotlinx.android.synthetic.main.content_ppob_denom.*
import kotlinx.android.synthetic.main.part_manual_input_top_up_saldo.*
import kotlinx.android.synthetic.main.ppob_button_payment_method.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class PpobDenomActivity : AppActivity() {

    val TAG = this.javaClass.simpleName

    val KEY_PPOB_DENOM_LIST: Int = 100
    val KEY_PPOB_INQUIRY: Int = 101
    val KEY_PPOB_INQUIRY_OLD: Int = 200

    var mPpobPayment: PpobPayment = PpobPayment()
    var mSelectedDenom: OttoagDenomModel? = null
    var mSelectedManualInputDenom: OttoagDenomModel? = null
    var mDenomList: MutableList<OttoagDenomModel> = ArrayList()

    val mPaymentMethodFragment = PpobPaymentMethodFragment()
    val mSetKomisiFragment = PpobSetKomisiFragment()
    private var mVoucherGameData: OttoagDenomModel? = null

    var mPaymentMethodList = mutableListOf<PpobPaymentMethod>()
    var mSelectedPaymentMethod: PpobPaymentMethod? = null
    var mKomisi = 0L

    var mPpobInquiryResponse: PpobInquiryResponse? = null
    var mPpobOttoagInquiryResponseModel: PpobOttoagInquiryResponseModel? = null

    val KEY_PPOB_SAVE_FAVORITE = 400

    var mTextWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppob_denom)

        initData()
        initView()
        initRecyclerView()
        displayCustNumberLabel()
        displayPaymentInfo()
        paymentMethodSelected(PpobPaymentMethodSetup().getPaymentMethod()[0], 0)

        if (mPpobPayment.ppobProductType?.type == PpobProductType.PASCABAYAR) {
            if (mSelectedDenom != null) {
                if (mPpobPayment.ppobProductType?.isOldApi == true) {
                    callInquiryOld()
                } else {
                    callInquiry()
                }
            } else {
                if (mPpobPayment.ppobProductType?.isOldApi == true) {
                    getDenomListOld()
                } else {
                    getProductList(mPpobPayment.ppobProductType?.code.toString())
                }
            }
        } else {
            if (mPpobPayment.ppobProductType?.code == AppKeys.PPOB_TYPE_TOP_UP) {
                getTopUpDenomList()
            } else if (mPpobPayment.ppobProductType?.code == AppKeys.PPOB_TYPE_GAME) {
                getGameDenomList()
            } else {
                getDenomList()
            }
        }
    }

    private fun initData() {
        //collect our intent
        if (intent.hasExtra(AppKeys.KEY_PPOB_PAYMENT_DATA)) {
            mPpobPayment = intent.getParcelableExtra<Parcelable>(AppKeys.KEY_PPOB_PAYMENT_DATA) as PpobPayment
        }

        if (intent.hasExtra(AppKeys.KEY_PPOB_PROVIDER_DATA)) {
            mSelectedDenom = Gson().fromJson(intent.getStringExtra(AppKeys.KEY_PPOB_PROVIDER_DATA), OttoagDenomModel::class.java)
            btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
        }

        if (intent.hasExtra(AppKeys.KEY_PPOB_GAME_DATA)) {
            mVoucherGameData = Gson().fromJson(intent.getStringExtra(AppKeys.KEY_PPOB_GAME_DATA), OttoagDenomModel::class.java)
        }

        mPaymentMethodList = PpobPaymentMethodSetup().getPaymentMethod()
    }

    private fun displayCustNumberLabel() {
        var label = "Nomor Pelanggan"

        tvCustNumberLabel.text = label
    }

    private fun displayPaymentInfo() {
        tvCustNumber.text = mPpobPayment.custNumber
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_ppob_denom)

        if (mPpobPayment.ppobProductType?.type.equals(PpobProductType.PRABAYAR)) {
            tvLabelPrice.text = getString(R.string.ppob_label_product_price_prepaid)
        } else if (mPpobPayment.ppobProductType?.type.equals(PpobProductType.PASCABAYAR)) {
            tvToolbarTitle.text = getString(R.string.text_invoice_detail)
            tvLabelPrice.text = getString(R.string.ppob_label_product_price_postpaid)
        } else {
            tvLabelPrice.text = getString(R.string.ppob_label_product_price_prepaid)
        }

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var amount = DataUtil.getInt(etAmount.text.toString().replace(getString(R.string.text_currency), ""))
                etAmount.removeTextChangedListener(mTextWatcher)

                if (amount != 0) {
                    etAmount.setText(DataUtil.convertCurrency(amount))
                    etAmount.setSelection(etAmount.text.length)

                    tvAmountError.gone()
                    btnNext.isEnabled = true
                    btnNext.background = ContextCompat.getDrawable(this@PpobDenomActivity, R.drawable.button_primary_selector)

                    //Reset Denom List
                    var i = 0
                    for (ottoagDenomModel in mDenomList) {
                        mDenomList.get(i).isSelected = false
                        i++
                    }
                    displayDenomList(mDenomList)
                } else {
//                    etAmount.setText("")
//                    tvAmountError.text = getString(R.string.ppob_top_up_manual_input_msg_amount_is_required)
//                    tvAmountError.visible()
//                    btnNext.isEnabled = false
//                    btnNext.background = ContextCompat.getDrawable(this@PpobDenomActivity, R.drawable.button_white_grey_selected_bg)
                }

                var totalPrice = mSelectedManualInputDenom?.price?.plus(amount.toLong())
                tvModal.text = DataUtil.convertCurrency(amount)
                tvTotal.text = DataUtil.convertCurrency(totalPrice)
                paymentDetailLayout.visible()

                etAmount.addTextChangedListener(mTextWatcher)
            }

            override fun afterTextChanged(editable: Editable) {}
        }

        etAmount.addTextChangedListener(mTextWatcher)

        if (mPpobPayment.ppobProductType?.type == PpobProductType.PASCABAYAR) {
            tvSelectNominal.text = getString(R.string.ppob_please_select_payment_method)
        }

        nestedScrollView.isNestedScrollingEnabled = true

        btnPaymentMethod.setOnClickListener {
            //selectPaymentMethod()
        }

        btnNext.setOnClickListener {
            mSelectedDenom.notNull {
                mPpobPayment.productName = mSelectedDenom?.product_name

                if (mSelectedPaymentMethod != null) {
                    mPpobPayment.ppobPaymentMethod = mSelectedPaymentMethod

                    if (mPpobPayment.ppobProductType?.type == PpobProductType.PASCABAYAR) {
                        val intent = Intent(this, PpobConfirmationActivity::class.java)
                        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, mPpobPayment)

                        if (mPpobPayment.ppobProductType?.isOldApi == true) {
                            intent.putExtra(AppKeys.KEY_PPOB_INQUIRY_OLD_DATA, Gson().toJson(mPpobOttoagInquiryResponseModel))
                        } else {
                            intent.putExtra(AppKeys.KEY_PPOB_INQUIRY_DATA, Gson().toJson(mPpobInquiryResponse))
                        }

                        startActivity(intent)
                    } else {
                        inquiry()
                    }
                } else {
                    selectPaymentMethod()
                }
            }
        }
    }

    private fun inquiry() {
        if (mPpobPayment.ppobProductType?.type == PpobProductType.PASCABAYAR) {
            if (mPpobPayment.ppobProductType?.isOldApi == true) {
                callInquiryOld()
            } else {
                callInquiry()
            }
        } else {
            if (mPpobPayment.ppobProductType?.isOldApi == true) {
                callInquiryOld()
            } else {
                callInquiry()
            }
        }
    }

    private fun initRecyclerView() {
        rvDetail.setHasFixedSize(true)
        val llmDetail = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        rvDetail.setLayoutManager(llmDetail)

        recyclerview.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        recyclerview.setLayoutManager(linearLayoutManager)
    }

    private fun displayDenomList(denomination: MutableList<OttoagDenomModel>) {
        var denomList = mutableListOf<OttoagDenomModel>()

        if (mPpobPayment.ppobProductType?.code.equals(AppKeys.PPOB_TYPE_TOP_UP)) {
            for (ottoagDenomModel in denomination) {
                if (!ottoagDenomModel.product_name.contains("manual", true)) {
                    denomList.add(ottoagDenomModel)
                } else {
                    mSelectedManualInputDenom = ottoagDenomModel

                    tvNominalAdminFee.text = getString(R.string.ppob_top_up_manual_input_biaya_admin,
                            DataUtil.convertCurrency(ottoagDenomModel.price))
                    tvModal.text = DataUtil.convertCurrency(0)
                    manualInputLayout.visible()
                }
            }
        } else {
            denomList = denomination
        }

        if (denomList.size == 0) {
            btnNext.isEnabled = false
            btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)
        }
        if (mPpobPayment.ppobProductType?.type == PpobProductType.PRABAYAR) {
            mDenomList = denomList

            val adapter = PpobDenomAdapter(this, denomList)
            recyclerview.adapter = adapter

            adapter.setmOnViewClickListener(object : PpobDenomAdapter.OnViewClickListener {
                override fun onViewClickListener(denom: OttoagDenomModel, position: Int) {
                    if (!denom.isDisabled) {
                        denomSelected(denom, position)
                    }
                }
            })
        } else {
            if (denomList.size > 0) {
                mSelectedDenom = denomList.get(0)
                btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)

                //Call Inquiry
                if (mPpobPayment.ppobProductType?.isOldApi == true) {
                    callInquiryOld()
                } else {
                    callInquiry()
                }
            }
        }
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
        adapter?.notifyDataSetChanged()

        if (mSelectedDenom == null) {
            btnNext.isEnabled = false
            btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)
        } else {
            btnNext.isEnabled = true
            btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
        }

        if (mPpobPayment.ppobProductType?.code.equals(AppKeys.PPOB_TYPE_TOP_UP)) {
            etAmount.setText("")
            recyclerview.requestFocus()
        }

        displayPaymentDetail()

        //Show Set Komisi Dialog
        if (mPpobPayment.ppobProductType?.isSetKomisi == true) {
            setKomisi()
        }
    }

    private fun displayInquiryData(ppobOttoagInquiryResponseModel: PpobOttoagInquiryResponseModel) {
        var confirmationDataList = mutableListOf<WidgetBuilderModel>()

        confirmationDataList.clear()

        for (widgetBuilderModel in ppobOttoagInquiryResponseModel.key_value_list) {
            if (!widgetBuilderModel.key.equals("no. pelanggan", true)) {
                if (widgetBuilderModel.value != null && !widgetBuilderModel.value.isEmpty()) {
                    confirmationDataList.add(widgetBuilderModel)
                }
            }
        }

        val adapter = PpobPaymentDetailAdapter(this, confirmationDataList)
        adapter.orientation = PpobPaymentDetailAdapter.VERTICAL
        rvDetail.adapter = adapter
        rvDetail.visible()
    }

    private fun displayPaymentDetail() {
        val komisi = mPpobPayment.komisi!!

        tvModal.text = DataUtil.convertCurrency(mSelectedDenom?.price)
        tvKomisi.text = DataUtil.convertCurrency(mKomisi)
        tvTotal.text = DataUtil.convertCurrency(mSelectedDenom?.price?.plus(komisi))
        paymentDetailLayout.visible()

        if (mPpobPayment.ppobProductType?.code.equals(AppKeys.PPOB_TYPE_TOP_UP)) {
            mSelectedDenom?.let {
                if (it.product_name.contains("manual", true)) {
                    tvModal.text = DataUtil.convertCurrency(0)
                }
            }
        }
    }

    private fun inquirySuccess(ppobInquiryResponse: PpobInquiryResponse) {
        mPpobInquiryResponse = ppobInquiryResponse

        if (mPpobPayment.ppobProductType?.type == PpobProductType.PASCABAYAR) {

            val ppobOttoagInquiryResponseModel = PpobOttoagInquiryResponseModel()
            ppobOttoagInquiryResponseModel.data = PpobOttoagInquiryResponseModel.PpobMultifinanceInquiryData()
            ppobOttoagInquiryResponseModel.key_value_list = ppobInquiryResponse.data.key_value_list

            displayInquiryData(ppobOttoagInquiryResponseModel)
        } else {
            val intent = Intent(this, PpobConfirmationActivity::class.java)
            intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, mPpobPayment)
            intent.putExtra(AppKeys.KEY_PPOB_INQUIRY_DATA, Gson().toJson(ppobInquiryResponse))
            startActivity(intent)
        }
    }

    private fun inquiryOldSuccess(ppobOttoagInquiryResponseModel: PpobOttoagInquiryResponseModel) {
        mPpobOttoagInquiryResponseModel = ppobOttoagInquiryResponseModel

        if (mPpobPayment.ppobProductType?.type == PpobProductType.PASCABAYAR) {
            displayInquiryData(ppobOttoagInquiryResponseModel)
        } else {
            val intent = Intent(this, PpobConfirmationActivity::class.java)
            intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, mPpobPayment)
            intent.putExtra(AppKeys.KEY_PPOB_INQUIRY_OLD_DATA, Gson().toJson(ppobOttoagInquiryResponseModel))
            startActivity(intent)
        }
    }

    private fun selectPaymentMethod() {
        mPaymentMethodFragment.mPaymentMethodList = mPaymentMethodList
        mPaymentMethodFragment.show(supportFragmentManager, mPaymentMethodFragment.getTag())

        mPaymentMethodFragment.setItemSelectedClickListener(object : PpobPaymentMethodFragment.OnPaymentMethodSelected {
            override fun onPaymeentMethodSelected(item: PpobPaymentMethod, position: Int) {
                paymentMethodSelected(item, position)
            }
        })
    }

    private fun paymentMethodSelected(item: PpobPaymentMethod, position: Int) {
        mSelectedPaymentMethod = item
        mPaymentMethodList.get(position).selected = true
        tvPaymentMeyhod.text = "Tipe Pembayaran : " + item.name
        imgPaymentMethodIcon.setImageDrawable(item.icon?.let { ContextCompat.getDrawable(this@PpobDenomActivity, it) })
        selectLayout.gone()
        selectedLayout.visible()
    }

    private fun setKomisi() {
        mSelectedDenom?.price?.let { mSetKomisiFragment.setPrice(it.toLong()) }
        mSetKomisiFragment.show(supportFragmentManager, mSetKomisiFragment.getTag())

        mSetKomisiFragment.setKomisiSelectedClickListener(object : PpobSetKomisiFragment.OnKomisiSelectedListener {
            override fun onKomisiSelected(komisi: Long) {
                mKomisi = komisi.toLong()
                tvKomisi.text = DataUtil.convertCurrency(komisi)
                mPpobPayment.komisi = mKomisi
                displayPaymentDetail()
            }
        })
    }


    //region API Call

    private fun getDenomList() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

        PpobDao(this).billerProductListCashback(mPpobPayment.ppobProductType?.code, mPpobPayment.custNumber, "", "", BaseDao.getInstance(this, KEY_PPOB_DENOM_LIST).callback)
    }

    private fun getProductList(productName: String) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

        var prefix = ""
        if (mPpobPayment.ppobProductType?.code.equals(AppKeys.PPOB_TYPE_PULSA_PASCABAYAR)) {
            mPpobPayment.custNumber?.let { prefix = it }
        }

        PpobDao(this@PpobDenomActivity).billerProductList(productName, prefix, mSelectedDenom?.product_code,
                BaseDao.getInstance(this@PpobDenomActivity, KEY_PPOB_DENOM_LIST).callback)
    }

    private fun getDenomListOld() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

        val responseModel = PpobOttoagDenomRequestModel()
        responseModel.prefix = mPpobPayment.custNumber
        responseModel.keyword = ""

        PpobDao(this).ppobDenomListOld(mPpobPayment.ppobProductType?.code, responseModel, BaseDao.getInstance(this, KEY_PPOB_DENOM_LIST).callback)
    }

    private fun getTopUpDenomList() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

        val productName = mPpobPayment.ppobProductType?.code?.toUpperCase() + "DENOM"

        PpobDao(this).billerProductListCashback(productName, "", mSelectedDenom?.product_code, "", BaseDao.getInstance(this, KEY_PPOB_DENOM_LIST).callback)
    }

    private fun getGameDenomList() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

        val voucherGameDenomRequestModel = VoucherGameDenomRequestModel()
        voucherGameDenomRequestModel.game_code = mVoucherGameData?.product_code

        PpobDao(this).billerProductListCashback("GAMEDENOM", "", "", mVoucherGameData?.product_code, BaseDao.getInstance(this, KEY_PPOB_DENOM_LIST).callback)
    }

    private fun callInquiryOld() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

        val requestModel = PpobOttoagInquiryRequestModel()
        requestModel.customer_reference = mPpobPayment.custNumber
        requestModel.product_code = mSelectedDenom?.product_code
        requestModel.isCashback = true

        if (mPpobPayment.period != null) {
            requestModel.period = mPpobPayment.period.toString()
            requestModel.months = mPpobPayment.period.toString()
        }

        PpobDao(this).ppobInquiryOld(mPpobPayment.ppobProductType?.code, requestModel, BaseDao.getInstance(this, KEY_PPOB_INQUIRY_OLD).callback)
    }

    private fun callInquiry() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()
        val requestModel = PpobInquiryRequest()
        requestModel.cust_id = mPpobPayment.custNumber
        requestModel.product_code = mSelectedDenom?.product_code
        requestModel.type = mPpobPayment.ppobProductType?.code

        if (mPpobPayment.period != null) {
            requestModel.period = mPpobPayment.period.toString()
        }

        if (mPpobPayment.ppobProductType?.code.equals(AppKeys.PPOB_TYPE_TOP_UP)) {
            if (manualInputLayout.visibility == View.VISIBLE) {
                requestModel.period = DataUtil.getInt(etAmount.text.toString().
                replace(getString(R.string.text_currency), "")).toString()
            }

            if (!etAmount.text.toString().isEmpty()) {
                requestModel.product_code = mSelectedManualInputDenom?.product_code
            }
        }

        PpobDao(this).ppobInquiry(requestModel, BaseDao.getInstance(this, KEY_PPOB_INQUIRY).callback)
    }

    private fun saveFavorite() {
        val favoriteAddRequestModel = FavoriteAddRequestModel()

        if (mPpobPayment.ppobProductType?.isOldApi == true) {
            favoriteAddRequestModel.product_code = mPpobOttoagInquiryResponseModel!!.data.productcode
        } else {
            favoriteAddRequestModel.product_code = mPpobInquiryResponse!!.data.product_code
        }

        favoriteAddRequestModel.product_type = mPpobPayment.ppobProductType?.favoriteCode
        favoriteAddRequestModel.customer_reference = mPpobPayment.custNumber
        favoriteAddRequestModel.email = mPpobPayment.email

        if (mPpobPayment.ppobProductType?.code.equals(AppKeys.PPOB_TYPE_TOP_UP)) {
            favoriteAddRequestModel.wallet_code = mPpobPayment.productCode
        }

        if (mPpobPayment.ppobProductType?.code.equals(AppKeys.PPOB_TYPE_GAME)) {
            mPpobPayment.custNumber?.let {
                if (it.contains("|")) {
                    val custNumberSeparated = it.split("|").toTypedArray()
                    if (custNumberSeparated.size > 0) {
                        favoriteAddRequestModel.customer_reference = custNumberSeparated[1]
                    }
                }
            }

            favoriteAddRequestModel.game_name = mPpobPayment.productName
            favoriteAddRequestModel.game_code = mPpobPayment.productCode
            favoriteAddRequestModel.server_id = mPpobPayment.serverId
            favoriteAddRequestModel.role_name = mPpobPayment.roleName
        }

        val dao = PpobDao(this)
        dao.addFavorite(favoriteAddRequestModel, BaseDao.getInstance(this, KEY_PPOB_SAVE_FAVORITE).callback)
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
                    KEY_PPOB_DENOM_LIST -> if ((br as BaseResponseModel).meta.code == 200) {
                        displayDenomList((br as PpobOttoagDenomResponseModel).data.denomination)
                    } else {
                        btnNext.isEnabled = false
                        btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)

                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.setOnDismissListener {
                            finish()
                        }
                        dialog.show()
                    }
                    KEY_PPOB_INQUIRY -> if ((br as BaseResponseModel).meta.code == 200) {
                        inquirySuccess(br as PpobInquiryResponse)
                        if (mPpobPayment.isAddToFavorite == true) {
                            saveFavorite()
                        }
                    } else {
                        if (!this.isFinishing) {
                            val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                            dialog.setOnDismissListener {
                                if (!mPpobPayment.ppobProductType?.code.equals(AppKeys.PPOB_TYPE_TOP_UP)) {
                                    finish()
                                }
                            }
                            dialog.show()
                        }
                    }
                    KEY_PPOB_INQUIRY_OLD -> if ((br as BaseResponseModel).meta.code == 200) {
                        inquiryOldSuccess(br as PpobOttoagInquiryResponseModel)
                        if (mPpobPayment.isAddToFavorite == true) {
                            saveFavorite()
                        }
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.setOnDismissListener {
                            finish()
                        }
                        dialog.show()
                    }
                    KEY_PPOB_SAVE_FAVORITE ->
                        if ((br as BaseResponseModel).meta.code == 200) {
                            val favoriteResponseModel = (br as FavoriteResponseModel)
                            Log.d(TAG, favoriteResponseModel.meta.message)
                        } else {
                            Log.d(TAG, br.meta.message)
                        }
                }
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        onApiResponseError()
    }

    //endregion API Call
}