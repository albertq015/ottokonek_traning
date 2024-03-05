package com.otto.mart.ui.activity.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.model.APIModel.Request.BankEditRequestModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.localmodel.ui.BankUiModel
import com.otto.mart.presenter.dao.ProfileDao
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.register.RegisterAddRekeningActivity
import kotlinx.android.synthetic.main.activity_bank_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BankDetailActivity : AppActivity() {
    companion object {
        const val accountIntentData = "accountIntentData"
        const val listPos = "listPos"
    }

    private val accountStatusRejected = "ditolak"
    private val accountStatusApproved = "disetujui"
    private var bankData: BankUiModel? = null
    private var listPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_detail)
        getIntentValue()
        initView()
    }

    override fun onStart() {
        super.onStart()
        setValue()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 888 && resultCode == RESULT_OK && data != null) {
            val id = data.getIntExtra("id", -1)
            if (id != -1) {
                callUpdateBankAccountAPI(data, id)
            }
        }
    }

    private fun getIntentValue() {
        bankData = Gson().fromJson(intent.getStringExtra(accountIntentData), BankUiModel::class.java)
        listPosition = intent.getIntExtra(listPos, -1)
        tvToolbarTitle.text = getString(R.string.text_account)
    }

    private fun initView() {
        btnToolbarBack.setOnClickListener { finish() }
        tvAccountStatus.setOnClickListener { showStatusReason() }
        btnEdit.setOnClickListener {
            bankData?.let { data ->
                if (!data.requestModel.approval_status.equals(accountStatusRejected, true) &&
                        !data.requestModel.approval_status.equals(accountStatusApproved, true)) {
                    showErrorDialog("Sedang dalam verifikasi")
                } else openBankAccountEditPage()
            }
        }
    }

    private fun showStatusReason() {
        bankData?.let { data ->
            if (data.requestModel.approval_status.equals(accountStatusRejected, true) && data.requestModel.reason.isNotEmpty())
                showErrorDialog(data.requestModel.reason)
        }
    }

    private fun openBankAccountEditPage() {
        bankData?.let { data ->
            val intent = Intent(this, RegisterAddRekeningActivity::class.java)
            intent.putExtra("listmodel", data.listModel)
            intent.putExtra("requestmodel", data.requestModel)
            intent.putExtra("position", listPosition)
            intent.putExtra("paymentposition", 1)
            intent.putExtra("bankposition", data.selectedBankPos)
            intent.putExtra("norek", data.requestModel.account_number)
            intent.putExtra("name", data.requestModel.account_name)
            intent.putExtra("bankName", data.listModel.name)
            intent.putExtra("isEdit", true)
            intent.putExtra("bankId", data.listModel.id)
            intent.putExtra("bank_code", data.listModel.code)
            intent.putExtra("bank_logo", data.listModel.logo)
            intent.putExtra("approval_status", data.requestModel.approval_status)

            startActivityForResult(intent, 888)
        }
    }

    private fun setValue() {
        bankData?.let { data ->
            tvBankName.text = data.listModel.name
            tvAccountName.text = data.requestModel.account_name
            tvAccountNumber.text = data.requestModel.account_number
            Glide.with(this).load(data.listModel.logo).into(ivBankLogo)

            tvAccountStatus.text = data.requestModel.approval_status
            setStatusAppearance(data.requestModel.approval_status)
        }
    }

    private fun setStatusAppearance(approvalStatus: String) {
        when (approvalStatus.toLowerCase()) {
            "ditolak" -> setTextAppearance(R.color.faded_red, R.drawable.bg_red_bordered)
            "disetujui" -> setTextAppearance(R.color.dark_sky_blue, R.drawable.bg_blue_bordered)
            else -> setTextAppearance(R.color.brown_grey_three, R.drawable.bg_grey_bordered)
        }
    }

    private fun setTextAppearance(textColor: Int, textBackground: Int) {
        tvAccountStatus.setTextColor(textColor)
        tvAccountStatus.background = ContextCompat.getDrawable(this, textBackground)
    }

    private fun callUpdateBankAccountAPI(data: Intent, id: Int) {
        val model = BankEditRequestModel()
        model.account_number = data.getStringExtra("accountNum")
        model.account_owner = data.getStringExtra("accountName")
        model.bank_id = data.getIntExtra("bankid", -1)
        model.isSet_as_main = data.getBooleanExtra("setMain", false)
        model.bank_account_id = id

        ProfileDao(this).updateBank(model, object : Callback<BaseResponseModel> {
            override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                onApiResponseError()
            }

            override fun onResponse(call: Call<BaseResponseModel>, response: Response<BaseResponseModel>) {
                handleUpdateResponse(response.body())
            }

        })
    }

    private fun handleUpdateResponse(response: BaseResponseModel?) {
        response?.let {
            if (it.meta.code == 200) setResult(RESULT_OK, getIntentResult(true))
            else setResult(Activity.RESULT_OK, getIntentResult(false))
        }
    }

    private fun getIntentResult(isBankAccountUpdated: Boolean): Intent {
        return Intent().apply { putExtra("isUpdated", isBankAccountUpdated) }
    }
}
