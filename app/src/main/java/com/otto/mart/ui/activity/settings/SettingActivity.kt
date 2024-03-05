package com.otto.mart.ui.activity.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Request.setting.LanguageSettingRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.merchant.featureProduct.FeatureProductResponse
import com.otto.mart.model.APIModel.Response.profile.ProfileData
import com.otto.mart.model.APIModel.Response.setting.LanguageSettingResponse
import com.otto.mart.model.localmodel.WebViewContent
import com.otto.mart.model.localmodel.profile.ProfileMenu
import com.otto.mart.presenter.dao.AuthDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.ui.Partials.adapter.setting.SettingMenuAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.login.LoginActivity
import com.otto.mart.ui.activity.profile.ProfileDetailActivity
import com.otto.mart.ui.activity.webView.WebViewActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.fragment.setting.LanguageSetttingFragment
import kotlinx.android.synthetic.main.content_setting.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class SettingActivity : AppActivity() {

    companion object {
        val KEY_DATA_PROFILE = "profile"
    }

    val KEY_API_LANGUAGE_SETTING = 100
    val KEY_API_PRODUCT_FEATURE = 200

    private var mProfile: ProfileData? = null

    private var mLanguageSetttingFragment: LanguageSetttingFragment? = null

    private var mLanguageCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Setting Activity", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        if (intent.hasExtra(KEY_DATA_PROFILE)) {
            mProfile = Gson().fromJson(intent.extras?.getString(KEY_DATA_PROFILE), ProfileData::class.java)
        }

        initView()
        initRecyclerview()
        displayMenu()
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_setting)

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecyclerview() {
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
    }

    private fun displayMenu() {
        val menuList = mutableListOf<ProfileMenu>()

        //Edit Profile
        val editAccount = ProfileMenu()
        val editAccountIntent = Intent(this, ProfileDetailActivity::class.java)
        editAccountIntent.putExtra(KEY_DATA_PROFILE, intent.getStringExtra(KEY_DATA_PROFILE))

        editAccount.name = getString(R.string.setting_menu_edit_profile)
        editAccount.intent = editAccountIntent
        menuList.add(editAccount)

        //Language Setting
//        val languageSetting = ProfileMenu()
//        languageSetting.name = getString(R.string.setting_menu_language)
//        languageSetting.intent = Intent()
//        menuList.add(languageSetting)

        //About OttoPay
        /*val aboutOttopay = ProfileMenu()
        aboutOttopay.name = getString(R.string.setting_menu_about_ottopay)
        aboutOttopay.intent = Intent(this, FaqActivity::class.java)
        menuList.add(aboutOttopay)*/

        val faq = ProfileMenu()
        val faqWebContent = WebViewContent()
        val faqIntent = Intent(this, WebViewActivity::class.java)
        faqWebContent.title = getString(R.string.profile_menu_faq)
        faqWebContent.url = "https://otto-konek.s3-ap-southeast-1.amazonaws.com/tnc/otto_konek_faq.html"
        faqIntent.putExtra(AppKeys.KEY_WEBVIEW_CONTENT, faqWebContent)

        faq.name = getString(R.string.profile_menu_faq)
        faq.intent = faqIntent
        menuList.add(faq)

        //Term and Condition
        val tncWebContent = WebViewContent()
        val tncIntent = Intent(this, WebViewActivity::class.java)
        tncWebContent.title = getString(R.string.setting_menu_tnc)
        tncWebContent.url = "https://otto-konek.s3-ap-southeast-1.amazonaws.com/tnc/otto_konek_tnc.html"
        tncIntent.putExtra(AppKeys.KEY_WEBVIEW_CONTENT, tncWebContent)

        val tnc = ProfileMenu()
        tnc.name = getString(R.string.profile_menu_term_and_condition)
        tnc.intent = tncIntent
        menuList.add(tnc)

        //Privacy Policy
        val policyIntent = Intent(this, WebViewActivity::class.java)
        val privacyWebContent = WebViewContent()
        privacyWebContent.title = getString(R.string.setting_menu_privacy_policy)
        privacyWebContent.url = "https://otto-konek.s3-ap-southeast-1.amazonaws.com/tnc/otto_konek_privacy_policy.html"
        policyIntent.putExtra(AppKeys.KEY_WEBVIEW_CONTENT, privacyWebContent)

        val privacyPolicy = ProfileMenu()
        privacyPolicy.name = getString(R.string.setting_menu_privacy_policy)
        privacyPolicy.intent = policyIntent
        menuList.add(privacyPolicy)

        //Security Information
        val securityInfoIntent = Intent(this, WebViewActivity::class.java)
        val securityInfoWebContent = WebViewContent()
        securityInfoWebContent.title = getString(R.string.setting_menu_security_information)
        securityInfoWebContent.url = "https://otto-konek.s3-ap-southeast-1.amazonaws.com/tnc/otto_konek_security_information.html"
        securityInfoIntent.putExtra(AppKeys.KEY_WEBVIEW_CONTENT, securityInfoWebContent)

        val securityInfo = ProfileMenu()
        securityInfo.name = getString(R.string.informasi_keamanan)
        securityInfo.intent = securityInfoIntent
        menuList.add(securityInfo)

        val adapter = SettingMenuAdapter(this as Activity, menuList)
        recyclerview.adapter = adapter

        adapter.setmOnViewClickListener(object : SettingMenuAdapter.OnViewClickListener {
            override fun onViewClickListener(data: ProfileMenu, position: Int) {
                menuSelected(data, position)
            }
        })
    }

    private fun menuSelected(data: ProfileMenu, position: Int) {
        if (data.intent != null) {
            if (data.name.equals(getString(R.string.setting_menu_language), true)) {
                showLanguageSettingFragment()
            } else {
                startActivity(data.intent)
            }
        }
    }

    /**
     * showing bottom sheet dialog fragment / Language Setting fragment
     */
    fun showLanguageSettingFragment() {
        mLanguageSetttingFragment = LanguageSetttingFragment()
        mLanguageSetttingFragment?.show(supportFragmentManager, mLanguageSetttingFragment?.tag)
    }

    fun hideLanguageSettingFragment() {
        if (mLanguageSetttingFragment != null) {
            mLanguageSetttingFragment?.let {
                it.dismiss()
            }
        }
    }


    //region API

    fun setLanguageSetting(languageCode: String) {
        val languageSetttingRequest = LanguageSettingRequest()
        languageSetttingRequest.language_id = LanguageIdConverter().getLanguageId(languageCode)
        mLanguageCode = languageCode

        ProgressDialogComponent.showProgressDialog(this, getString(R.string.text_loading), false).show()
        AuthDao(this).languageSetting(languageSetttingRequest, BaseDao.getInstance(this, KEY_API_LANGUAGE_SETTING).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse, responseCode: Int, response: Response<*>) {
        ProgressDialogComponent.dismissProgressDialog(this)
        validateApiResponse(br)
        if (response.body() != null && response.isSuccessful) {
            when (responseCode) {
                KEY_API_LANGUAGE_SETTING -> if ((br as LanguageSettingResponse).meta.code == 200) {
                    if (mLanguageCode.isNotEmpty()) {
                        SessionManager.setLanguageCode(mLanguageCode)
                        callProductFeatureAPI()
                    }
                } else {
                    val dialog = ErrorDialog(this, this, false, false, (br as BaseResponseModel).meta.message, "")
                    dialog.show()
                }
                KEY_API_PRODUCT_FEATURE -> {
                    if ((br as BaseResponseModel).meta.code == 200) {
                        SessionManager.setFeatureProduct((br as FeatureProductResponse).data)

                        val intent = Intent(this, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.show()
                    }
                }
            }
        }
    }

    override fun onApiFailureCallback(message: String, ac: BaseActivity) {
        onApiResponseError()
    }


    private fun callProductFeatureAPI() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()
        AuthDao(this).featureProduct(BaseDao.getInstance(this, KEY_API_PRODUCT_FEATURE).callback)
    }

    //endregion API Call
}