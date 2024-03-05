package com.otto.mart.ui.activity.transaction.balance

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import cl.jesualex.stooltip.Position
import cl.jesualex.stooltip.Tooltip
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.PaymentData
import com.otto.mart.model.APIModel.Misc.history.OmzetHistory
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagPaymentResponseModel
import com.otto.mart.model.APIModel.Response.balance.OttoKonekBalanceResponse
import com.otto.mart.model.APIModel.Response.history.BankHistoryResponse
import com.otto.mart.model.APIModel.Response.history.OmzetHistoryResponse
import com.otto.mart.model.localmodel.omzet.OmzetFilter
import com.otto.mart.presenter.dao.TransactionDao
import com.otto.mart.presenter.dao.WalletDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.DateUtil
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.pref.Pref
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.omzet.OmzetHistoryAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.Topup.TopupActivity
import com.otto.mart.ui.activity.cashOut.CashOutMenuActivity
import com.otto.mart.ui.activity.ppob.PpobPaymentDetailActivity
import com.otto.mart.ui.activity.qr.scan.ScanQrActivity
import com.otto.mart.ui.activity.transaction.transferToBank.TransferBankConfirmationActivity
import com.otto.mart.ui.activity.transaction.withdraw.WithdrawActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import com.otto.mart.ui.component.dialog.Popup
import com.otto.mart.ui.fragment.transaction.balance.DepositFilterFragment
import com.otto.mart.ui.fragment.transaction.balance.OmzetFilterFragment
import kotlinx.android.synthetic.main.content_omzet.*
import kotlinx.android.synthetic.main.content_omzet_button.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class OmzetActivity : AppActivity() {

    companion object {
        const val KEY_IS_FROM_OMZET = "is_from_omzet"
        const val KEY_IS_FROM_DEPOSIT = "is_from_deposit"
    }

    val TAG = javaClass.simpleName

    val KEY_GET_BALANCE: Int = 100

    private var mLimit = 10
    private var mPage = 1
    private var mDateFrom = ""
    private var mDateTo = ""
    private var mPaymentMethod = ""
    private var mTransactionType = ""

    private var mNomoreData = false
    private var mIsLoading = false
    private var isFromOmzet = false

    val mOmzetFilterFragment = OmzetFilterFragment()
    val mDepositFilterFragment = DepositFilterFragment()

    val historyList = mutableListOf<OmzetHistory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_omzet)

        if (intent.hasExtra(KEY_IS_FROM_OMZET)) {
            isFromOmzet = intent.getBooleanExtra(KEY_IS_FROM_OMZET, false)
        }

        initView()
        initRecyclerview()
        setupToolTip()
       // getBalance()
        getHistory()
    }

    private fun initView() {
        if (isFromOmzet) {
            tvToolbarTitle.text = getString(R.string.text_omzet)
            tvLabelBalance.text = getString(R.string.text_omzet)
            btnWithdrawDepositContainer.gone()
            btnOttoKonekContainer.gone()
            tvBalance.text = Pref.getPreference().getString(AppKeys.PREF_LAST_OMZET)
        } else {
            tvToolbarTitle.text = getString(R.string.text_deposite)
            tvLabelBalance.text = getString(R.string.text_deposite)
            btnWithdrawContainer.gone()
            btnWithdrawDepositContainer.gone()
            tvBalance.text = Pref.getPreference().getString(AppKeys.PREF_LAST_BALANCE)

            mDateFrom = "2020-01-01"
            mDateTo = DateUtil.getCurrentDate("yyyy-MM-dd")
        }

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        btnTopUp.setOnClickListener {
            val intent = Intent(this, TopupActivity::class.java)
            intent.putExtra(TopupActivity.KEY_IS_OTTOPAY, true)
            startActivity(intent)
        }

        btnWithdraw.setOnClickListener {
          //  gotoWithdraw()
            comingSoonDialogBase()
        }

        btnWithdrawDeposit.setOnClickListener {
           // gotoWithdraw()
            comingSoonDialogBase()
        }

        btnScanQr.setOnClickListener {
           // startActivity(Intent(this, ScanQrActivity::class.java))
            comingSoonDialogBase()
        }

        btnCashOut.setOnClickListener {
            startActivity(Intent(this, CashOutMenuActivity::class.java))
            comingSoonDialogBase()
        }

        btnTransfer.setOnClickListener {
            //gotoWithdraw()
            comingSoonDialogBase()
        }

        btnFilter.setOnClickListener {
            showFilterDialog()
        }
    }

    private fun gotoWithdraw() {
        val intent = Intent(this, WithdrawActivity::class.java)
        intent.putExtra(KEY_IS_FROM_OMZET, isFromOmzet)
        intent.putExtra(
            TransferBankConfirmationActivity.KEY_TRANSACTION_TYPE,
            TransferBankConfirmationActivity.TRANSACTION_TYPE.AJ
        )
        startActivity(intent)
    }

