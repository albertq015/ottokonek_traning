package com.otto.mart.ui.fragment.profile


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.keys.AppKeys
import com.otto.mart.keys.AppKeys.API_KEY_MERCHANT_STATUS
import com.otto.mart.model.APIModel.Misc.AddressModel
import com.otto.mart.model.APIModel.Misc.ProfileResponseData
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.MerchantStatusResponse
import com.otto.mart.model.APIModel.Response.ProfileResponseModel
import com.otto.mart.model.APIModel.Response.profile.ProfileData
import com.otto.mart.model.APIModel.Response.profile.ProfileResponse
import com.otto.mart.presenter.dao.OCBIDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.ApiCallback
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.profile.ProfileInfoAdapter
import com.otto.mart.ui.activity.profile.AddressListActivity
import com.otto.mart.ui.activity.profile.BankListActivity
import com.otto.mart.ui.activity.profile.ProfileChangeActivity
import com.otto.mart.ui.activity.settings.SettingActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.fragment.AppFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Response
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : AppFragment() {

    var mAddressList = mutableListOf<AddressModel>()
    private var mProfileData: ProfileData? = null

    var isMainAddressInvalid = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initRecyclerview()
        if (SessionManager.getPrefLogin() != null && SessionManager.getPrefLogin().memberType != null) {
            showMerchantStatus(SessionManager.getPrefLogin().memberType)
        }
    }

    override fun onResume() {
        super.onResume()
        getProfile()
//        getMerchantStatus()
    }

    private fun initView() {
        btnBackToolbar.setOnClickListener { activity?.finish() }

        var themeColor: String? = AppKeys.DEFAULT_THEME_COLOR
        if (SessionManager.getMerchantTheme() != null && SessionManager.getMerchantTheme().themeColor != null) {
            if (SessionManager.getMerchantTheme().themeColor.contains("#")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    themeColor = SessionManager.getMerchantTheme().themeColor
                }
            }
        }

        toolbarContainer.setBackgroundColor(Color.parseColor(themeColor))
        container.setBackgroundColor(Color.parseColor(themeColor))

        btnAddress.setOnClickListener {
            val intent = Intent(context, AddressListActivity::class.java)
            intent.putParcelableArrayListExtra("addresses", mAddressList as ArrayList<out Parcelable>)
            if (mAddressList.size == 0 || isMainAddressInvalid) {
                intent.putExtra(AddressListActivity.KEY_IS_ADD_ADDRESS, true);
            }
            startActivity(intent)
        }

        btnBank.setOnClickListener {
            val intent = Intent(context, BankListActivity::class.java)
            intent.putExtra(AppKeys.KEY_BANK_LIST_NOT_SELECTABLE, true)
            startActivity(intent)
        }

        btnEditProfile.setOnClickListener { btnEditClickedProfileInfo() }
    }

    private fun initRecyclerview() {
        rvProfile.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        rvProfile.layoutManager = linearLayoutManager

//        rvMenu.setHasFixedSize(true)
//        val menuLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
//        rvMenu.layoutManager = menuLayoutManager
    }

    private fun getProfileSuccess(data: ProfileResponseData?) {
//        mProfileData = data

        if (tvAvatarName != null) {

            Glide.with(this)
                    .load(data?.avatar)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .circleCrop()
                            .onlyRetrieveFromCache(false))
                    .into(imgAvatar)

            tvAvatarName.text = DataUtil.getInitialName(data?.name)
            tvName.text = data?.name
            tvMerchantId.text = "M.ID " + data?.merchantId

            //Display Profile Data List
            var profileDataList = mutableListOf<WidgetBuilderModel>()

            var name = WidgetBuilderModel()
            name.key = getString(R.string.label_full_name)
            name.value = data?.merchantName
            profileDataList.add(name)

            var phone = WidgetBuilderModel()
            phone.key = getString(R.string.label_phone_number)
            phone.value = data?.phone
            profileDataList.add(phone)

            var email = WidgetBuilderModel()
            email.key = "Email"
            email.value = data?.email
            profileDataList.add(email)

//            var dob = WidgetBuilderModel()
//            dob.key = "Tanggal Lahir"
//            dob.value = data?.dob
//            profileDataList.add(dob)
//
//            var bussinessName = WidgetBuilderModel()
//            bussinessName.key = "Nama Usaha"
//            bussinessName.value = data?.name
//            profileDataList.add(bussinessName)
//
//            var bussinessType = WidgetBuilderModel()
//            bussinessType.key = "Jenis Usaha"
//            bussinessType.value = data?.businessCategoryName
//            profileDataList.add(bussinessType)

            var adapter = ProfileInfoAdapter(context as Activity, profileDataList,
                    ::itemClickedProfileInfo, ::btnEditClickedProfileInfo)
            rvProfile.adapter = adapter


            //Address
            if (data != null && data.addresses != null && data.addresses.size > 0) {
                mAddressList = data.addresses
                var mainAddress = AddressModel()

                for (address in mAddressList) {
                    if (address.status.equals("main", true)) {
                        mainAddress = address
                    }
                }

                if (mainAddress.districtName == null || mainAddress.cityName == null || mainAddress.provinceName == null) {
                    isMainAddressInvalid = true
                    addressContentLayout.gone()
                    btnAddress.text = getString(R.string.label_add_address)
                } else {
                    var addressDetail = mainAddress.districtName + " - " + mainAddress.cityName +
                            " - " + mainAddress.provinceName

                    tvAddressTitle.text = getString(R.string.label_main_address)
                    tvAddress.text = mainAddress.completeAddress
                    tvAddress2.text = addressDetail
                }
            } else {
                addressContentLayout.gone()
                btnAddress.text = getString(R.string.label_add_address)
            }


            //Bank
            bankContainer.gone()
            btnBank.text = getString(R.string.button_add_bank_account)

            if (data?.accounts != null && data.accounts?.size!! > 0) {
                for (account in data.accounts) {
                    if (account.status.equals("main")) {
                        Glide.with(this).load(account.bankLogo).into(imgBank)
                        tvBankName.text = account.bankName
                        tvBankNoRek.text = account.accountNumber
                        tvBankAccountName.text = account.accountOwner
                        tvStatus.text = account.approvalStatus
                        setStatusAppearance(account.approvalStatus)
                        btnBank.text = getString(R.string.button_open_banks)
//                        bankContainer.visible()
                    }
                }
            }
        }
    }

    private fun setStatusAppearance(approvalStatus: String?) {
        approvalStatus?.let { status ->
            when (status.toLowerCase()) {
                "ditolak" -> setTextAppearance(tvStatus, R.color.faded_red, R.drawable.bg_red_bordered)
                "disetujui" -> setTextAppearance(tvStatus, R.color.dark_sky_blue, R.drawable.bg_blue_bordered)
                else -> setTextAppearance(tvStatus, R.color.brown_grey_three, R.drawable.bg_grey_bordered)
            }
        }
    }

    private fun setTextAppearance(tvStatus: TextView?, tvColor: Int, tvBackground: Int) {
        tvStatus?.apply {
            setTextColor(tvColor)
            setBackgroundResource(tvBackground)
        }
    }

    private fun itemClickedProfileInfo(item: WidgetBuilderModel, position: Int) {}

    private fun btnEditClickedProfileInfo() {
        val intent = Intent(context, ProfileChangeActivity::class.java)
        intent.putExtra("profile", activity?.intent?.getStringExtra(SettingActivity.KEY_DATA_PROFILE))
        startActivity(intent)
    }

    //region API Call

    private fun getProfile() {
//        ProfileDao(this).getProfile(BaseDao.getInstance(this, API_KEY_GET_PROFILE).callback)
        showProgressDialog(R.string.loading_message)
        OttoKonekAPI.profile(context, object : ApiCallback<ProfileResponse>() {
            override fun onResponseSuccess(body: ProfileResponse?) {
                dismissProgressDialog()
                if (isSuccess200) {
                    body?.let { handleProfileResponse(it) }
                } else showErrorDialog(body?.meta?.message)
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                onApiResponseError()
            }

        })
    }

    private fun handleProfileResponse(profileResponse: ProfileResponse) {
        val data = profileResponse.data
        data?.run {
            Glide.with(imgAvatar)
                    .load(profilePictureUrl)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .circleCrop()
                            .onlyRetrieveFromCache(false))
                    .into(imgAvatar)

            tvAvatarName.text = DataUtil.getInitialName(storeName)
            tvName.text = storeName
            tvMerchantId.text = mid?.replace("RTSM;;;;", "", true)

            val profileDataList = mutableListOf<WidgetBuilderModel>().apply {
                add(WidgetBuilderModel().apply {
                    key = getString(R.string.label_full_name)
                    value = storeName
                })

                add(WidgetBuilderModel().apply {
                    key = getString(R.string.label_phone_number)
                    value = SessionManager.getPhone()
                })

                add(WidgetBuilderModel().apply {
                    key = "Email"
                    value = email
                })
            }

            rvProfile.adapter = ProfileInfoAdapter(context as Activity, profileDataList,
                    ::itemClickedProfileInfo, ::btnEditClickedProfileInfo)

            val addressDetail = (addressBarangay?.plus(comaSeparator(addressMunicipality)) ?: "") +
                    (addressMunicipality?.plus(comaSeparator(addressProvince)) ?: "") +
                    (addressProvince?.plus(comaSeparator(addressRegion)) ?: "") +
                    (addressRegion ?: "")


            tvAddressTitle.text = getString(R.string.text_main_store)
            tvAddress.text = storeName
            tvAddress2.text = completeAddress
        }
    }

    private fun comaSeparator(field: String?): String {
        field?.takeIf { it.isNotEmpty() }?.run { return ", " }
        return ""
    }

    private fun getMerchantStatus() {
        OCBIDao(this).getProfile(BaseDao.getInstance(this, API_KEY_MERCHANT_STATUS).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        super.onApiResponseCallback(br, responseCode, response)
        if (responseCode == AppKeys.API_KEY_GET_PROFILE) {
            if ((br as BaseResponseModel).meta.code == 200) {
                getProfileSuccess((br as ProfileResponseModel).data)
            } else {
                val dialog = ErrorDialog(context as Activity, activity, false, false, br.meta.message, "")
                dialog.setOnDismissListener {
                    activity?.finish()
                }
                dialog.show()
            }
        } else if (responseCode == API_KEY_MERCHANT_STATUS) {
            if ((br as BaseResponseModel).meta.code == 200) {
//                showMerchantStatus(br as MerchantStatusResponse)
            }
        }
    }

    private fun showMerchantStatus(merchantStatusResponse: MerchantStatusResponse) {
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

        tvMemberStatus.text = "${merchantStatusResponse.data?.level} member"
        tvMemberStatus.visible()
        vDivider.visible()
    }

    private fun showMerchantStatus(memberType: String) {
        val badgeRes = when (memberType.toLowerCase()) {
            "silver" -> R.drawable.ic_silver_member
            "gold" -> R.drawable.ic_gold_member
            "platinum" -> R.drawable.ic_gold_member
            else -> {
                hideBadge()
                return
            }
        }

        showBadge(badgeRes)

        tvMemberStatus.text = memberType.toUpperCase()
        tvMemberStatus.visible()
        vDivider.visible()
    }

    private fun showBadge(badgeRes: Int) {
        ivMerchantBadge?.visible()
        Glide.with(ivMerchantBadge)
                .load(badgeRes)
                .into(ivMerchantBadge)
    }

    private fun hideBadge() {
        ivMerchantBadge?.gone()
    }

    override fun onApiFailureCallback(message: String?) {
        onApiResponseError()
    }

    //endregion API Call
}
