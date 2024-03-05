package com.otto.mart.ui.activity.dashboard

import android.app.*
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.CheckVersionResponseModel
import com.otto.mart.model.APIModel.Response.inbox.InboxResponse
import com.otto.mart.model.localmodel.Realm.LoginDatastoreModel
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.service.NotificationReceiverService
import com.otto.mart.support.util.ApiCallback
import com.otto.mart.support.util.DateUtil
import com.otto.mart.ui.activity.BaseSessioncheckActivity
import com.otto.mart.ui.activity.SecurityInformation.SecurityInformationActivity
import com.otto.mart.ui.activity.deposit.ottocash.WalletOttocashActivity
import com.otto.mart.ui.activity.faq.FAQWebViewActivity
import com.otto.mart.ui.activity.login.LoginActivity
import com.otto.mart.ui.activity.profile.ProfileActivity
import com.otto.mart.ui.activity.syaratDanKetentuan.SnKActivity
import com.otto.mart.ui.activity.transaction.balance.OmzetActivity
import com.otto.mart.ui.activity.transaction.history.HistoryActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.component.dialog.Popup
import com.otto.mart.ui.fragment.dashboard.*
import kotlinx.android.synthetic.main.activity_dashboard_main.*
import kotlinx.android.synthetic.main.nav_dashboard.*
import retrofit2.Response

class DashboardActivity : BaseSessioncheckActivity(), IDashboard, View.OnClickListener {

    private val KEY_INBOX = "inbox"
    private val KEY_EARNING_POINT = "earrning_point"

