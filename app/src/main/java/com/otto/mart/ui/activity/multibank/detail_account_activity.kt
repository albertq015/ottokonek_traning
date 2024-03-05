package com.otto.mart.ui.activity.multibank

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import com.bumptech.glide.Glide
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.multibank.HistoryAccountResponse
import com.otto.mart.model.APIModel.Response.multibank.HistoryTransactionAccount
import com.otto.mart.presenter.dao.TransactionDao

import com.otto.mart.support.util.DateUtil
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.multibank.HistoryAccountAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.cashOut.CashOutMenuActivity
import com.otto.mart.ui.activity.qr.scan.ScanQrActivity
import kotlinx.android.synthetic.main.activity_transaction_detail.*

import kotlinx.android.synthetic.main.content_omzet.*
import kotlinx.android.synthetic.main.content_omzet_button.*
import kotlinx.android.synthetic.main.item_list_linked_account.view.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class DetailAccountActivity : AppActivity() {
    val presenter: ListLinkedBankAccountPresenter = ListLinkedBankAccountPresenterImpl(this)
    private var mIsLoading = false
    private var mLimit = 10
    private var mPage = 1
    private var mDateFrom = ""
    private var mDateTo = ""
    private var mAccountNumber = ""
    val historyList = mutableListOf<HistoryTransactionAccount>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_omzet)
        init()
        initRecyclerview()
        getHistoryAccount()

    }

    fun init() {

        mAccountNumber = intent?.getStringExtra("accountNumber").toString()

        presenter.getDataAccount().observe(this,
            Observer<MutableList<LLBAViewModel>> {
                val t1 = it[intent.getIntExtra("pos", 0)].object2?.formatedBalance
                val t2 = it[intent.getIntExtra("pos", 0)].object2?.currencyCode
                val logo = it[intent.getIntExtra("pos", 0)].object1?.logo

                if (!t2.isNullOrEmpty()) {
                    tvBalance.text = "$t1"
                    sflBalance.stopShimmerAnimation()
                }

                Glide.with(imgLoggo)
                    .load(logo)
                    .into(imgLoggo)
            })
        sflBalance.startShimmerAnimation()
        tvToolbarTitle.text = "Detail Account"
        btnWithdrawContainer.visibility = View.GONE
        btnWithdrawDepositContainer.visibility = View.GONE
        imgInfo.visibility = View.GONE
        tvLabelBalance.text = "My Balance"
        btnFilter.visibility = View.GONE
        imgToolbarLeft.setOnClickListener {
            onBackPressed()
        }

        btnScanQr.setOnClickListener {
          //  startActivity(Intent(this, ScanQrActivity::class.java))
            comingSoonDialogBase()
        }

        btnCashOut.setOnClickListener {
          //  startActivity(Intent(this, CashOutMenuActivity::class.java))
            comingSoonDialogBase()
        }

        btnTransfer.setOnClickListener {
            comingSoonDialogBase()
//            presenter.getDataAccount().observe(this,
//                Observer<MutableList<LLBAViewModel>> {
//                    val name = it[intent.getIntExtra("pos", 0)].object1?.binName
//                    val number = it[intent.getIntExtra("pos", 0)].object1?.accountNumber
//                    val logo = it[intent.getIntExtra("pos", 0)].object1?.logo
//                    val bin = it[intent.getIntExtra("pos", 0)].object1?.bin
//                    val intent = Intent(this, TransferMenuAccountActivity::class.java)
//                    intent.putExtra("name", name)
//                    intent.putExtra("number", number)
//                    intent.putExtra("logo", logo)
//                    intent.putExtra("bin", bin)
//                    startActivity(intent)
//                })
        }
    }

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
                if (!mIsLoading) {
                    getHistoryAccount()
                    spinKit.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun getHistoryAccount() {
        mDateFrom = "2020-01-01"
        mDateTo = DateUtil.getCurrentDate("yyyy-MM-dd")

        mIsLoading = true
        TransactionDao(this).getHistoryAccount(
            mAccountNumber,
            mDateFrom, mDateTo,
            mLimit, mPage, BaseDao.getInstance(this, AppKeys.API_KEY_GET_OMSET_HISTORY).callback
        )

    }

    override fun onApiResponseCallback(
        br: BaseResponse?,
        responseCode: Int,
        response: Response<*>?
    ) {
        super.onApiResponseCallback(br, responseCode, response)
        response?.let {
            if (it.isSuccessful) {
                if ((br as BaseResponseModel).meta.code == 200) {
                    (br as HistoryAccountResponse).data?.let { it1 ->
                        it1.histories?.let {
                            displayHistory(it, br.count)
                        }

                    }
                }
            }
        }
    }


    fun onApiFailureCallback(message: String?) {
        onApiResponseError()
    }

    private fun displayHistory(transactions: List<HistoryTransactionAccount>, count: Int) {
        if (mPage == 1) {
            historyList.clear()
        }
        historyList.addAll(transactions)

        if (mPage == 1) {
            if (transactions.size > 0) {
                val adapter = HistoryAccountAdapter(this, historyList)
                // adapter.isFromOmzet = isFromOmzet
                recyclerview.adapter = adapter

                adapter.setmOnViewClickListener(object : HistoryAccountAdapter.OnViewClickListener {
                    override fun onViewClickListener(
                        item: HistoryTransactionAccount,
                        position: Int
                    ) {
//                        if (isFromOmzet) {
//                            historySelected(item, position)
//                        }
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

//        if ((transactions.size == 0) || (transactions.size == count)) {
//            mNomoreData = true
//        }

        viewAnimator.displayedChild = 1
        mPage++
        mIsLoading = false

        //Hide Loading
        Handler().postDelayed({
            spinKit.gone()
        }, 2000)
    }


}