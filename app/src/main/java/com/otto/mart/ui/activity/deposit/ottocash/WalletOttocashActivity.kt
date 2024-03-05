package com.otto.mart.ui.activity.deposit.ottocash

import android.content.Intent
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.OmzetHistoryResponseData
import com.otto.mart.model.APIModel.Misc.history.OmzetHistory
import com.otto.mart.model.APIModel.Request.OmsetHistoryRequestModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.OmzetHistoryResponseModel
import com.otto.mart.model.APIModel.Response.WalletResponseModel
import com.otto.mart.model.APIModel.Response.balance.BalanceResponse
import com.otto.mart.model.APIModel.Response.ottocash.StatusOttocashResponse
import com.otto.mart.model.localmodel.ui.GridMenu
import com.otto.mart.presenter.dao.OCBIDao
import com.otto.mart.presenter.dao.WalletDao
import com.otto.mart.support.util.DataUtil
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.GridMenuAdapter
import com.otto.mart.ui.Partials.adapter.WalletLatestHistoryAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.NearbyMerchant.NearbyMerchantActivity
import com.otto.mart.ui.activity.Topup.TopupActivity
import com.otto.mart.ui.activity.profile.ProfileChangePinActivity
import com.otto.mart.ui.activity.qr.scan.ScanQrActivity
import com.otto.mart.ui.activity.register.registerFromSales.registerPhotoInput.ShowKTPActivity.KEY_IS_FROM_WALLET
import com.otto.mart.ui.activity.register.registerFromSales.registerPhotoInput.TakePictureActivity
import com.otto.mart.ui.activity.syaratDanKetentuan.SnKActivity
import com.otto.mart.ui.activity.transaction.history.HistoryActivity
import com.otto.mart.ui.activity.deposit.HistoryTransaction
import com.otto.mart.ui.activity.transaction.alfamart.AlfamartShowPaymentCodeActivity
import com.otto.mart.ui.activity.wallet.PayWalletActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.content_wallet_ottocash.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class WalletOttocashActivity : AppActivity(), androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {

    companion object {
        val API_KEY_BALANCE_OTTOCASH = 100
    }

    private var balance : Long= 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_ottocash)

        initView()
        initRecyclerview()
        displayMenu()
        callBalancOttoCasheApi()
        getStatusOttoCash()
    }

    override fun onRefresh() {
        callBalancOttoCasheApi()
        getStatusOttoCash()
    }

    private fun initRecyclerview() {
        nestedScrollView.isNestedScrollingEnabled = true

        rvMenu.setHasFixedSize(false)
        val gridLayoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 4)
        rvMenu.setLayoutManager(gridLayoutManager)

        rvHistory.setHasFixedSize(true)
        val linearLayoutManagerHistory = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        rvHistory.setLayoutManager(linearLayoutManagerHistory)
    }

    private fun initView() {
        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

//        if (GLOBAL.isOttoCash) {
        tvToolbarTitle.text = getString(R.string.title_activity_wallet_ottocash)
        headerContactUs.text = getString(R.string.label_oc_contact_us)
        emailWallet.text = getString(R.string.label_email)
        tvEmailWallet.text = getString(R.string.text_oc_email)
        phoneNumberWallet.text = getString(R.string.label_telephone_number)
        tvPhoneNumberWallet.text = getString(R.string.text_phone_oc_value)
        websiteWallet.text = getString(R.string.text_website)
        tvWebsiteWallet.text = getString(R.string.text_oc_url)
        timeWallet.text = getString(R.string.label_working_hour)
        tvTimeWallet.text = getString(R.string.text_working_hour)
        saldo_name.text = getString(R.string.label_oc_balance)
        logo_wallet.setImageResource(R.drawable.ic_ottocash_logo)

        changePin.setOnClickListener {
            var intent = Intent(this, ProfileChangePinActivity::class.java)
            startActivity(intent)
        }
        btnTnc.setOnClickListener {
            startActivity(Intent(this, SnKActivity::class.java).apply { putExtra(SnKActivity.KEY_URL_CONTENT, getString(R.string.url_tnc_ottocash)) })
        }
        btn_upgrade.setOnClickListener {
            if (tv_upgrade.text.equals("Upgrade")) {
                var intent = Intent(this, TakePictureActivity::class.java)
                intent.putExtra(KEY_IS_FROM_WALLET, true)
                startActivity(intent)
            } else {
                btn_upgrade.isEnabled = false
            }
        }
    }

    private fun displayMenu() {
        val menuList = mutableListOf<GridMenu>()

        val intentScanQr = Intent(this, PayWalletActivity::class.java)
        intentScanQr.putExtra(AlfamartShowPaymentCodeActivity.KEY_IS_OTTOCASH, true)

        val Bayar = GridMenu()
        Bayar.name = "Bayar"
        Bayar.icon = R.drawable.ic_bayar
        Bayar.intent = intentScanQr
        menuList.add(Bayar)

        val merchantTerdekat = GridMenu()
        merchantTerdekat.name = "Merchant\nTerdekat"
        merchantTerdekat.icon = R.drawable.group_29
        merchantTerdekat.intent = Intent(this, NearbyMerchantActivity::class.java)
        menuList.add(merchantTerdekat)

        //Top Up
        val intentTopUp = Intent(this, TopupActivity::class.java)
        intentTopUp.putExtra(TopupActivity.KEY_IS_OTTOPAY, false)

        val topUp = GridMenu()
        topUp.name = "Top Up"
        topUp.icon = R.drawable.ic_topup
        topUp.intent = intentTopUp
        menuList.add(topUp)

        val history = GridMenu()
        history.name = "Riwayat\nTransaksi"
        history.icon = R.drawable.ic_history_new
        history.intent = Intent(this, HistoryTransaction::class.java)
        menuList.add(history)

        val kirimHadiah = GridMenu()
        kirimHadiah.name = "Kirim\nHadiah"
        kirimHadiah.icon = R.drawable.ic_gift
        //kirimHadiah.intent = Intent(this, TransferSaldoActivity::class.java)
        kirimHadiah.code
        menuList.add(kirimHadiah)

        val penarikanUang = GridMenu()
        penarikanUang.name = "Penarikan\nUang"
        penarikanUang.icon = R.drawable.ic_cashout
        //kirimHadiah.intent = Intent(this, TransferSaldoActivity::class.java)
        menuList.add(penarikanUang)

        val permintaanUang = GridMenu()
        permintaanUang.name = "Permintaan\nUang"
        permintaanUang.icon = R.drawable.ic_askmoney
        //kirimHadiah.intent = Intent(this, TransferSaldoActivity::class.java)
        menuList.add(permintaanUang)

        val transferUang = GridMenu()
        transferUang.name = "Transfer\nUang"
        transferUang.icon = R.drawable.ic_transfer_new
        //kirimHadiah.intent = Intent(this, TransferSaldoActivity::class.java)
        menuList.add(transferUang)


        //        GridMenu history = new GridMenu();
        //        history.setName("Riwayat");
        //        history.setIcon(R.drawable.ic_menu_transfer);
        //        history.setIntent(new Intent(this, HistoryActivity.class));
        //        menuList.add(history);

        val gridMenuAdapter = GridMenuAdapter(this, menuList)
        rvMenu.adapter = gridMenuAdapter


        gridMenuAdapter.setmOnViewClickListener { gridMenu, position ->
            if (position >= 4) {
                val errorDialog = ErrorDialog(this, this, false, true, "Fitur akan segera hadir", "")
                errorDialog.show()
            } else {
                intentScanQr.putExtra("balance", balance)
                startActivity(gridMenu.intent)
            }
        }

        swipeRefresh.setOnRefreshListener { getWalletInfo() }

        btnMore.setOnClickListener({ v -> startActivity(Intent(this, HistoryActivity::class.java)) })
    }

    private fun displayHistory(transactions: List<OmzetHistory>) {
        val LIMIT = 3

        if (transactions.size > 0) {
            val displayList = mutableListOf<OmzetHistory>()

            for (transaction in transactions) {
                if (displayList.size < LIMIT) {
                    displayList.add(transaction)
                }
            }

            val adapter = WalletLatestHistoryAdapter(this, displayList)
            rvHistory.adapter = adapter
            viewAnimator.displayedChild = 1
        } else {
            viewAnimator.visibility = View.GONE
        }
    }


    //region API Call

    private fun getWalletInfo() {
        WalletDao(this).emoneySummary(BaseDao.getInstance(this@WalletOttocashActivity, AppKeys.API_KEY_WALLET_INFO).callback)
    }

    private fun getStatusOttoCash() {
        OCBIDao(this).getOttocashStatus(BaseDao.getInstance(this, AppKeys.API_KEY_GET_OC_STATUS).callback)
    }

    private fun callBalancOttoCasheApi() {
        OCBIDao(this).getBalance(BaseDao.getInstance(this, API_KEY_BALANCE_OTTOCASH).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        swipeRefresh.isRefreshing = false
        if (responseCode == AppKeys.API_KEY_WALLET_HISTORY) {
            if ((br as BaseResponseModel).meta.code == 200) {
                val model = br as OmzetHistoryResponseModel?
                if (model!!.transactions != null) {
                    displayHistory(model.transactions)
                }
            } else {
                viewAnimator.setVisibility(View.GONE)
            }
        } else if (responseCode == AppKeys.API_KEY_GET_OC_STATUS) {
            if ((br as BaseResponseModel).meta.code == 200) {
                doCheckOCStatus(br as StatusOttocashResponse)
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        swipeRefresh.isRefreshing = false
        onApiResponseError()
    }

    //endregion API Call

    private fun doCheckOCStatus(response: StatusOttocashResponse) {
        tvBalance.text = DataUtil.convertCurrency(response.data?.emoneyBalance)
        balance = DataUtil.getLong(response.data?.emoneyBalance)

        val verifyStatus = response.data?.verifyStatus
        if (verifyStatus.equals("0").or(verifyStatus.equals("3"))) {
            tvUpgradeStatus.gone()
            btn_upgrade.visible()
        } else if (verifyStatus.equals("1")) {
            tvUpgradeStatus.visible()
            btn_upgrade.gone()
        } else {
            tvUpgradeStatus.gone()
            btn_upgrade.gone()
        }
    }

}
