package com.otto.mart.ui.activity.ppob.product.donasi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Request.donasi.DonasiInquiryRequest
import com.otto.mart.model.APIModel.Request.donasi.DonasiPaymentRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.donasi.DonasiInquiryResponse
import com.otto.mart.model.APIModel.Response.donasi.ProductDonasiResponse
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.presenter.dao.DonasiDao
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.formValidation.FormValidation
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.onChange
import com.otto.mart.support.util.visible
import com.otto.mart.ui.activity.PpobActivity
import com.otto.mart.ui.activity.ppob.PpobConfirmationActivity
import com.otto.mart.ui.activity.ppob.setup.PpobProductTypeSetup
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.content_donasi_jago_sodaqoh_form.*
import kotlinx.android.synthetic.main.content_donasi_lezis_nu_form.*
import kotlinx.android.synthetic.main.content_ppob_donasi_product_input.*
import kotlinx.android.synthetic.main.ppob_button_provider.*
import kotlinx.android.synthetic.main.toolbar.btnToolbarBack
import kotlinx.android.synthetic.main.toolbar.tvToolbarTitle
import kotlinx.android.synthetic.main.toolbar_ppob.*
import retrofit2.Response

class PpobDonasiProductInputActivity : PpobActivity() {

    val KEY_GET_PROVIDER = 100

    private var mProductDonasi: ProductDonasiResponse.Data.Denomination? = null
    private lateinit var mProductList: MutableList<ProductDonasiResponse.Data.Denomination>
    private lateinit var mSelectedProduct: ProductDonasiResponse.Data.Denomination.Detail

    private val KEY_PRODUCT_NAME = "DONASI"
    private val KEY_ZAKAT = "Zakat"
    private val KEY_INFAQ = "Infaq"
    private val KEY_NPWP_EMPTY = "######"

    private var mName = ""
    private var mEmail = ""
    private var mPhone = ""
    private var mNPWP = ""
    private var mCustReference = ""
    private var mProductName = ""

