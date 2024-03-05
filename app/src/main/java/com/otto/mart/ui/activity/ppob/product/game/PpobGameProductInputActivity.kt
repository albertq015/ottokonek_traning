package com.otto.mart.ui.activity.ppob.product.game

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.FavoriteItemModel
import com.otto.mart.model.APIModel.Misc.OttoagDenomModel
import com.otto.mart.model.localmodel.ppob.PpobPayment
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.formValidation.FormValidation
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.onChange
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.ppob.game.PpobServerIdSpinnerAdapter
import com.otto.mart.ui.activity.PpobActivity
import com.otto.mart.ui.activity.ppob.PpobDenomActivity
import com.otto.mart.ui.activity.ppob.PpobFavoriteListActivity
import com.otto.mart.ui.activity.ppob.setup.PpobProductTypeSetup
import kotlinx.android.synthetic.main.content_ppob_game_product_input.*
import kotlinx.android.synthetic.main.toolbar.*

class PpobGameProductInputActivity : PpobActivity() {

    val KEY_GET_FAVORITE = 100

    private var mPpobProductType: PpobProductType = PpobProductType()
    private var mFormValidation: FormValidation? = null
    private var mIsValidationEnable = false

    private var mVoucherGameData: OttoagDenomModel? = null
    private var mSelectedServerId: OttoagDenomModel.OptionField.Serverid? = null

    private var isZoneIdRequired = false
    private var isRoleNameRequired = false

