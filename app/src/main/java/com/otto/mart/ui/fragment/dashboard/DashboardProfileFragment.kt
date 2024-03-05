package com.otto.mart.ui.fragment.dashboard

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.*
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.ProfileResponseData
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.MerchantStatusResponse
import com.otto.mart.model.APIModel.Response.ProfileResponseModel
import com.otto.mart.model.APIModel.Response.balance.BalanceResponse
import com.otto.mart.model.APIModel.Response.balance.OttoKonekBalanceResponse
import com.otto.mart.model.APIModel.Response.profile.ProfileData
import com.otto.mart.model.APIModel.Response.profile.ProfileResponse
import com.otto.mart.model.localmodel.profile.ProfileMenu
import com.otto.mart.presenter.dao.OCBIDao
import com.otto.mart.presenter.dao.OttoPointDao
//import com.otto.mart.presenter.dao.ottopoin.OttoPoinDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.*
import com.otto.mart.support.util.widget.dialog.ConfirmationLogoutDialogFragment
import com.otto.mart.ui.Partials.adapter.profile.ProfileMenuAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.contact.ContactActivity
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import com.otto.mart.ui.activity.deposit.ottocash.WalletOttocashActivity
import com.otto.mart.ui.activity.login.LoginActivity
import com.otto.mart.ui.activity.multibank.ListBankTransferActivity
import com.otto.mart.ui.activity.multibank.ListLinkedBankAccountActivity
import com.otto.mart.ui.activity.ottopoint.OttomartActivity
import com.otto.mart.ui.activity.ottopoint.SyaratKetentuanActivity
import com.otto.mart.ui.activity.profile.BalanceActivity
import com.otto.mart.ui.activity.profile.ProfileDetailActivity
import com.otto.mart.ui.activity.register.forgot.RegisterPinResetActivity
import com.otto.mart.ui.activity.settings.SettingActivity
import com.otto.mart.ui.activity.transaction.balance.OmzetActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.component.dialog.InfoEligibleOttoPointDialog
import com.otto.mart.ui.fragment.AppFragment
import github.nisrulz.screenshott.ScreenShott
import kotlinx.android.synthetic.main.fragment_dashboard_profile.*
import kotlinx.android.synthetic.main.part_item_saldo.view.*
import kotlinx.android.synthetic.main.part_profile_header.*
import kotlinx.android.synthetic.main.part_saldo_profile.*
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class DashboardProfileFragment : AppFragment(), View.OnClickListener {

    val TAG = this.javaClass.simpleName

    private var pointStatus: String? = null
    private var mProfile: ProfileData? = null
    private val KEY_BALANCE = 1
//    private var poinDao: OttoPoinDao? = null
    private var isPinOttoPointConfirm = false
    private var trycount = 0
    val deleteConfirmationDialog = ConfirmationLogoutDialogFragment()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_dashboard_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
//        getMerchantStatus()
        //  callBalanceApi()
        //initPointSDK()p[[]]ppynhp
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == AppActivity.RESULT_OK) {
            isPinOttoPointConfirm = true
        }
    }

    override fun onResume() {
        super.onResume()
        getProfile()
//        if (isPinOttoPointConfirm) {
//            poinDao?.callAction(activity?.let { (it as AppActivity).longLat } ?: "")
//            poinDao = null
//        }
//
//        if (poinDao == null && !isPinOttoPointConfirm) {
//            initPointSDK()
//        }
    }

    private fun initView() {
        setBalanceBadge()
        setProfileListener()
        setMenuAccountInfo()
        setMenu()
        initDeleteConfirmationDialog()

        //initPointSDK()

        //Init Profile Container
        val ratioY = 204
        val ratioX = 324
        var width = getScreenWidth() - 36
        var height = (ratioY * width) / ratioX
        UIUtils.resize(ivProfileBg, width, height)

        if (SessionManager.getPrefLogin() != null && SessionManager.getPrefLogin().memberType != null) {
            tvLevelName.text = "${SessionManager.getPrefLogin().memberType.toUpperCase()} Member"
        }


        //Init Themes by Merchant Group
        var themeColor: String? = AppKeys.DEFAULT_THEME_COLOR

        if (SessionManager.getMerchantTheme() != null && SessionManager.getMerchantTheme().themeColor != null) {
            if (SessionManager.getMerchantTheme().themeColor.contains("#")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    themeColor = SessionManager.getMerchantTheme().themeColor
                }
            }
        }

        //Set Header Color Bg
        headerLayout.setBackgroundColor(Color.parseColor(themeColor))

        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setColor(Color.parseColor(themeColor))
        shape.setStroke(0, Color.BLACK)
        headerBg.setBackground(shape)

        ivSetting.setOnClickListener {
            val intent = Intent(activity, SettingActivity::class.java)
            intent.putExtra(SettingActivity.KEY_DATA_PROFILE, Gson().toJson(mProfile))
            startActivity(intent)
        }

        btnDownloadCard.setOnClickListener {
            //downloadMemberCard()
            generatePdfCard()
        }

        headerContainer.setOnClickListener {
            startActivity(Intent(activity, ProfileDetailActivity::class.java).apply {
                putExtra(SettingActivity.KEY_DATA_PROFILE, Gson().toJson(mProfile))
            })
        }
    }

    private fun generatePdfCard() {

        // create a new document
        val document = PdfDocument()

        val bitmapView = ScreenShott.getInstance().takeScreenShotOfView(headerContainer)

        // crate a page description
        val pageInfo = PdfDocument.PageInfo.Builder(
            bitmapView.width, bitmapView.height, 1
        )
            .create()

        // start a page
        val page = document.startPage(pageInfo)
        val canvas = page.getCanvas()
        val paint = Paint()
        paint.setColor(Color.BLACK)
        canvas.drawBitmap(bitmapView, null, Rect(0, 0, bitmapView.width, bitmapView.height), paint)

        // finish the page
        document.finishPage(page)

        // write the document content
        var directoryPath = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            directoryPath =
                activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath!!
        } else {
            directoryPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString()
        }

        val file = File(directoryPath)
        if (!file.exists()) {
            file.mkdirs()
        }

        var currentTimeMiilis = System.currentTimeMillis()
        val filePath =
            File(directoryPath, String.format("OttoKonek_Member_Card_%d.pdf", currentTimeMiilis))

        try {
            document.writeTo(FileOutputStream(filePath))
            (activity as DashboardActivity).showDownloadMemberCardNotif()
        } catch (e: IOException) {
            Log.e(TAG, "error " + e.toString())

            activity?.let {
                val dialog = ErrorDialog(
                    it,
                    activity,
                    false,
                    false,
                    getString(R.string.msg_download_member_card_failed),
                    ""
                )
                dialog.show()
            }
        }

        // close the document
        document.close()
    }

    private fun downloadMemberCard() {
        val bitmapView = ScreenShott.getInstance().takeScreenShotOfView(headerContainer)
        saveImageToGallery(bitmapView)
    }

    fun saveImageToGallery(qrImages: Bitmap?): Boolean {
        var result = false
        try {
            var currentTimeMiilis = System.currentTimeMillis()
            //Reset date to prevent multiple image
            //currentTimeMiilis = 1530005294
            var directory = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                directory =
                    activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath!! + "/OttoPay"
            } else {
                directory = Environment.getExternalStorageDirectory().absolutePath + "/OttoPay"
            }

            val f = File(directory)
            if (!f.exists()) {
                f.mkdirs()
            }
            val imageFile = File(directory, String.format("member_card_%d.jpg", currentTimeMiilis))
            saveBitmapToJPG(qrImages!!, imageFile)
            scanMediaFile(imageFile)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    fun saveBitmapToJPG(bitmap: Bitmap, photo: File?) {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream: OutputStream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
    }

    private fun scanMediaFile(imageFile: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(imageFile)
        mediaScanIntent.data = contentUri
        activity?.sendBroadcast(mediaScanIntent)

        activity?.let {
            getString(R.string.msg_save_member_card).showToast(it)
        }
    }

    private fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        activity?.getWindowManager()?.getDefaultDisplay()?.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    private fun setBalanceBadge() {
        iclOmzet.ivBalance.setImageResource(R.drawable.vector_logo_ottopay)
        iclOmzet.tvBalanceName.text = "Omzet"
        iclOmzet.tvPointLabel.gone()
        iclOmzet.tvBalance.text = "-"

        iclDeposit.ivBalance.setImageResource(R.drawable.vector_logo_ottopay)
        iclDeposit.tvBalanceName.text = "Deposit"
        iclDeposit.tvPointLabel.gone()
        iclDeposit.tvBalance.text = "-"

        iclOC.ivBalance.setImageResource(R.drawable.logo_otto_cash)
        iclOC.tvBalanceName.text = "OttoCash"
        iclOC.tvPointLabel.gone()
        iclOC.tvBalance.text = "-"

        iclOP.ivBalance.setImageResource(R.drawable.vector_logo_ottopoint)
        iclOP.tvBalanceName.text = "OttoPoint"
        iclOP.tvRupiahLabel.gone()
        iclOP.tvBalance.text = "-"

        //open balance list
//        btnOtherBalance.setOnClickListener {
//            startActivity(
//                Intent(
//                    activity,
//                    BalanceActivity::class.java
//                )
//            )
//        }
    }

    private fun setProfileListener() {
        iclOmzet.setOnClickListener {
            activity?.startActivity(
                Intent(
                    activity,
                    OmzetActivity::class.java
                ).apply { putExtra(OmzetActivity.KEY_IS_FROM_OMZET, true) })
        }
        iclDeposit.setOnClickListener {
            activity?.startActivity(
                Intent(
                    activity,
                    OmzetActivity::class.java
                ).apply { putExtra(OmzetActivity.KEY_IS_FROM_OMZET, false) })
        }
        iclOC.setOnClickListener {
            activity?.startActivity(
                Intent(
                    activity,
                    WalletOttocashActivity::class.java
                )
            )
        }
//        iclOP.setOnClickListener { callApiOttoPointBalance() }
//        iclOP.setOnClickListener {
//            pointStatus?.let {
//                if (it.equals(OttoPoinDao.NOT_ELIGIBLE_KEY, ignoreCase = true)) {
//                    InfoEligibleOttoPointDialog.showDialog(
//                        context,
//                        "",
//                        getString(R.string.message_feature_unavailable)
//                    ) { data -> }
//                } else {
////                    poinDao?.callAction(activity?.let { (it as AppActivity).longLat; } ?: "")
//                }
//            }
//        }
    }


    private fun setMenuAccountInfo() {
        rv_account_info.setHasFixedSize(true)
        rv_account_info.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )

        val menuList = mutableListOf<ProfileMenu>()

        //sales
        val changePin = ProfileMenu()
        changePin.name = "Sales"
        var intent = Intent(activity, OmzetActivity::class.java)
        intent.putExtra(OmzetActivity.KEY_IS_FROM_OMZET, true)
        changePin.intent = intent
        menuList.add(changePin)

        //Linked Account
        val contactUs = ProfileMenu()
        contactUs.name = "Linked Account"
        contactUs.intent = Intent(activity, ListLinkedBankAccountActivity::class.java)
        menuList.add(contactUs)


        //bank_account
        val logout = ProfileMenu()
        logout.name = "Bank Account"
        logout.intent = Intent(context, ListBankTransferActivity::class.java)
        menuList.add(logout)


        val adapter = ProfileMenuAdapter(context as Activity, menuList)
        rv_account_info.adapter = adapter

        adapter.setmOnViewClickListener(object : ProfileMenuAdapter.OnViewClickListener {
            override fun onViewClickListener(data: ProfileMenu, position: Int) {
                menuSelected(data, position)
            }
        })
    }

    private fun setMenu() {
        rvMenu.setHasFixedSize(true)
        rvMenu.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
//        context?.let { context ->
//            val itemDecorator = LineDivider(context, LineDivider.VERTICAL, 0, R.drawable.line_divider_vertical)
//            itemDecorator.isExcludeLastItem = true
//            rvMenu.addItemDecoration(itemDecorator)
//        }

        val menuList = mutableListOf<ProfileMenu>()

        val changePin = ProfileMenu()
        changePin.name = getString(R.string.ubah_kode_pin)
        var intent = Intent(activity, RegisterPinResetActivity::class.java)
        intent.putExtra("resetFunc", true)
        changePin.intent = intent
        menuList.add(changePin)

        //Contact Us
        val contactUs = ProfileMenu()
        contactUs.name = getString(R.string.profile_menu_contact_us)
        contactUs.intent = Intent(activity, ContactActivity::class.java)
        menuList.add(contactUs)


        //Logout
        val logout = ProfileMenu()
        logout.name = getString(R.string.profile_menu_sign_out)
        logout.intent = Intent(context, LoginActivity::class.java)
        menuList.add(logout)


        val adapter = ProfileMenuAdapter(context as Activity, menuList)
        rvMenu.adapter = adapter

        adapter.setmOnViewClickListener(object : ProfileMenuAdapter.OnViewClickListener {
            override fun onViewClickListener(data: ProfileMenu, position: Int) {
                menuSelected(data, position)
            }
        })
    }

    private fun initDeleteConfirmationDialog() {

        deleteConfirmationDialog.title = getString(R.string.dialog_title_logout)
        deleteConfirmationDialog.message = getString(R.string.dialog_sub_title_logout)
        deleteConfirmationDialog.negativeButton = getString(R.string.dialog_ask_location_btn_no)
        deleteConfirmationDialog.positiveButton = getString(R.string.dialog_ask_location_btn_yes)
        deleteConfirmationDialog.onPositiveButton = ::callLogoutApi
        deleteConfirmationDialog.onNegativeButton = ::onDeleteNegativeButton
    }

    fun onDeleteNegativeButton() {
        deleteConfirmationDialog.dismiss()
    }


    private fun menuSelected(data: ProfileMenu, position: Int) {
        when (data.name) {
            getString(R.string.profile_menu_sign_out) -> {

                deleteConfirmationDialog.show(
                    childFragmentManager,
                    deleteConfirmationDialog.getTag()
                )


                //Logout

            }

            "Bank Account" ->{
                comingSoonDialogBaseFragment()
            }

            else -> {
                if (data.intent != null) {
                    startActivity(data.intent)
                }
            }
        }
    }

    //exclusive for profile detail
    override fun onClick(v: View?) {
        startActivity(Intent(activity, ProfileDetailActivity::class.java))
    }

    private fun callLogoutApi() {
        deleteConfirmationDialog.dismiss()
        showProgressDialog(R.string.loading_message)
        OttoKonekAPI.logout(context, object : ApiCallback<BaseResponseModel>() {
            override fun onResponseSuccess(body: BaseResponseModel?) {
                dismissProgressDialog()
                if (isSuccess200) {
                    SessionManager.logout()

                    //Netcore
                    //Smartech.getInstance(WeakReference(context as Activity)).clearUserIdentity()
                    //Smartech.getInstance(WeakReference(context as Activity)).logoutAndClearUserIdentity(true)

                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    (context as Activity).finish()
                } else showErrorDialog(body?.meta?.message)
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                onApiResponseError()
            }

        })
    }

    private fun getProfile() {
//        ProfileDao(this).getProfile(BaseDao.getInstance(this, AppKeys.API_KEY_GET_PROFILE).callback)
        OttoKonekAPI.profile(context, object : ApiCallback<ProfileResponse>() {
            override fun onResponseSuccess(body: ProfileResponse?) {
                dismissProgressDialog()
                if (isSuccess200) {
                    body?.takeIf { context != null }?.run { handleProfileResponse(this) }
                } else showErrorDialog(body?.meta?.message)
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                retryProfileApi("Server Error")
            }

        })
    }

    private fun handleProfileResponse(profileResponse: ProfileResponse) {
        mProfile = profileResponse.data
        profileResponse.data?.run {
            tvMerchantName.text = storeName
            tvMid.text = mid?.replace("RTSM;;;;", "", true)
            ivMerchanttLogo.setImageResource(R.drawable.ic_ottopay_logo)
            SessionManager.getProfileLogo()?.let {
                Glide.with(ivMerchanttLogo)
                    .load(SessionManager.getProfileLogo())
                    .into(ivMerchanttLogo)
            }

            SessionManager.getProfileCardBg()?.let {
                Glide.with(ivProfileBg)
                    .load(SessionManager.getProfileCardBg())
                    .into(ivProfileBg)
            }

            tvLevelName.text = memberType?.toUpperCase(Locale.US)
        }
    }

    private fun getMerchantStatus() {
        OCBIDao(this).getProfile(
            BaseDao.getInstance(
                this,
                AppKeys.API_KEY_MERCHANT_STATUS
            ).callback
        )
    }

    private fun callBalanceApi() {
//        OCBIDao(this).getBalance(BaseDao.getInstance(this, KEY_BALANCE).callback)

        OttoKonekAPI.balance(context, object : ApiCallback<OttoKonekBalanceResponse>() {
            override fun onResponseSuccess(body: OttoKonekBalanceResponse?) {
                dismissProgressDialog()
                if (isSuccess200) {
                    body?.data?.takeIf { context != null }
                        ?.let { balance -> tvBalance.text = balance.formatted_balance }
                } else showErrorDialog(body?.meta?.message)
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                onApiResponseError()
            }

        })

        OttoKonekAPI.revenue(context, object : ApiCallback<OttoKonekBalanceResponse>() {
            override fun onResponseSuccess(body: OttoKonekBalanceResponse?) {
                dismissProgressDialog()
                if (isSuccess200) {
                    body?.data?.takeIf { context != null }
                        ?.let { revenue -> tvRevenue.text = revenue.formatted_balance }
                } else showErrorDialog(body?.meta?.message)
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                onApiResponseError()
            }

        })
    }

    private fun callApiOttoPointBalance() {
        ProgressDialogComponent.showProgressDialog(
            context,
            getString(R.string.loading_message),
            false
        )
        OttoPointDao.getBalance(context) { _, metaCode ->
            ProgressDialogComponent.dismissProgressDialog(activity as BaseActivity)
            when (metaCode) {
                200 -> OttomartActivity.openPage(context)
                201 -> startActivity(Intent(context, SyaratKetentuanActivity::class.java))
                else -> InfoEligibleOttoPointDialog.showDialog(
                    context,
                    "",
                    getString(R.string.text_phone_not_eligible)
                ) { data: Bundle? -> }
            }
        }
    }

    /*private fun initPointSDK() {
        poinDao = OttoPoinDao().apply { init(this@DashboardProfileFragment) }
        poinDao?.callAction(activity?.let { (it as AppActivity).longLat; }
                ?: "", OttoSDK.OTTOPOINT_SDK_LOADDATA)
    }*/

    override fun onApiResponseCallback(
        br: BaseResponse?,
        responseCode: Int,
        response: Response<*>?
    ) {
        super.onApiResponseCallback(br, responseCode, response)
        response?.let {
            if (it.isSuccessful) {
                if (responseCode == AppKeys.API_KEY_GET_PROFILE) {
                    if ((br as BaseResponseModel).meta.code == 200) {
                        getProfileSuccess((br as ProfileResponseModel).data)
                    } else {
                        val dialog = ErrorDialog(
                            context as Activity,
                            activity,
                            false,
                            false,
                            br.meta.message,
                            ""
                        )
                        dialog.setOnDismissListener {
                            activity?.finish()
                        }
                        dialog.show()
                    }
                } else if (responseCode == AppKeys.API_KEY_MERCHANT_STATUS) {
                    if ((br as BaseResponseModel).meta.code == 200) {
                        showMerchantStatus(br as MerchantStatusResponse)
                    }
                } else if (responseCode == KEY_BALANCE) {
                    br?.let {
                        if ((br as BaseResponseModel).meta.code == 200) {
                            context?.let { showBalance(br as BalanceResponse) }
                        }
                    }
                }
            }
        }
    }

    override fun onApiFailureCallback(message: String?) {
        //onApiResponseError()
    }

    private fun showBalance(balance: BalanceResponse) {
        balance.data?.balances?.forEach { item ->
            item?.name?.let { name ->
                when (name.toLowerCase()) {
                    "omzet" -> setBalance(
                        iclOmzet,
                        R.drawable.vector_logo_ottopay,
                        name,
                        item.formattedBalance
                    )
                    "deposit" -> setBalance(
                        iclDeposit,
                        R.drawable.vector_logo_ottopay,
                        name,
                        item.formattedBalance
                    )
                    "ottocash" -> setBalance(
                        iclOC,
                        R.drawable.logo_otto_cash,
                        name,
                        item.formattedBalance
                    )
                    "ottopoint" -> setBalance(
                        iclOP,
                        R.drawable.vector_logo_ottopoint,
                        name,
                        item.formattedBalance
                    )
                }
            }
        }
    }

    private fun setBalance(view: View, idRes: Int, name: String, balance: String?) {
        if (name.toLowerCase() == "ottopoint")
            view.tvPointLabel.visible()
        else view.tvRupiahLabel.visible()

        if (balance?.toLowerCase().equals("-")) {
            view.tvRupiahLabel.gone()
            view.tvPointLabel.gone()
        }

        view.ivBalance.setImageResource(idRes)
        view.tvBalanceName.text = name
        view.tvBalance.text = balance
    }

    private fun getProfileSuccess(data: ProfileResponseData?) {
//        mProfile = data
        if (tvMerchantName != null) {

            if (SessionManager.getMerchantTheme() != null && SessionManager.getMerchantTheme().profileBackgroundImage != null) {
                Glide.with(this)
                    .load(SessionManager.getMerchantTheme().profileBackgroundImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivProfileBg)
            }

            if (SessionManager.getMerchantTheme() != null && SessionManager.getMerchantTheme().dashboardLogo != null) {
                Glide.with(this)
                    .load(SessionManager.getMerchantTheme().dashboardLogo)
                    .error(R.drawable.ic_ottopay_logo)
                    .into(ivMerchanttLogo)
            }

            tvMerchantName.text = data?.name
            tvMid.text = "M.ID ${data?.merchantId}"
        }
    }

    private fun showMerchantStatus(merchantStatusResponse: MerchantStatusResponse) {
//        if (SessionManager.getPrefLogin() != null && SessionManager.getPrefLogin().memberType != null) {
//            tvLevelName.text = "${SessionManager.getPrefLogin().memberType.toUpperCase()} Member"
//        }

        val badgeRes = when (merchantStatusResponse.data?.level?.toLowerCase(Locale.ENGLISH)) {
            "silver" -> R.drawable.ic_silver_member
            "gold" -> R.drawable.ic_gold_member
            "platinum" -> R.drawable.ic_gold_member
            else -> {
                hideBadge()
                return
            }
        }

        showBadge(badgeRes)
    }

    private fun showBadge(badgeRes: Int) {

    }

    private fun hideBadge() {

    }

//    override fun callbackSDKClosed(status: String?) {
//        pointStatus = status
//        status?.let { status ->
//            if (status.equals(OttoPoinDao.REGISTER_KEY, ignoreCase = true) || status.equals(
//                    OttoPoinDao.ACTIVATE_KEY,
//                    ignoreCase = true
//                )
//            ) {
//                if (isPinOttoPointConfirm) {
//                    isPinOttoPointConfirm = false
//                }
//            }
//        }
//    }
//
//    override fun callbackBalance(balance: String?) {
//        Log.d("sdk balance : ", balance!!)
//    }
//
//    override fun callbackLoadData(p0: String?) {}
//
//    override fun callbackEligible(status: String?) {
//        Log.d("sdk clesed : ", status!!)
//    }

    private fun retryProfileApi(message: String) {
        if (trycount <= 2) {
            trycount++
            Handler(Looper.getMainLooper()).postDelayed({
                getProfile()
            }, 1000)

        } else {

            showErrorDialog(message)
        }

    }


}