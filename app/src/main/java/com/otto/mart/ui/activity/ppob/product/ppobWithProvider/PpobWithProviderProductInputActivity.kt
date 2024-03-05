package com.otto.mart.ui.activity.ppob.product.ppobWithProvider

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.InputType
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.FavoriteItemModel
import com.otto.mart.model.APIModel.Misc.OttoagDenomModel
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.support.util.formValidation.FormValidation
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.onChange
import com.otto.mart.support.util.visible
import com.otto.mart.ui.activity.PpobActivity
import com.otto.mart.ui.activity.ppob.PpobDenomActivity
import com.otto.mart.ui.activity.ppob.PpobFavoriteListActivity
import com.otto.mart.ui.activity.ppob.PpobProviderActivity
import kotlinx.android.synthetic.main.content_ppob_product_input.btnFavorite
import kotlinx.android.synthetic.main.content_ppob_product_input.btnNext
import kotlinx.android.synthetic.main.content_ppob_product_input.cbFavorite
import kotlinx.android.synthetic.main.content_ppob_product_input.etCustNumber
import kotlinx.android.synthetic.main.content_ppob_product_input.etEmail
import kotlinx.android.synthetic.main.content_ppob_product_input.formLayout
import kotlinx.android.synthetic.main.content_ppob_product_input.tvCustNumberError
import kotlinx.android.synthetic.main.content_ppob_product_input.tvEmailError
import kotlinx.android.synthetic.main.content_ppob_with_provider_product_input.*
import kotlinx.android.synthetic.main.ppob_button_provider.*
import kotlinx.android.synthetic.main.toolbar.btnToolbarBack
import kotlinx.android.synthetic.main.toolbar.tvToolbarTitle
import kotlinx.android.synthetic.main.toolbar_ppob.btnToolbarRight
import kotlinx.android.synthetic.main.toolbar_ppob.imgToolbarRight


class PpobWithProviderProductInputActivity : PpobActivity() {

    val KEY_GET_FAVORITE = 100
    val KEY_GET_PROVIDER = 101

    private var mPpobProductType: PpobProductType = PpobProductType()
    private var mFormValidation: FormValidation? = null
    private var mIsValidationEnable = false

    var mSelectedProvider : OttoagDenomModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppob_with_provider_product_input)

        //collect our intent
        mPpobProductType = intent.getParcelableExtra<Parcelable>(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA) as PpobProductType

        if (intent.hasExtra(AppKeys.KEY_PPOB_PROVIDER_DATA)) {
            mSelectedProvider = Gson().fromJson(intent.getStringExtra(AppKeys.KEY_PPOB_PROVIDER_DATA), OttoagDenomModel::class.java)
        }

        initView()
        initBanner()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == KEY_GET_FAVORITE) {
                var favorite = Gson().fromJson(data?.extras!!.getString(AppKeys.KEY_PPOB_FAVORITE_DATA), FavoriteItemModel::class.java)
                etCustNumber.setText(favorite.customer_reference)
                etEmail.setText(favorite.email)
                etCustNumber.requestFocus()
                etCustNumber.setSelection(etCustNumber.text.length)
            } else if(requestCode == KEY_GET_PROVIDER){
                var denomModel = Gson().fromJson(data?.extras!!.getString(AppKeys.KEY_PPOB_PROVIDER_DATA), OttoagDenomModel::class.java)
                mSelectedProvider = denomModel
                tvProvider.text = denomModel.product_name
                bankSelectLayout.gone()
                bankSelectedLayout.visible()
                formLayout.visible()
                btnFavorite.visible()
            }
        }
    }

    private fun initView() {
        tvToolbarTitle.text = mPpobProductType.name
        btnFavorite.gone()
        imgToolbarRight.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_all_menu_ppob))
        btnToolbarRight.visible()

        if (mSelectedProvider != null) {
            tvProvider.text = mSelectedProvider?.product_name
            bankSelectLayout.gone()
            bankSelectedLayout.visible()
            formLayout.visible()
            btnFavorite.visible()
        }

        mFormValidation = FormValidation(this)

        if(mPpobProductType.code.equals(AppKeys.PPOB_TYPE_AIR)){
            etCustNumber.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
        }

        etCustNumber.onChange {
            if (mFormValidation!!.isValidNoPdam(it)) {
                btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
            }
            validateInputForm()
        }

        etEmail.onChange {
            validateInputForm()
        }

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        btnToolbarRight.setOnClickListener {
            showAllPpobMenu(mPpobProductType.name)
        }

        btnFavorite.setOnClickListener {
            val intent = Intent(this, PpobFavoriteListActivity::class.java)
            intent.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_CODE, mPpobProductType.favoriteCode)
            intent.putExtra(PpobFavoriteListActivity.KEY_PRODUCT_CODE, mSelectedProvider?.product_code)
            startActivityForResult(intent, KEY_GET_FAVORITE)
        }

        btnProvider.setOnClickListener {
            val intent = Intent(this, PpobProviderActivity::class.java)
            intent.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, mPpobProductType)
            intent.putExtra(AppKeys.KEY_PPOB_IS_FROM_INPUT_FORM, true)
            intent.putExtra(AppKeys.KEY_PPOB_CATEGORY_PICKER_SEARCH_STATUS, true)
            startActivityForResult(intent, KEY_GET_PROVIDER)
        }

        btnNext.setOnClickListener {
            mIsValidationEnable = true
            if (validateInputForm()) {
                var ppobPayment = PpobPayment(
                        etCustNumber.text.toString(),
                        etEmail.text.toString(),
                        0, null, null, null,
                        cbFavorite.isChecked,
                        mPpobProductType,
                null,
                        mSelectedProvider?.product_name,
                        mSelectedProvider?.product_code
                )

                val intent = Intent(this, PpobDenomActivity::class.java)
                intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, ppobPayment)
                intent.putExtra(AppKeys.KEY_PPOB_PROVIDER_DATA, Gson().toJson(mSelectedProvider))
                startActivity(intent)
            }
        }
    }

    private fun initBanner() {
        var imgSrc = 0

        when (mPpobProductType.code) {
            AppKeys.PPOB_TYPE_AIR -> imgSrc = R.drawable.ppob_banner_air
        }

        if (imgSrc != 0) {
            imgBanner.setImageDrawable(ContextCompat.getDrawable(this, imgSrc))
            imgBanner.visible()
        }
    }

    private fun validateInputForm(): Boolean {
        if (mIsValidationEnable) {
            var status = true

            tvCustNumberError.gone()
            tvEmailError.gone()

            if (!mFormValidation!!.isRequired(etCustNumber.text.toString())) {
                tvCustNumberError.text = getString(R.string.ppob_id_pelanggan_is_required)
                tvCustNumberError.visible()
                status = false
            } else if (!etEmail.text.toString().equals("")) {
                if (!mFormValidation!!.isValidEmail(etEmail.text.toString())) {
                    tvEmailError.text = getString(R.string.ppob_msg_eemail_is_not_valid)
                    tvEmailError.visible()
                    status = false
                }
            }

            if (status) {
                btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
            } else {
                btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_white_grey_selected_bg)
            }

            return status
        } else {
            return false
        }
    }
}