    private var mProductName = ""
    private var mProductCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppob_game_product_input)

        if (intent.hasExtra(AppKeys.KEY_PPOB_GAME_DATA)) {
            mVoucherGameData = Gson().fromJson(intent.getStringExtra(AppKeys.KEY_PPOB_GAME_DATA), OttoagDenomModel::class.java)
        }

        mPpobProductType = PpobProductTypeSetup().getProductGame()

        if (mVoucherGameData?.product_name != null) {
            mProductName = mVoucherGameData?.product_name!!
        }

        if (mVoucherGameData?.product_code != null) {
            mProductCode = mVoucherGameData?.product_code!!
        }

        initView()
        displayGameInfo()
        displayServerOption()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == KEY_GET_FAVORITE) {
                var favorite = Gson().fromJson(data?.extras!!.getString(AppKeys.KEY_PPOB_FAVORITE_DATA), FavoriteItemModel::class.java)
                etUserId.setText(favorite.customer_reference)
                etServerId.setText(favorite.server_id.replace("-", ""))
                etRoleName.setText(favorite.role_name)
                etEmail.setText(favorite.email)
                etUserId.requestFocus()
                etUserId.setSelection(etUserId.text.length)
                validateInputForm()
            }
        }
    }

    private fun initView() {
        mFormValidation = FormValidation(this)

        etUserId.onChange {
            if (mFormValidation!!.isRequired(it)) {
                btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
            }
            validateInputForm()
        }

        when {
            mVoucherGameData?.getField_input()?.size == 0 -> {
                etUserId.setText(SessionManager.getPhone())
                etUserId.gone()
                etRoleName.gone()
                etServerId.gone()
                serverIdOptionLayout.gone()
                cbFavorite.gone()
                btnFavorite.gone()
                etEmail.gone()
                btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
            }
            mVoucherGameData?.getField_input()?.size == 1 -> {
                isZoneIdRequired = false
                etUserId.visible()
                etServerId.gone()
                etRoleName.gone()
                serverIdOptionLayout.gone()
            }
            mVoucherGameData?.getField_input()?.size == 2 -> {
                isZoneIdRequired = true
                etUserId.visible()
                etServerId.visible()
                etRoleName.gone()
            }
            mVoucherGameData?.getField_input()?.size == 3 -> {
                isZoneIdRequired = true
                isRoleNameRequired = true
                etUserId.visible()
                etServerId.visible()
                etRoleName.visible()
            }
            else -> {
                etUserId.gone()
                etRoleName.gone()
                etServerId.gone()
                etEmail.gone()
                serverIdOptionLayout.gone()
                cbFavorite.gone()
                btnFavorite.gone()
                btnNext.background = ContextCompat.getDrawable(this, R.drawable.button_primary_selector)
            }
        }

        etUserId.onChange {
            validateInputForm()
        }

        etEmail.onChange {
            validateInputForm()
        }

        etServerId.onChange {
            validateInputForm()
        }

        etRoleName.onChange {
            validateInputForm()
        }

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        btnFavorite.setOnClickListener {
            val intent = Intent(this, PpobFavoriteListActivity::class.java)
            intent.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_CODE, mPpobProductType.favoriteCode)
            intent.putExtra(PpobFavoriteListActivity.KEY_PRODUCT_CODE, mVoucherGameData?.product_code)
            startActivityForResult(intent, KEY_GET_FAVORITE)
        }

        btnNext.setOnClickListener {
            mIsValidationEnable = true
            if (validateInputForm()) {
                var userId = ""
                var period: Long? = null

                mVoucherGameData?.let {
                    it.field_input?.let {
                        if (it.size == 3) {
                            userId =  etRoleName.text.toString() + "|" + etUserId.text.toString()
                        } else if (it.size > 0) {
                            userId = etUserId.text.toString()
                        } else {
                            userId = SessionManager.getPhone()
                        }
                    }
                }

                if (!etServerId.text.toString().equals("")) {
                    period = etServerId.text.toString().toLong()
                }

                if (mSelectedServerId != null) {
                    mSelectedServerId?.let {
                        period = it.backend_value.toLong()
                    }
                }

                var ppobPayment = PpobPayment(
                        userId,
                        etEmail.text.toString(),
                        0, period,
                        etServerId.text.toString(),
                        etRoleName.text.toString(),
                        cbFavorite.isChecked,
                        mPpobProductType,
                        null,
                        mProductName,
                        mProductCode
                )

                val intent = Intent(this, PpobDenomActivity::class.java)
                intent.putExtra(AppKeys.KEY_PPOB_PAYMENT_DATA, ppobPayment)
                intent.putExtra(AppKeys.KEY_PPOB_GAME_DATA, Gson().toJson(mVoucherGameData))
                startActivity(intent)

                mVoucherGameData?.let {
                    if (it.field_input.size == 0) {
                        finish()
                    }
                }
            }
        }

        //Game POD dirrect to Deenom List
        mVoucherGameData?.let {
            if (it.field_input.size == 0) {
                btnNext.performClick()
            }
        }
    }

    private fun displayGameInfo() {
        tvToolbarTitle.text = mVoucherGameData?.product_name
        tvProductName.text = mVoucherGameData?.product_name

        if (!mVoucherGameData?.remarks.equals("")) {
            tvGameDesc.text = mVoucherGameData?.remarks
            gameDescLayout.visible()
        }

        try {
            Glide.with(this)
                    .load(mVoucherGameData?.logo)
                    .apply(RequestOptions().placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).skipMemoryCache(true))
                    .into(imgProductLogo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun displayServerOption() {
        serverIdOptionLayout.gone()

        if (mVoucherGameData?.option_field != null) {
            if ((mVoucherGameData?.option_field != null) && (mVoucherGameData?.option_field?.size!! > 0)) {

                mVoucherGameData?.let {
                    val serverIdAdapter = PpobServerIdSpinnerAdapter(this@PpobGameProductInputActivity, it.option_field.get(0).data)
                    spinServerId.setAdapter(serverIdAdapter)
                    etServerId.gone()
                    serverIdOptionLayout.visible()
                }

                spinServerId.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(arg0: AdapterView<*>, view: View, position: Int, arg3: Long) {
                        mVoucherGameData?.let {
                            mSelectedServerId = it.option_field.get(0).data.get(position)
                        }
                    }

                    override fun onNothingSelected(arg0: AdapterView<*>) {
                        // TODO Auto-generated method stub
                    }
                })
            }
        }
    }

    private fun validateInputForm(): Boolean {
        if (mIsValidationEnable) {
            var status = true

            tvUserIdError.gone()
            tvServerIdError.gone()
            tvRoleNameError.gone()
            tvEmailError.gone()

            if (!mFormValidation!!.isRequired(etUserId.text.toString()) && (etUserId.visibility == View.VISIBLE)) {
                tvUserIdError.text = getString(R.string.ppob_id_pelanggan_is_required)
                tvUserIdError.visible()
                status = false
            } else if (!mFormValidation!!.isRequired(etServerId.text.toString()) && (etServerId.visibility == View.VISIBLE)) {
                tvServerIdError.text = getString(R.string.ppob_server_id_is_required)
                tvServerIdError.visible()
                status = false
            } else if (!mFormValidation!!.isRequired(etRoleName.text.toString()) && (etRoleName.visibility == View.VISIBLE)) {
                tvRoleNameError.text = getString(R.string.ppob_role_name_is_required)
                tvRoleNameError.visible()
                status = false
            } else if (!etEmail.text.toString().equals("")) {
                if (!mFormValidation!!.isValidEmail(etEmail.text.toString())) {
                    tvEmailError.text = getString(R.string.ppob_msg_eemail_is_not_valid)
                    tvEmailError.visibility = View.VISIBLE
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