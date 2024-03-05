package com.otto.mart.ui.activity.ppob.product.listrik

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.FavoriteItemModel
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.support.util.formValidation.FormValidation
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.invisible
import com.otto.mart.support.util.onChange
import com.otto.mart.support.util.visible
import com.otto.mart.ui.activity.PpobActivity
import com.otto.mart.ui.activity.ppob.PpobDenomActivity
import com.otto.mart.ui.activity.ppob.PpobFavoriteListActivity
import com.otto.mart.ui.activity.ppob.setup.PpobProductTypeSetup
import kotlinx.android.synthetic.main.content_ppob_product_input.*
import kotlinx.android.synthetic.main.ppob_tab_menu.*
import kotlinx.android.synthetic.main.toolbar.btnToolbarBack
import kotlinx.android.synthetic.main.toolbar.tvToolbarTitle
import kotlinx.android.synthetic.main.toolbar_ppob.btnToolbarRight
import kotlinx.android.synthetic.main.toolbar_ppob.imgToolbarRight

class PpobListrikProductInputActivity : PpobActivity() {

    val KEY_GET_FAVORITE = 100

    private var mPpobProductType: PpobProductType = PpobProductType()
    private var mFormValidation: FormValidation? = null

    private var mIsValidationEnable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppob_listrik_product_input)

        mPpobProductType = PpobProductTypeSetup().getProductListrikToken()

        initView()
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
            }
        }
    }

    fun initView() {
        tvToolbarTitle.text = mPpobProductType.name
        imgToolbarRight.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_all_menu_ppob))
        btnToolbarRight.visible()

        tvTab1.text = getString(R.string.ppob_tab_listrik_pln)
        tvTab2.text = getString(R.string.ppob_tab_tagihan_pln)

        mFormValidation = FormValidation(this)

        etCustNumber.onChange {
            if (mFormValidation!!.isValidHandphone(it)) {
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
            startActivityForResult(intent, KEY_GET_FAVORITE)
        }

        btnTab1.setOnClickListener {
           tabSelected(1)
        }

        btnTab2.setOnClickListener {
           tabSelected(2)
        }

        btnNext.setOnClickListener {
            mIsValidationEnable = true
            if (validateInputForm()) {
                var ppobPayment = PpobPayment(
                        etCustNumber.text.toString(),
                        etEmail.text.toString(),
                        0, null, null, null,
                        cbFavorite.isChecked,
                        mPpobProductType
                )

                val intent = Intent(this, PpobDenomActivity::class.java)
                intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, ppobPayment)
                startActivity(intent)
            }
        }
    }

    private fun tabSelected(tabPosition: Int) {
        tabIndicator1.invisible()
        tabIndicator2.invisible()

        if (tabPosition == 1) {
            tabIndicator1.visible()
            mPpobProductType = PpobProductTypeSetup().getProductListrikToken()
        } else {
            tabIndicator2.visible()
            mPpobProductType = PpobProductTypeSetup().getProductListrikTagihan()
        }
    }

    private fun validateInputForm(): Boolean {
        if (mIsValidationEnable) {
            var status = true

            tvCustNumberError.gone()
            tvEmailError.gone()

            if (!mFormValidation!!.isRequired(etCustNumber.text.toString())) {
                tvCustNumberError.text = getString(R.string.ppob_msg_no_meter_is_required)
                tvCustNumberError.visible()
                status = false
            } else if (!mFormValidation!!.isValidHandphone(etCustNumber.text.toString())) {
                tvCustNumberError.text = getString(R.string.ppob_msg_no_meter_invalid)
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