    private val lastTranslate = 0.0f
    private val thiswindow: Window? = null
    private var isHome = false
    private var isChecked = false
    private val isIntentTransaction = false
    var count = 0
    private var homeFragment: Fragment? = null
    private var inboxFragment: Fragment? = null
    private var walletFragment: Fragment? = null
    private var profileFragment: Fragment? = null
    private var model: LoginDatastoreModel? = null
    var onPauseDuration: Long = 600000 // 600000 = 10 Menit
    private val lastSeen = System.currentTimeMillis()
    private var mMoreMenuFragment: MoreMenuFragment? = null
    private var mPaymentOptionFragment: PaymentOptionFragment? = null
    private var mSelectedTabPosition = 0
    private var mTarget = ""
    private var isFirstTimeLaunch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_main)
        if (intent.hasExtra(KEY_NOTIF_INBOX)) {
            if (intent.getBooleanExtra(KEY_NOTIF_INBOX, false)) {
                mTarget = KEY_INBOX
            }
        }
        if (intent.hasExtra(KEY_NOTIF_EARNING_POINT)) {
            if (intent.getBooleanExtra(KEY_NOTIF_EARNING_POINT, false)) {
                mTarget = KEY_EARNING_POINT
            }
        }
        initLocationPermission()
        initComponent()
        initContent()
        initFragment()
        checkVersion()
        setNetcoreProfile()
    }

    override fun onResume() {
        super.onResume()
        inbox
        if (isFirstTimeLaunch) {
            addNetcoreEvent("home")
            isFirstTimeLaunch = false
        }

//        Dialog dialog = new Loading().getDialog(this, "Permintaan Anda sedang diproses");
//        dialog.show();
    }

    private fun addNetcoreEvent(name: String) {
        val payload = HashMap<String, Any>()
        payload["name"] = name
        payload["created_at"] = DateUtil.getSimpleTime("dd-MM-yyyy HH:mm:ss")
        //Smartech.getInstance(WeakReference(this)).trackEvent(name, payload)
    }

    private fun setNetcoreProfile() {
        var name = "-"
        var email = "-"
        if (SessionManager.getPrefLogin().merchant_name != null && !SessionManager.getPrefLogin().merchant_name.isEmpty()) {
            name = SessionManager.getPrefLogin().merchant_name
        }
        if (SessionManager.getPrefLogin().email != null && !SessionManager.getPrefLogin().email.isEmpty()) {
            email = SessionManager.getPrefLogin().email
        }
        if (SessionManager.getPrefLogin().phone != null && !SessionManager.getPrefLogin().phone.isEmpty()) {
            val payload = HashMap<String, Any>()
            payload["NAME"] = name
            payload["EMAIL"] = email
            payload["MOBILE"] = SessionManager.getPrefLogin().phone
            //Smartech.getInstance(WeakReference(this)).login(SessionManager.getPrefLogin().phone)
            //Smartech.getInstance(WeakReference(this)).updateUserProfile(payload)
        }
    }

    override fun startActivity(intent: Intent) {
        count = 0
        //isIntentTransaction = true;
        if (intent.hasExtra("extraString") && intent.getStringExtra("extraString") == "back") {
        } else {
            super.startActivity(intent)
        }
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        count = 0
        //isIntentTransaction = true;
        super.startActivityForResult(intent, requestCode)
    }

    fun resetCounter() {
        count = 0
        //isIntentTransaction = true;
    }

    override fun onBackPressed() {
        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> finishAffinity()
                DialogInterface.BUTTON_NEGATIVE -> {
                }
            }
        }
        val builder = AlertDialog.Builder(this)
        if (isHome) {
            builder.setMessage(getString(R.string.text_close_app_confirmation))
                .setPositiveButton(getString(R.string.text_yes), dialogClickListener)
                .setNegativeButton(getString(R.string.text_no), dialogClickListener).show()
        } else {
            val hund = supportFragmentManager
            tabSelected(1)
        }
    }

    private fun initComponent() {
        tabMenuHome.setOnClickListener(this)
        tabMenuInbox.setOnClickListener(this)
        tabMenuQr.setOnClickListener(this)
        tabMenuKasir.setOnClickListener(this)
        tabMenuProfile.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == tabMenuHome!!.id) {
            tabSelected(1)
        } else if (id == tabMenuInbox!!.id) {
            tabSelected(2)
        } else if (id == tabMenuQr!!.id) {
            tabSelected(3)
        } else if (id == tabMenuKasir!!.id) {
            //tabSelected(4)
            gotoOttoKasir()
        } else if (id == tabMenuProfile!!.id) {
            tabSelected(5)
        }
    }

    private fun initFragment() {
        val bedong = supportFragmentManager
        homeFragment = DashboardHomeFragment()
        inboxFragment = DashboardInboxFragment()
        walletFragment = DashboardWalletFragment()
        profileFragment = DashboardProfileFragment()
        when (mTarget) {
            "" -> tabSelected(1)
            KEY_INBOX -> tabSelected(2)
            KEY_EARNING_POINT -> tabSelected(2)
        }
    }

    private fun tabSelected(tabPosition: Int) {
        var themeColor = AppKeys.DEFAULT_THEME_COLOR

        if (SessionManager.getMerchantTheme() != null && SessionManager.getMerchantTheme().themeColor != null) {
            if (SessionManager.getMerchantTheme().themeColor.contains("#")) {
                themeColor = SessionManager.getMerchantTheme().themeColor
            }
        }

        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setColor(Color.parseColor(themeColor))
        shape.setStroke(0, Color.BLACK)
        ivTabMenuQrIconContainer.setBackground(shape)

        if (mSelectedTabPosition != tabPosition) {
            if (tabPosition != 3 && tabPosition != 4) {
                tvTabMenuHome.setTextColor(ContextCompat.getColor(this, R.color.brown_grey_4))
                tvTabMenuInbox.setTextColor(ContextCompat.getColor(this, R.color.brown_grey_4))
                tvTabMenuProfile.setTextColor(ContextCompat.getColor(this, R.color.brown_grey_4))

                tvTabMenuHome.setTypeface(tvTabMenuHome.getTypeface(), Typeface.NORMAL)
                tvTabMenuInbox.setTypeface(tvTabMenuInbox.getTypeface(), Typeface.NORMAL)
                tvTabMenuProfile.setTypeface(tvTabMenuProfile.getTypeface(), Typeface.NORMAL)

                ivTabMenuHome.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_tab_menu_home
                    )
                )
                ivTabMenuInbox.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_tab_menu_inbox
                    )
                )
                ivTabMenuProfile.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_tab_menu_profile
                    )
                )
                isHome = false
            }

            when (tabPosition) {
                1 -> {
                    isHome = true
                    ivTabMenuHome.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_tab_menu_home_selected_svg
                        )
                    )
                    DrawableCompat.setTint(
                        ivTabMenuHome.getDrawable(),
                        Color.parseColor(themeColor)
                    )
                    tvTabMenuHome.setTextColor(Color.parseColor(themeColor))
                    tvTabMenuHome.setTypeface(tvTabMenuHome.getTypeface(), Typeface.BOLD)
                    showFragment(homeFragment, fragmentContainer.id)
                }
                2 -> {
                    ivTabMenuInbox.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_tab_menu_inbox_selected_svg
                        )
                    )
                    DrawableCompat.setTint(
                        ivTabMenuInbox.getDrawable(),
                        Color.parseColor(themeColor)
                    )
                    tvTabMenuInbox.setTextColor(Color.parseColor(themeColor))
                    tvTabMenuInbox.setTypeface(tvTabMenuHome.getTypeface(), Typeface.BOLD)
                    showFragment(inboxFragment, fragmentContainer.id)
                }
                3 ->
                    //startActivity(Intent(this, ShowQrActivity::class.java))
                    // showPaymentOptionFragment()
                    comingSoonDialogBase()

                4 ->
                    //showFragment(walletFragment, fragmentContainer.getId());
                    startActivity(Intent(this, WalletOttocashActivity::class.java))
                5 -> {
                    ivTabMenuProfile.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_tab_menu_profile_selected_svg
                        )
                    )
                    DrawableCompat.setTint(
                        ivTabMenuProfile.getDrawable(),
                        Color.parseColor(themeColor)
                    )
                    tvTabMenuProfile.setTextColor(Color.parseColor(themeColor))
                    tvTabMenuProfile.setTypeface(tvTabMenuHome.getTypeface(), Typeface.BOLD)
                    showFragment(profileFragment, fragmentContainer!!.id)
                }
                else -> {
                    showFragment(homeFragment, fragmentContainer!!.id)
                }
            }
        }
        if (tabPosition != 3 && tabPosition != 4) {
            mSelectedTabPosition = tabPosition
        }
    }

    private fun initContent() {
        /*
         * Check intent from notification
         */
        if (intent.getBooleanExtra(NotificationReceiverService.KEY_INTENT_HISTORY, false)) {
            val intent = Intent(this, OmzetActivity::class.java)
            intent.putExtra(HistoryActivity.KEY_TITLE, HistoryActivity.TAB_LABEL_OMZET)
            startActivity(intent)
        }
        val toselfJenk = Intent(this@DashboardActivity, DashboardActivity::class.java)
        val tologinJenk = Intent(this@DashboardActivity, LoginActivity::class.java)
        tologinJenk.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val toFaqJenk = Intent(this@DashboardActivity, FAQWebViewActivity::class.java)
        val toSnKJenk = Intent(this@DashboardActivity, SnKActivity::class.java)
        //        Intent toConJenk = new Intent(DashboardActivity.this, ContactUsActivity.class);
        val toProJenk = Intent(this@DashboardActivity, ProfileActivity::class.java)
        val toSecJenk = Intent(this@DashboardActivity, SecurityInformationActivity::class.java)
        var contactUs = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "08111678894", null))
        if (isAppInstalled("com.whatsapp")) {
            contactUs = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/628111678894?text=Saya%20memerlukan%20bantuan\n")
            )
        }
        if (!SessionManager.isSoftKey()) {
            navbar!!.visibility = View.GONE
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

    private fun gotoOttoKasir() {
        val ottoKasirAppId = "ottokasir.ottokonek.com"
        if (isAppInstalled(ottoKasirAppId)) {
            val intent = getPackageManager().getLaunchIntentForPackage(ottoKasirAppId)
            intent?.let { startActivity(it) }
        } else {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$ottoKasirAppId")
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$ottoKasirAppId")
                    )
                )
            }
        }
    }

    override fun openDrawer() {}
    override fun isMainPage(status: Boolean) {
        isHome = status
    }

    override fun onPause() {
        super.onPause()

        /*if (!isIntentTransaction) {
            count++;
            isIntentTransaction = false;
            isChecked = false;
            lastSeen = System.currentTimeMillis();
            SessionManager.setLastSeen(lastSeen);
        }*/
    }

    override fun onPostResume() {
        super.onPostResume()

        /*isIntentTransaction = false;
        if (count > 0 && !isChecked) {
            count = 0;
            isChecked = false;

            if ((System.currentTimeMillis() - SessionManager.getLastSeen()) > onPauseDuration) {
                Intent jenk = new Intent(this, RegisterPinResetActivity.class);
                jenk.putExtra("session", true);
                startActivityForResult(jenk, 555);
            }
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 555 && resultCode == Activity.RESULT_OK) {
            isChecked = true
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun showDownloadMemberCardNotif() {
        val intent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
        var pendingIntent: PendingIntent? = null
        val channelId = "ottopay_download"

        pendingIntent = PendingIntent.getActivity(
            this,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_ottopay_logo)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.msg_download_member_card_success))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        manager.notify(0, builder.build())
    }


    /**
     * showing bottom sheet dialog fragment / more menu fragment
     */
    fun showMoreMenuFragment() {
        mMoreMenuFragment = MoreMenuFragment()
        mMoreMenuFragment?.let {
            it.show(supportFragmentManager, it.tag)
        }
    }

    fun hideMoreMenuFragment() {
        mMoreMenuFragment?.let {
            if (it.isVisible) {
                it.dismiss()
            }
        }
    }

    fun showPaymentOptionFragment() {
        mPaymentOptionFragment = PaymentOptionFragment()
        mPaymentOptionFragment?.let {
            it.show(supportFragmentManager, it.tag)
        }
    }

    fun hidePaymentOptionFragment() {
        mPaymentOptionFragment?.let {
            if (it.isVisible) {
                it.dismiss()
            }
        }
    }

    private val inbox: Unit
        private get() {
            setmOnGetInboxListener { unread: Int ->
                if (unread > 0) {
                    inboxReadIndicator!!.visibility = View.VISIBLE
                } else {
                    inboxReadIndicator!!.visibility = View.GONE
                }
            }

            OttoKonekAPI.notificationList(this, 0, object : ApiCallback<InboxResponse>() {
                override fun onResponseSuccess(body: InboxResponse?) {
                    if (isSuccess200 && !isDestroyed && !isFinishing) {
                        body?.data?.unread?.let { mOnGetInboxListener.onGetInboxSuccess(it) }
                    }
                }

                override fun onApiServiceFailed(throwable: Throwable?) {

                }

            })
        }

    //region API
    override fun onApiResponseCallback(br: BaseResponse, responseCode: Int, response: Response<*>) {
        ProgressDialogComponent.dismissProgressDialog(this)
        validateApiResponse(br)
        if (response.body() != null && response.isSuccessful) {
            when (responseCode) {
                AppKeys.API_KEY_CHECK_VERSION -> if ((br as BaseResponseModel).meta.code == 200) {
                    var model: CheckVersionResponseModel? = null
                    try {
                        model = br as CheckVersionResponseModel
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (model != null) {
                        updateDialog(model.data)
                    }
                } else {
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.show()
                }
            }
        } else {
            onApiResponseError()
        }
    }

    override fun onApiFailureCallback(message: String, ac: BaseActivity) {
        //onApiResponseError();
    } //endregion

    companion object {
        const val KEY_NOTIF_INBOX = "NOTIF_INBOX"
        const val KEY_NOTIF_EARNING_POINT = "NOTIF_EARNING_POINT"
        val isEmulator: Boolean
            get() = (Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || Build.MANUFACTURER.contains("Nox")
                    || Build.MANUFACTURER.contains("Bluestack")
                    || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                    || Build.BRAND == null || Build.BOARD == null || Build.getRadioVersion() == null || "google_sdk" == Build.PRODUCT)
    }

}