//    private fun comingSoonDialog() {
//        val dialog = Popup()
//        dialog.isHideBtnNegative = true
//        dialog.isHideBtnPositive = false
//        dialog.positiveButton = getString(R.string.oke)
//        dialog.title = getString(R.string.message_feature_coming_soon)
//        dialog.singleShow(supportFragmentManager, "popupInfo")
//    }

    private fun initRecyclerview() {
        recyclerview.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        recyclerview.setLayoutManager(linearLayoutManager)
        ViewCompat.setNestedScrollingEnabled(recyclerview, false)

        nestedScrollView.setOnScrollChangeListener { nestedScrollView: NestedScrollView, scrollX: Int, scrollY: Int,
                                                     oldScrollX: Int, oldScrollY: Int ->
            if (scrollY == (-1 * (nestedScrollView.getMeasuredHeight() - nestedScrollView.getChildAt(
                    0
                ).getMeasuredHeight()))
            ) {
                if (!mIsLoading && !mNomoreData) {
                    getHistory()
                    spinKit.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupToolTip() {
        imgInfo.setOnClickListener {
            var message = getString(R.string.deposit_tooltip_message)

            if (isFromOmzet) {
                message = getString(R.string.omzet_tooltip_message)
            }

            val tooltip = Tooltip.on(imgInfo)
                .text(message)
                .color(resources.getColor(R.color.blue_grey))
                .textSize(12f)
                .border(Color.BLACK, 1f)
                .clickToHide(true)
                .corner(5)
                .position(Position.BOTTOM)
                .show(5000)
        }
    }

    private fun showFilterDialog() {
        if (isFromOmzet) {
            mOmzetFilterFragment.applyFilterOmzet = ::applyFilterOmzet
            mOmzetFilterFragment.show(supportFragmentManager, mOmzetFilterFragment.getTag())
        } else {
            mDepositFilterFragment.applyFilterDeposit = ::applyFilterDeposit
            mDepositFilterFragment.show(supportFragmentManager, mDepositFilterFragment.getTag())
        }
    }

    private fun getOmzetBalanceSuccess(ottoKonekBalanceResponse: OttoKonekBalanceResponse) {
        if (ottoKonekBalanceResponse != null) {

            tvBalance.setText(ottoKonekBalanceResponse.data?.formatted_balance)

            //Save Omzet
            Pref.getPreference().putString(
                AppKeys.PREF_LAST_OMZET, tvBalance.text.toString()
                    .replace(getString(R.string.text_currency), "")
            )
        }
    }

    private fun getDepositBalanceSuccess(ottoKonekBalanceResponse: OttoKonekBalanceResponse) {
        tvBalance.setText(ottoKonekBalanceResponse.data?.formatted_balance)

        //Save Last Balance
        Pref.getPreference().putString(
            AppKeys.PREF_LAST_BALANCE, tvBalance.text.toString()
                .replace(getString(R.string.text_currency), "")
        )
    }

    private fun displayHistory(transactions: List<OmzetHistory>, count: Int) {
        if (mPage == 1) {
            historyList.clear()
        }
        historyList.addAll(transactions)

        if (mPage == 1) {
            if (transactions.size > 0) {
                val adapter = OmzetHistoryAdapter(this, historyList)
                adapter.isFromOmzet = isFromOmzet
                recyclerview.adapter = adapter

                adapter.setmOnViewClickListener(object : OmzetHistoryAdapter.OnViewClickListener {
                    override fun onViewClickListener(item: OmzetHistory, position: Int) {
                        if (isFromOmzet) {
                            historySelected(item, position)
                        }
                    }
                })

                historyLayout.visible()
                emptyLayout.gone()
            } else {
                historyLayout.gone()
                emptyLayout.visible()
            }
        } else {
            recyclerview.adapter?.notifyDataSetChanged()
        }

        if ((transactions.size == 0) || (transactions.size == count)) {
            mNomoreData = true
        }

        viewAnimator.displayedChild = 1
        mPage++
        mIsLoading = false

        //Hide Loading
        Handler().postDelayed({
            spinKit.gone()
        }, 2000)
    }

    private fun historySelected(item: OmzetHistory, position: Int) {

        val ppobOttoagPaymentResponseModel = PpobOttoagPaymentResponseModel()
        val paymentData = PaymentData()
        paymentData.uimsg = item.uiMsg

        if (isFromOmzet) {
            paymentData.keyValueList = SetupReceiptDetail().getOmzetReceipt(this, item)
        } else {
            paymentData.keyValueList = SetupReceiptDetail().getDepositReceipt(item)
        }

        ppobOttoagPaymentResponseModel.setData(paymentData)

        val intent = Intent(this, PpobPaymentDetailActivity::class.java)
        intent.putExtra(
            AppKeys.KEY_PPOB_PAYMENT_SUCCESS_DATA,
            Gson().toJson(ppobOttoagPaymentResponseModel)
        )
        intent.putExtra(PpobPaymentDetailActivity.KEY_STATUS, item.status)
        intent.putExtra(PpobPaymentDetailActivity.KEY_REFERENCE_NUMBER, item.reference_number)
        intent.putExtra(PpobPaymentDetailActivity.KEY_IS_FROM_OMZET, isFromOmzet)
        startActivity(intent)
    }

    fun applyFilterOmzet(
        startDate: String, endDate: String, paymentTypeList: MutableList<OmzetFilter>,
        transactionTypeList: MutableList<OmzetFilter>
    ) {
        if (!startDate.equals("") && !endDate.equals("")) {
            mDateFrom = DateUtil.formatDate(startDate, "yyyy-MM-dd", "dd-MM-yyyy")
            mDateTo = DateUtil.formatDate(endDate, "yyyy-MM-dd", "dd-MM-yyyy")
        }

        mPaymentMethod = ""
        for (omzetFilter in paymentTypeList) {
            var delimiter = ","
            if (mPaymentMethod.equals("")) {
                delimiter = ""
            }

            if (omzetFilter.isSelected) {
                mPaymentMethod += delimiter + omzetFilter.code
            }
        }

        if (paymentTypeList[0].isSelected) {
            mPaymentMethod = ""
        }

        mTransactionType = ""
        for (omzetFilter in transactionTypeList) {
            var delimiter = ","
            if (mTransactionType.equals("")) {
                delimiter = ""
            }

            if (omzetFilter.isSelected) {
                mTransactionType += delimiter + omzetFilter.code
            }
        }

        if (transactionTypeList[0].isSelected) {
            mTransactionType = ""
        }

        callApiApplyFilter()
    }

    fun applyFilterDeposit(startDate: String, endDate: String) {
        if (!startDate.equals("") && !endDate.equals("")) {
            mDateFrom = DateUtil.formatDate(startDate, "yyyy-MM-dd", "dd-MM-yyyy")
            mDateTo = DateUtil.formatDate(endDate, "yyyy-MM-dd", "dd-MM-yyyy")
        }

        callApiApplyFilter()
    }

    private fun callApiApplyFilter() {
        mPage = 1
        mNomoreData = false
        viewAnimator.displayedChild = 0
        historyLayout.gone()
        emptyLayout.gone()
        getHistory()
    }


    //region API Call

    private fun getBalance() {
        if (isFromOmzet) {
            TransactionDao(this).revenue(BaseDao.getInstance(this, KEY_GET_BALANCE).callback)
        } else {
            WalletDao(this).balance(BaseDao.getInstance(this, KEY_GET_BALANCE).callback)
        }
    }

    private fun getHistory() {
        mIsLoading = true

        if (isFromOmzet) {
            TransactionDao(this).getOmsetHistory(
                mDateFrom, mDateTo, mPaymentMethod, mTransactionType,
                mLimit, mPage, BaseDao.getInstance(this, AppKeys.API_KEY_GET_OMSET_HISTORY).callback
            )
        } else {
            WalletDao(this).getWalletHistory(
                SessionManager.getPrefLogin().account_number,
                mDateFrom,
                mDateTo,
                mLimit,
                mPage,
                BaseDao.getInstance(this, AppKeys.API_KEY_GET_OMSET_HISTORY).callback
            )
        }
    }

    override fun onApiResponseCallback(
        br: BaseResponse?,
        responseCode: Int,
        response: Response<*>?
    ) {
        super.onApiResponseCallback(br, responseCode, response)
        response?.let {
            if (it.isSuccessful()) {
                when (responseCode) {
                    KEY_GET_BALANCE -> if ((br as BaseResponseModel).meta.code == 200) {
                        if (isFromOmzet) {
                            getOmzetBalanceSuccess((br as OttoKonekBalanceResponse))
                        } else {
                            getDepositBalanceSuccess((br as OttoKonekBalanceResponse))
                        }
                    } else {
                        val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                        dialog.show()
//                        dialog.setOnDismissListener {
////                            getBalance()
//                        }
                    }
                    AppKeys.API_KEY_GET_OMSET_HISTORY -> {
                        viewAnimator.setDisplayedChild(1)
                        if ((br as BaseResponseModel).meta.code == 200) {
                            if (isFromOmzet) {
                                (br as OmzetHistoryResponse).data?.let { it1 ->
                                    displayHistory(it1, br.count)
                                }
                            } else {
                                (br as BankHistoryResponse).data?.let { it1 ->
                                    it1.histories?.let {
                                        displayHistory(it, br.count)
                                    }
                                }
                            }
                        } else if (br.meta.code == 400) {
                            emptyLayout.setVisibility(View.VISIBLE)
                            val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                            dialog.show()
                        }
                    }
                }
            }
        }
    }

    protected fun onApiFailureCallback(message: String?) {
        onApiResponseError()
    }

    //endregion API Call
}