    private var mIsValidationEnable = false
    var mTextWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppob_donasi_product_input)

        if (intent.hasExtra(AppKeys.KEY_PPOB_DONASI_PRODUCT_DATA)) {
            mProductDonasi = Gson().fromJson(intent.getStringExtra(AppKeys.KEY_PPOB_DONASI_PRODUCT_DATA), ProductDonasiResponse.Data.Denomination::class.java)
            mSelectedProduct = mProductDonasi?.detail?.get(0)!!
            displayProductInfo()
        }

        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == KEY_GET_PROVIDER) {
                mProductDonasi = Gson().fromJson(data?.extras!!.getString(AppKeys.KEY_PPOB_DONASI_PRODUCT_DATA), ProductDonasiResponse.Data.Denomination::class.java)
                displayProductInfo()
            }
        }
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_ppob_donasi_product_input)
        imgToolbarRight.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_all_menu_ppob))
        btnToolbarRight.visible()

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        btnToolbarRight.setOnClickListener {
            showAllPpobMenu(getString(R.string.donasi_menu))
        }

        //Laziz NU
        mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var amount = DataUtil.getInt(etAmountNu.text.toString().replace(getString(R.string.text_currency), ""))

                etAmountNu.removeTextChangedListener(mTextWatcher)
                etAmountNu.setText(DataUtil.convertCurrency(amount))
                etAmountNu.setSelection(etAmountNu.text.length)

                if (amount == 0) {
                    etAmountNu.setText("")
                }

                if (mProductDonasi?.detail?.size!! > 1) isFormNuValid else isFormValid

                etAmountNu.addTextChangedListener(mTextWatcher)
            }

            override fun afterTextChanged(editable: Editable) {

            }
        }

        etAmountNu.addTextChangedListener(mTextWatcher)
        etNameNu.onChange { isFormNuValid }
        etPhoneNu.onChange { isFormNuValid }
        etEmailNu.onChange { isFormNuValid }
        etNpwpNu.onChange { isFormNuValid }

        //Jaga Sodaqoh
        etName.onChange { isFormValid }
        etPhone.onChange { isFormValid }
        etEmail.onChange { isFormValid }

        btnProvider.setOnClickListener {
            val intent = Intent(this, PpobDonasiProductListActivity::class.java)
            intent.putExtra(AppKeys.KEY_PPOB_IS_FROM_INPUT_FORM, true)
            startActivityForResult(intent, KEY_GET_PROVIDER)
        }

        productLayout.setOnClickListener {
            val intent = Intent(this, PpobDonasiProductListActivity::class.java)
            intent.putExtra(AppKeys.KEY_PPOB_IS_FROM_INPUT_FORM, true)
            startActivityForResult(intent, KEY_GET_PROVIDER)
        }

        btnNext.setOnClickListener {
            mIsValidationEnable = true

            if (mProductDonasi?.detail?.size!! > 1) {
                if (isFormNuValid) {

                    mName = etNameNu.text.toString()
                    var amount = DataUtil.getInt(etAmountNu.text.toString().replace(getString(R.string.text_currency), ""))
                    mEmail = etEmailNu.text.toString()
                    mPhone = etPhoneNu.text.toString()
                    mNPWP = etNpwpNu.text.toString()

                    mCustReference = mName + " | " + mEmail + " | " + mPhone + " | " + mNPWP + " | " + amount
                    callBillerInquiryApi(mCustReference)
                }
            } else {
                if (isFormValid) {
                    mName = etName.text.toString()
                    var amount = 1000
                    mEmail = etEmail.text.toString()
                    mPhone = etPhone.text.toString()
                    mNPWP = KEY_NPWP_EMPTY

                    mCustReference = mName + " | " + mEmail + " | " + mPhone + " | " + "" + " | " + amount
                    callBillerInquiryApi(mCustReference)
                }
            }
        }

        rgDonationType.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
                when (checkedId) {
                    rbLeft.id -> {
                        mSelectedProduct = mProductDonasi?.detail?.get(0)!!
                        mProductName = mProductDonasi?.detail?.get(0)!!.name
                    }
                    rbRight.id -> {
                        mSelectedProduct = mProductDonasi?.detail?.get(1)!!
                        mProductName = mProductDonasi?.detail?.get(1)?.name!!
                    }
                }
            }
        })
    }

    //todo productname on xml placeholder or tools
    private fun displayProductInfo() {
        tvProductName.text = mProductDonasi?.operator

        try {
            Glide.with(this)
                    .load(mProductDonasi?.images)
                    .apply(RequestOptions().placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).skipMemoryCache(true))
                    .into(imgProductLogo)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mProductDonasi?.let {
            if (it.detail != null) {
                if (rgDonationType.checkedRadioButtonId == rbLeft.id) {
                    mSelectedProduct = it.detail.get(0)
                } else {
                    mSelectedProduct = it.detail.get(1)
                }

                if (mProductDonasi?.detail?.size!! > 1) {
                    rbLeft.text = it.detail[0].name
                    rbRight.text = it.detail[1].name
                    jagaSodaqohForm.gone()
                    lezizNUForm.visible()
                } else {
                    jagaSodaqohForm.visible()
                    lezizNUForm.gone()
                }
            }
        }

        selectProviderLayout.gone()
        contentLayout.visible()
        btnNext.visible()
    }

    /**
     * method to validate JagaSodaqoh form
     * */
    private val isFormValid: Boolean
        get() {
            if (!mIsValidationEnable) {
                return false
            }

            var status = true

            var fullname = etName.text.toString()
            var email = etEmail.text.toString()
            var phone = etPhone.text.toString()

            tvNameError.gone()
            tvPhoneError.gone()
            tvEmailError.gone()

            if (!FormValidation(this).isRequired(fullname)) {
                tvNameError.text = getString(R.string.donasi_msg_nama_donatur_required)
                tvNameError.visible()
                status = false
            } else if (!FormValidation(this).isRequired(phone)) {
                tvPhoneError.text = getString(R.string.donasi_msg_phone_required)
                tvPhoneError.visible()
                status = false
            } else if (!FormValidation(this).isValidHandphone(phone)) {
                tvPhoneError.text = getString(R.string.donasi_msg_phone_invalid)
                tvPhoneError.visible()
                status = false
            } else if (!FormValidation(this).isValidMobilePhone(phone)) {
                tvPhoneError.text = getString(R.string.ppob_msg_phone_is_not_valid)
                tvPhoneError.visible()
                status = false
            } else if (!FormValidation(this).isRequired(email)) {
                tvEmailError.text = getString(R.string.donasi_msg_email_required)
                tvEmailError.visible()
                status = false
            } else if (!FormValidation(this).isValidEmail(email)) {
                tvEmailError.text = getString(R.string.donasi_msg_email_invalid)
                tvEmailError.visible()
                status = false
            }

            if (status) {
                btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
            } else {
                btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)
            }

            return status
        }

    /**
     * method to validate Leziz NU form
     * */
    private val isFormNuValid: Boolean
        get() {
            if (!mIsValidationEnable) {
                return false
            }

            var status = true

            var fullname = etNameNu.text.toString()
            var amount = etAmountNu.text.toString()
            var email = etEmailNu.text.toString()
            var phone = etPhoneNu.text.toString()


            tvAmountNuError.gone()
            tvNameNuError.gone()
            tvPhoneNuError.gone()
            tvEmailNuError.gone()
            tvNpwpError.gone()

            if (!FormValidation(this).isRequired(amount)) {
                tvAmountNuError.text = getString(R.string.donasi_msg_amount_required)
                tvAmountNuError.visible()
                status = false
            } else if (!FormValidation(this).isRequired(fullname)) {
                tvNameNuError.text = getString(R.string.donasi_msg_nama_donatur_required)
                tvNameNuError.visible()
                status = false
            } else if (FormValidation(this).isRequired(email) && !FormValidation(this).isValidEmail(email)) {
                tvEmailNuError.text = getString(R.string.donasi_msg_email_invalid)
                tvEmailNuError.visible()
                status = false
            } else if (!FormValidation(this).isRequired(phone)) {
                tvPhoneNuError.text = getString(R.string.donasi_msg_phone_required)
                tvPhoneNuError.visible()
                status = false
            } else if (!FormValidation(this).isValidMobilePhone(phone)) {
                tvPhoneNuError.text = getString(R.string.ppob_msg_phone_is_not_valid)
                tvPhoneNuError.visible()
                status = false
            } else if (!FormValidation(this).isValidHandphone(phone)) {
                tvPhoneNuError.text = getString(R.string.donasi_msg_phone_invalid)
                tvPhoneNuError.visible()
                status = false
            }

            if (status) {
                btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
            } else {
                btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)
            }

            return status
        }

    private fun inquirySuccess(donasiInquiryResponse: DonasiInquiryResponse) {
        var ppobPayment = PpobPayment("", "",
                0, null, null, null,false,
                PpobProductTypeSetup().getProductDonasi(), null,
                mProductDonasi?.operator + " (" + mSelectedProduct.name + ")"
        )

        val donasiPaymentRequest = DonasiPaymentRequest()
        donasiPaymentRequest.product_code = donasiInquiryResponse.data.inquiry_data.productcode
        donasiPaymentRequest.customer_reference = mCustReference
        donasiPaymentRequest.admin_fee = donasiInquiryResponse.data.inquiry_data.adminfee
        donasiPaymentRequest.sub_amount = 0
        donasiPaymentRequest.amount = donasiInquiryResponse.data.inquiry_data.amount
        donasiPaymentRequest.inquiry_data = donasiInquiryResponse.data.inquiry_data.rrn

        val intent = Intent(this, PpobConfirmationActivity::class.java)
        intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, ppobPayment)
        intent.putExtra(AppKeys.KEY_PPOB_DONASI_INQUIRY_DATA, Gson().toJson(donasiInquiryResponse))
        intent.putExtra(AppKeys.KEY_PPOB_DONASI_PAYMENT_REQUEST_DATA, Gson().toJson(donasiPaymentRequest))
        startActivity(intent)
    }


    //region API Call

    private fun callBillerInquiryApi(custReference: String) {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

        var donasiInquiryRequest = DonasiInquiryRequest()
        donasiInquiryRequest.product_code = mSelectedProduct.productcode
        donasiInquiryRequest.customer_reference = custReference

        DonasiDao(this@PpobDonasiProductInputActivity).billerInquiry(donasiInquiryRequest,
                BaseDao.getInstance(this@PpobDonasiProductInputActivity, AppKeys.API_KEY_DONASI_INQUIRY).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        ProgressDialogComponent.dismissProgressDialog(this)
        super.onApiResponseCallback(br, responseCode, response)
        if (responseCode == AppKeys.API_KEY_DONASI_INQUIRY) {
            if ((br as BaseResponseModel).meta.code == 200) {
                inquirySuccess((br as DonasiInquiryResponse))
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

    //endregion API Call
}