package com.otto.mart.ui.activity.profile

import android.content.Intent
import android.os.Bundle
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.balance.BalanceResponse
import com.otto.mart.model.APIModel.Response.balance.BalancesItem
import com.otto.mart.presenter.dao.OCBIDao
//import com.otto.mart.presenter.dao.ottopoin.OttoPoinDao
import com.otto.mart.support.util.showToast
import com.otto.mart.ui.Partials.adapter.BalanceAdapter
import com.otto.mart.ui.Partials.decorator.LineDivider
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.deposit.ottocash.WalletOttocashActivity
import com.otto.mart.ui.activity.transaction.balance.OmzetActivity
import com.otto.mart.ui.component.dialog.InfoEligibleOttoPointDialog
import kotlinx.android.synthetic.main.activity_balance.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response
import java.util.*

class BalanceActivity : AppActivity() {

    private var pointStatus: String? = null
    private val KEY_BALANCE = 1
    private lateinit var adapter: BalanceAdapter
//    private var poinDao: OttoPoinDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance)
        initLocationPermission()
        initView()
        callBalanceApi()
        //initPointSDK()
    }

    /*private fun initPointSDK() {
        poinDao = OttoPoinDao().apply { init(this@BalanceActivity) }
        poinDao?.callAction(longLat, OttoSDK.OTTOPOINT_SDK_LOADDATA)
    }*/

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.label_saldo)
        imgToolbarLeft.setOnClickListener { finish() }

        adapter = BalanceAdapter(::onItemClicked)
        rvBalance.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rvBalance.addItemDecoration(LineDivider(this, LineDivider.VERTICAL, 0, R.drawable.line_divider_vertical).apply { isExcludeLastItem = true })
        rvBalance.adapter = adapter
    }

    private fun callBalanceApi() {
        OCBIDao(this).getBalance(BaseDao.getInstance(this, KEY_BALANCE).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        if ((br as BaseResponseModel).meta.code == 200) {
            adapter.balances = (br as BalanceResponse).data?.balances as MutableList?
        }
    }

    private fun onItemClicked(item: BalancesItem) {
        var intent: Intent? = null
        item.name?.let { name ->
            if (name.contains("cash", true)) {
                intent = Intent(this, WalletOttocashActivity::class.java)
            } else if (name.toLowerCase(Locale.ENGLISH).equals("omzet", true)) {
                intent = Intent(this, OmzetActivity::class.java).apply { putExtra(OmzetActivity.KEY_IS_FROM_OMZET, true) }
            } else if (name.toLowerCase(Locale.ENGLISH).equals("deposit", true)) {
                intent = Intent(this, OmzetActivity::class.java).apply { putExtra(OmzetActivity.KEY_IS_FROM_OMZET, false) }
            } else if (name.contains("point", true)) {
//                openOttoPoin()
                return
            } else {
                //next mapping
                intent = null
            }

            intent?.let { startActivity(it) } ?: "Halaman tidak tersedia".showToast(this)
        }
    }

//    private fun openOttoPoin() {
//        pointStatus?.let {
//            if (pointStatus.equals(OttoPoinDao.NOT_ELIGIBLE_KEY, ignoreCase = true)) {
//                InfoEligibleOttoPointDialog.showDialog(this, "", getString(R.string.message_feature_unavailable)) { data -> }
//            } else {
//                poinDao?.callAction(longLat)
//            }
//        }
//    }
//
//    override fun callbackSDKClosed(status: String?) {
//        pointStatus = status
//    }
//
//    override fun callbackBalance(jsonrespond: String?) { }
//
//    override fun callbackLoadData(p0: String?) { }
//
//    override fun callbackEligible(jsonrespond: String?) { }
}