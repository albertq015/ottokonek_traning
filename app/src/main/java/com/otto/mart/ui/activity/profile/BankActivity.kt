package com.otto.mart.ui.activity.profile

import android.content.Intent
import android.os.Bundle
import com.otto.mart.R
import com.otto.mart.api.OttoKonekAPI
import com.otto.mart.model.APIModel.Response.bank.BankAccountListOKKResponse
import com.otto.mart.support.util.ApiCallback
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.FragmentPagerAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.register.RegisterAddRekeningActivity
import com.otto.mart.ui.activity.transaction.balance.OmzetActivity
import com.otto.mart.ui.fragment.profile.BankListFragment
import kotlinx.android.synthetic.main.activity_bank.*
import kotlinx.android.synthetic.main.toolbar.*

class BankActivity : AppActivity() {
    companion object {
        const val requestBank = 3
        const val RESULT = "resultBank"
    }

    lateinit var adapter: FragmentPagerAdapter
    lateinit var approvedBankFrag: BankListFragment
    lateinit var onProcessBankFrag: BankListFragment
    lateinit var rejectedBankFrag: BankListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank)

        setupToolbar()
        setupTabLayout()
        callBankListApi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestBank && resultCode == RESULT_OK) {
            callBankListApi()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupToolbar() {
        btnToolbarBack.setOnClickListener { finish() }
        tvToolbarTitle.text = getString(R.string.title_bank_account_list)
        btnToolbarRight.visible()
        imgToolbarRight.setImageResource(R.drawable.ic_top_up_plus)
        btnToolbarRight.setOnClickListener {
            val intent = Intent(this, RegisterAddRekeningActivity::class.java)
            intent.putExtra(OmzetActivity.KEY_IS_FROM_DEPOSIT, true)
            startActivityForResult(intent, requestBank)
        }
    }

    private fun setupTabLayout() {
        approvedBankFrag = BankListFragment.newInstance(BankListFragment.approvedType)
        onProcessBankFrag = BankListFragment.newInstance(BankListFragment.pendingType)
        rejectedBankFrag = BankListFragment.newInstance(BankListFragment.rejectedType)

        adapter = FragmentPagerAdapter(supportFragmentManager)
        adapter.addFragment(approvedBankFrag, getString(R.string.text_approved))
        adapter.addFragment(onProcessBankFrag, getString(R.string.text_on_process))
        adapter.addFragment(rejectedBankFrag, getString(R.string.text_rejected))

        vp.offscreenPageLimit = 2
        vp.adapter = adapter
        tab.setupWithViewPager(vp)
    }

    private fun callBankListApi() {
        showProgressDialog(R.string.loading_message)
        OttoKonekAPI.bankAccountList(this, object : ApiCallback<BankAccountListOKKResponse>() {
            override fun onResponseSuccess(body: BankAccountListOKKResponse?) {
                dismissProgressDialog()
                if (isSuccess200) setBankListValueToFrag(body)
            }

            override fun onApiServiceFailed(throwable: Throwable?) {
                dismissProgressDialog()
                onApiResponseError()
            }
        })
    }

    private fun setBankListValueToFrag(body: BankAccountListOKKResponse?) {
        body?.data?.let { data ->
            data.approved?.let { approved -> approvedBankFrag.banks = approved }
            data.onProcess?.let { onProcess -> onProcessBankFrag.banks = onProcess }
            data.rejected?.let { rejected -> rejectedBankFrag.banks = rejected }
        }
    }
}
