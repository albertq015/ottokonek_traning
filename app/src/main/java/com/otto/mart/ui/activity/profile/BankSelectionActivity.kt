package com.otto.mart.ui.activity.profile

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.model.APIModel.Misc.bank.BankAccountModel
import com.otto.mart.model.APIModel.Request.BankEditRequestModel
import com.otto.mart.model.APIModel.Request.bank.AddBankDepositRequest
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.bank.BankAccountListDepositResponse
import com.otto.mart.model.APIModel.Response.bank.BankAccountListResponseModel
import com.otto.mart.model.APIModel.Response.ottocash.StatusOttocashResponse
import com.otto.mart.presenter.dao.OCBIDao
import com.otto.mart.presenter.dao.ProfileDao
import com.otto.mart.presenter.dao.deposit.TransferDepositDao
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.px
import com.otto.mart.support.util.showToast
import com.otto.mart.support.util.visible
import com.otto.mart.support.util.widget.dialog.ConfirmationDialogFragment
import com.otto.mart.ui.Partials.adapter.BankSelectionAdapter
import com.otto.mart.ui.Partials.decorator.SpaceDecorator
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.register.RegisterAddRekeningActivity
import com.otto.mart.ui.activity.transaction.balance.OmzetActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_bank_selection.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class BankSelectionActivity : AppActivity() {

    private lateinit var adapter: BankSelectionAdapter
    private val disposable = CompositeDisposable()
    private var selectedBank: BankAccountModel? = null
    private var selectedDeleteBank: BankAccountModel? = null
    private val filterKey by lazy { getString(R.string.text_approved) }
    private var customerName = ""
    private var isFromOmzet = false
    val deleteConfirmationDialog = ConfirmationDialogFragment()

    companion object {
        const val SELECTED_ID = "selectedId"
        const val RESULT = "resultBank"
        const val BANK_VA_ID = -1
        private const val ADD_BANK = 421
        val API_KEY_GET_GET_STATUS_OC = 100
        val API_KEY_GET_BANK_LIST = 101
        val API_KEY_BANK_LIST_DEPOSIT = 102
        val API_KEY_DELETE_BANK_DEPOSIT = 103
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_selection)

        if (intent.hasExtra(OmzetActivity.KEY_IS_FROM_OMZET)) {
            isFromOmzet = intent.getBooleanExtra(OmzetActivity.KEY_IS_FROM_OMZET, false)
        }

        initView()
        initDeleteConfirmationDialog()
    }

    override fun onResume() {
        super.onResume()
        if (isFromOmzet) {
            getStatusOttoCash()
        } else {
            getBankListDeposit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == ADD_BANK) {
            val id = data!!.getIntExtra("id", -1)
            val model = BankEditRequestModel()
            model.account_number = data.getStringExtra("accountNum")
            model.account_owner = data.getStringExtra("accountName")
            model.bank_id = data.getIntExtra("bankid", -1)
            model.isSet_as_main = data.getBooleanExtra("setMain", false)
            if (id == -1) {
                ProfileDao(this).addBank(model, BaseDao.getInstance(this, 910).callback)
            }
        }
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.text_bank_list)
        btnToolbarBack.setOnClickListener { finish() }

        imgToolbarRight.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add_toolbar))
        btnToolbarRight.visible()

        val selectedId = intent.getIntExtra(SELECTED_ID, -1)
        adapter = BankSelectionAdapter().apply {
            listener = ::onBankSelected
            selectedItemId = selectedId
        }
        rvBank.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rvBank.addItemDecoration(SpaceDecorator(12.px(), LinearLayout.VERTICAL, 24.px(), 20.px(), 20.px()))
        adapter.onDeleteBank = ::onDeleteBank
        rvBank.adapter = adapter

        btnChooseBank.setOnClickListener {
            when {
                selectedBank == null -> getString(R.string.text_choose_bank_account).showToast(this)
                selectedBank?.id == BANK_VA_ID -> {
                    setResult()
                }
                !selectedBank?.approvalStatus.equals(filterKey, true) -> {
                    val message = if (selectedBank?.reason?.isEmpty() == true) getString(R.string.text_bank_account_on_process) else selectedBank?.reason
                    ErrorDialog(this, this, true, false, message, "").show()
                }
                else -> setResult()
            }
        }

        btnToolbarRight.setOnClickListener {
            if (isFromOmzet) {
                startActivityForResult(Intent(this, RegisterAddRekeningActivity::class.java), ADD_BANK)
            } else {
                val intent = Intent(this, RegisterAddRekeningActivity::class.java)
                intent.putExtra(OmzetActivity.KEY_IS_FROM_DEPOSIT, !isFromOmzet)
                startActivity(intent)
            }
        }
    }

    private fun initDeleteConfirmationDialog() {
        deleteConfirmationDialog.title = getString(R.string.text_delete_bank_account)
        deleteConfirmationDialog.message = getString(R.string.message_delete_bank_account)
        deleteConfirmationDialog.negativeButton = getString(R.string.text_return)
        deleteConfirmationDialog.positiveButton = getString(R.string.text_delete_yes)
        deleteConfirmationDialog.onPositiveButton = ::onDeletePositiveButton
        deleteConfirmationDialog.onNegativeButton = ::onDeleteNegativeButton
    }

    private fun setResult() {
        val intentResult = Intent().apply {
            putExtra(RESULT, Gson().toJson(selectedBank))
        }

        setResult(RESULT_OK, intentResult)
        finish()
    }

    private fun callBankListAPI() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
        ProfileDao(this).getBankList(BaseDao.getInstance(this, API_KEY_GET_BANK_LIST).callback)
    }

    private fun getStatusOttoCash() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
        OCBIDao(this).getOttocashStatus(BaseDao.getInstance(this, API_KEY_GET_GET_STATUS_OC).callback)
    }

    private fun getBankListDeposit() {
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false)
        TransferDepositDao(this).bankListDeposit(BaseDao.getInstance(this, API_KEY_BANK_LIST_DEPOSIT).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        if (!isFinishing) ProgressDialogComponent.dismissProgressDialog(this)
        if (response != null) {
            if (response.isSuccessful) {
                br.let {
                    when (responseCode) {
                        API_KEY_GET_GET_STATUS_OC -> {
                            if ((it as BaseResponseModel).meta.code == 200) {
                                var statusOttocashResponse = it as StatusOttocashResponse
                                statusOttocashResponse.data.let {
                                    customerName = it?.customerName.toString()
                                }
                            }
                            callBankListAPI()
                        }
                        API_KEY_GET_BANK_LIST -> {
                            if ((it as BaseResponseModel).meta.code == 200) {
                                doMappingBank(it as BankAccountListResponseModel)
//                                setBanksData((it as BankListResponseModel).data.account)
                            } else {
                                val dialog = ErrorDialog(this, this, false, false, it.meta.message, "")
                                dialog.setOnDismissListener {
                                    finish()
                                }
                                dialog.show()
                            }
                        }
                        API_KEY_BANK_LIST_DEPOSIT -> {
                            if ((it as BaseResponseModel).meta.code == 200) {
                                displayBankListDeposit(it as BankAccountListDepositResponse)
                            } else {
                                val dialog = ErrorDialog(this, this, false, false, it.meta.message, "")
                                dialog.setOnDismissListener {
                                    finish()
                                }
                                dialog.show()
                            }
                        }
                        API_KEY_DELETE_BANK_DEPOSIT -> {
                            if ((it as BaseResponseModel).meta.code == 200) {
                                getBankListDeposit()
                            } else {
                                val dialog = ErrorDialog(this, this, false, false, it.meta.message, "")
                                dialog.setOnDismissListener {
                                    finish()
                                }
                                dialog.show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        if (!isFinishing) ProgressDialogComponent.dismissProgressDialog(this)
        super.onApiFailureCallback(message, ac)
    }

    private fun displayBankListDeposit(bankAccountListDepositResponse: BankAccountListDepositResponse) {
        bankAccountListDepositResponse.data?.let {
            if (it.size > 0) {
                tvEmpty.gone()
            } else {
                tvEmpty.visible()
            }

            adapter.isFromDeposit = !isFromOmzet
            adapter.banks = it
        }
    }


    private fun doMappingBank(responseAccount: BankAccountListResponseModel) {
        disposable.add(
                Observable.defer { Observable.fromIterable(responseAccount.data.account) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.computation())
                        .filter { it.approvalStatus.equals(filterKey, true) }
                        .toList()
                        .subscribe({ banks ->
                            setBanksData(banks)
                        }, Throwable::printStackTrace)
        )
    }

    private fun setBanksData(banks: MutableList<BankAccountModel>) {
        if (customerName.isNotEmpty()) {
            val vaBankINA = BankAccountModel()
            vaBankINA.id = BANK_VA_ID
            vaBankINA.bankName = "Bank INA"
            vaBankINA.accountNumber = "VA " + SessionManager.getPhone()
            vaBankINA.accountOwner = customerName
            vaBankINA.resLogo = R.drawable.logo_otto_cash

            banks.add(vaBankINA)
        }

        if (banks.size > 0) tvEmpty.gone() else {
            tvEmpty.visible()
            return
        }

        adapter.banks = banks
    }

    private fun onBankSelected(bank: BankAccountModel) {
        selectedBank = bank
    }

    fun onDeleteBank(bank: BankAccountModel) {
        selectedDeleteBank = bank
        deleteConfirmationDialog.show(supportFragmentManager, deleteConfirmationDialog.getTag())
    }

    fun onDeletePositiveButton() {
        deleteConfirmationDialog.dismiss()
        ProgressDialogComponent.showProgressDialog(this, getString(R.string.loading_message), false).show()

        val addBankDepositRequest = AddBankDepositRequest()
        selectedDeleteBank?.let {
            addBankDepositRequest.account_number = it.accountNumber
            addBankDepositRequest.bank_name = it.bankName
            addBankDepositRequest.bank_code = it.bankCode
        }

        TransferDepositDao(this).deleteBankDeposit(addBankDepositRequest, BaseDao.getInstance(this, API_KEY_DELETE_BANK_DEPOSIT).callback)
    }

    fun onDeleteNegativeButton() {
        deleteConfirmationDialog.dismiss()
    }

    override fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.clear()
        }
        super.onDestroy()
    }
}
