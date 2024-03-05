package com.otto.mart.ui.fragment.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel
import com.otto.mart.model.APIModel.Response.bank.BankOKKItem
import com.otto.mart.support.util.isVisible
import com.otto.mart.support.util.px
import com.otto.mart.support.util.showToast
import com.otto.mart.ui.Partials.adapter.BankAdapter
import com.otto.mart.ui.Partials.decorator.SpaceDecorator
import com.otto.mart.ui.activity.profile.BankActivity
import com.otto.mart.ui.activity.register.RegisterAddRekeningActivity
import kotlinx.android.synthetic.main.fragment_bank_list.*

class BankListFragment : Fragment(R.layout.fragment_bank_list) {

    companion object {
        private val typeKey = "typeKey"
        private val selectedIdKey = "selectedId"
        const val approvedType = 1
        const val rejectedType = 2
        const val pendingType = 3

        @JvmStatic
        fun newInstance(type: Int, selectedId: Int? = null): BankListFragment =
                BankListFragment().apply {
                    arguments = Bundle().apply {
                        putInt(typeKey, type)
                        putInt(selectedIdKey, selectedId ?: -1)
                    }
                }
    }

    private val none = -1
    private var currentType: Int? = null
    private var selectedBank: BankOKKItem? = null
    private lateinit var adapter: BankAdapter
    var banks = listOf<BankOKKItem>()
        set(value) {
            field = value
            context?.let {
                adapter.banks = field
                validateBankSize()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentType = arguments?.getInt(typeKey, none) ?: none
        initContent()
    }

    override fun onStart() {
        super.onStart()
        adapter.banks?.takeIf { it.isNotEmpty() }?.takeIf { it.size != banks?.size }?.let {
            adapter.banks = banks
        }

        validateBankSize()
    }

    private fun initContent() {

        tvBankProceed.isVisible(currentType == pendingType)
        btnConfirm.isVisible(currentType == approvedType)

        val selectedId = arguments?.getString(selectedIdKey) ?: ""
        adapter = BankAdapter().apply {
            selectedListener = ::onBankSelected
            selectedItemId = selectedId
            actionListener = ::onEditBank
            currentStatus = currentType ?: pendingType
        }

        rvBanks.layoutManager = LinearLayoutManager(context)
        rvBanks.addItemDecoration(SpaceDecorator(12.px(), LinearLayout.VERTICAL, 24.px(), 20.px(), 20.px()))
        rvBanks.adapter = adapter

        btnConfirm.setOnClickListener {
            getConvertedModel()?.let { selectedBank ->
                activity?.setResult(RESULT_OK, Intent().apply {
                    putExtra(BankActivity.RESULT, Gson().toJson(selectedBank))
                })

                activity?.finish()
            } ?: "Please choose bank account".showToast(it.context)
        }
    }

    private fun validateBankSize() {

        banks.isEmpty().let { isNotEmpty ->
            ivEmpty.isVisible(isNotEmpty)
            tvEmptyMessage.isVisible(isNotEmpty)
            rvBanks.isVisible(!isNotEmpty)
        }

    }

    private fun getConvertedModel(): BankAccountModel? =
            selectedBank?.let { selectedBank ->
                BankAccountModel().apply {
                    bankCode = selectedBank.bankCode
                    accountNumber = selectedBank.accountNumber
                    bankName = selectedBank.bankName
                    bankLogo = selectedBank.urlImage
                    accountOwner = selectedBank.accountNumber
                    accountName = selectedBank.accountName
                    resLogo = selectedBank.resLogo ?: -1
                }
            }

    private fun onBankSelected(bankAccountModel: BankOKKItem) {
        selectedBank = bankAccountModel
        btnConfirm.isEnabled = true
    }

    private fun onEditBank(bankAccountModel: BankOKKItem) {
        activity?.startActivityForResult(
                Intent(activity, RegisterAddRekeningActivity::class.java).apply {
                    putExtra("isEdit", true)
                    putExtra("isInternalEdit", true)
                    putExtra("norek", bankAccountModel.accountNumber)
                    putExtra("name", bankAccountModel.accountName)
                    putExtra("bankCode", bankAccountModel.bankCode)
                    putExtra("id", bankAccountModel.id)
                }, BankActivity.requestBank)

    }
}