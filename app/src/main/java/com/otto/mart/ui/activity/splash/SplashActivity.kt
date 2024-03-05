package com.otto.mart.ui.activity.splash

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.otto.mart.BuildConfig
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Response.AppClonerListResponse
import com.otto.mart.model.APIModel.Response.AppClonerListResponse.Data.Packages
import com.otto.mart.model.APIModel.Response.CheckVersionResponseModel
import com.otto.mart.presenter.dao.AuthDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.AppClonerUtil
import com.otto.mart.support.util.pref.Pref
import com.otto.mart.support.util.widget.dialog.AlertDialogBottomSheet
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.login.LoginActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.activity_splash.*
import retrofit2.Response


class SplashActivity : AppActivity() {

    private val APP_PACKAGE_DOT_COUNT = 3
    private val DUAL_APP_ID_999 = "999"
    private val DOT = '.'

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initView()
        firebaseToken()
        checkVersion()
    }

    private fun firebaseToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token = task.result
                SessionManager.setFirebaseToken(task.result)

                Log.d("Firebase Token", token)
                SessionManager.setFirebaseToken(token)
            })
    }

    override fun onResume() {
        super.onResume()
    }


//    private fun displayGetLocationDialog() {
//        val dialog = AlertDialogBottomSheet(
//            this,
//            supportFragmentManager
//        )
//        dialog.setIsShowIcon(true)
//        dialog.setIcon(R.drawable.ic_get_location_info)
//        dialog.setTitle(getString(R.string.dialog_ask_location_title))
//        dialog.setMessage(getString(R.string.dialog_ask_location_msg))
//        dialog.setButton(getString(R.string.dialog_ask_location_btn_ok))
//        dialog.setIsCancelable(false)
//        dialog.setActionButton(::allowGetLocation)
//        dialog.setActionOnDismiss(::onDismissDialogGetLocation)
//        dialog.show()
//    }

    private fun displayGetContactDialog() {
        val dialog = AlertDialogBottomSheet(
            this,
            supportFragmentManager
        )
        dialog.setIsShowIcon(true)
        dialog.setIcon(R.drawable.ic_dialog_contact)
        dialog.setTitle(getString(R.string.dialog_ask_contact_title))
        dialog.setMessage(getString(R.string.dialog_ask_contact_msg))
        dialog.setButton(getString(R.string.dialog_ask_location_btn_ok))
        dialog.setIsCancelable(false)
        dialog.setActionButton(::allowGetContact)
        dialog.setActionOnDismiss(::onDismissDialogGetContact)
        dialog.show()
    }


    private fun allowGetContact() {
        Pref.getPreference()
            .putBoolean(SessionManager.KEY_PREF_HIDE_CONTACT_PERMISSION_DIALOG, true)
        gotoLogin()
    }

    private fun onDismissDialogGetLocation() {
        initAllPermission()
    }

    private fun onDismissDialogGetContact() {
        gotoLogin()
    }

    private fun initView() {
        if (SessionManager.getMerchantTheme() != null) {

            var themeColor = AppKeys.DEFAULT_THEME_COLOR

            if (SessionManager.getMerchantTheme() != null && SessionManager.getMerchantTheme().themeColor != null) {
                if (SessionManager.getMerchantTheme().themeColor.contains("#")) {
                    themeColor = SessionManager.getMerchantTheme().themeColor
                }
            }

//            rootLayout.setBackgroundColor(Color.parseColor(themeColor))

            Glide.with(this)
                .load(SessionManager.getMerchantTheme().dashboardLogo)
                .error(R.drawable.ic_ottopay_logo)
                .into(imgLogo)
        }

        try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            tvVersionCode.text = getString(R.string.app_name) + " ver " + pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    fun isAppInstalled(packageName: String?): Boolean {
        return try {
            packageManager.getApplicationInfo(packageName!!, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun checkAppCloner(appCloneerList: List<Packages>) {
        var appName = ""
        for (appCloner in appCloneerList) {
            if (isAppInstalled(appCloner.package_name)) {
                if (appName == "") {
                    appName = appCloner.app_name
                } else {
                    appName += ", " + appCloner.app_name
                }
            }
        }

        if (appName != "") {
            val dialog = ErrorDialog(
                this@SplashActivity, this@SplashActivity, false,
                false, "Mohon hapus App Cloner/Parallel Space anda ($appName)", ""
            )
            dialog.setOnDismissListener { dialog1: DialogInterface? -> finish() }
            dialog.show()
        } else {
            //Handle Crash when update app (PREF_LOGIN is null)
            if (SessionManager.getPrefLogin() == null || SessionManager.getMerchantTheme() == null ||
                SessionManager.getFeatureProduct() == null
            ) {
                SessionManager.logout()
            }
            checkVersion()
        }
    }

    private fun checkAppCloning() {
        if (AppClonerUtil().isRunOnAppCloner(this)) {
            appClonerDetected()
        }
    }

    private fun gotoLogin() {
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2400)
    }

    private fun appClonerDetected() {
        val dialog = ErrorDialog(
            this@SplashActivity, this@SplashActivity,
            false, false, getString(R.string.msg_app_cloner_detected), ""
        )
        dialog.setOnDismissListener { dialog1: DialogInterface ->
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }


    // region API

    private fun appCloner() {
        val handler = Handler()
        handler.postDelayed({
            AuthDao(this@SplashActivity).appClonerList(
                BaseDao.getInstance(
                    this@SplashActivity, AppKeys.API_KEY_APP_CLONER_LIST
                ).callback
            )
        }, 2400)
    }

    override fun onApiResponseCallback(
        br: BaseResponse,
        responseCode: Int,
        response: Response<*>
    ) {
        if (response.body() != null && response.isSuccessful) {
            if (responseCode == AppKeys.API_KEY_APP_CLONER_LIST) {
                if (response.isSuccessful) {
                    if ((br as AppClonerListResponse).meta.code == 200) {
                        checkAppCloner(br.data.packages)
                    } else {
                        val dialog = ErrorDialog(
                            this@SplashActivity,
                            this@SplashActivity,
                            false,
                            false,
                            getString(R.string.error_msg_response_error),
                            ""
                        )
                        dialog.show()
                    }
                }
            } else if (responseCode == AppKeys.API_KEY_CHECK_VERSION) {
                handleCheckVerion(br)
            }
        } else {
            val dialog = ErrorDialog(
                this,
                this,
                false,
                false,
                getString(R.string.error_msg_response_error_title),
                getString(R.string.error_msg_response_error)
            )
            dialog.setOnDismissListener { dialog1: DialogInterface? -> finish() }
            dialog.show()
        }
    }

    private fun handleCheckVerion(br: BaseResponse) {
        val response = br as CheckVersionResponseModel
        if (response.meta.code == 200) {
            updateDialog(response.data)
            if (response.data.version_api <= BuildConfig.VERSION_CODE) gotoLogin()
        } else {
            val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
            dialog.show()
        }
    }

    override fun onApiFailureCallback(message: String, ac: BaseActivity) {
        onApiResponseError()
    }

    // endregion API
}