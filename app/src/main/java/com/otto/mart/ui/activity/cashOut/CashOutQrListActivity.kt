package com.otto.mart.ui.activity.cashOut

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.component.ProgressDialogComponent
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.cashOut.CashOutListResponse
import com.otto.mart.model.APIModel.Response.cashOut.CashOutQr
import com.otto.mart.presenter.dao.CashOutDao
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.cashOut.CashOutQrAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import kotlinx.android.synthetic.main.content_cash_out_qr_list.*
import kotlinx.android.synthetic.main.content_setting.recyclerview
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response


class CashOutQrListActivity : AppActivity() {

    val KEY_API_CASH_OUT_LIST = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_out_qr_list)

        initView()
        initRecyclerview()
        getQrList()
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_cash_out_qr_list)

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecyclerview() {
        recyclerview.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)

        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.cash_out_qr_list_devider)?.let {
            itemDecorator.setDrawable(it)
            recyclerview.addItemDecoration(itemDecorator)
        }
    }

    private fun displayQrList(cashOutQrList: List<CashOutQr>) {
        if(cashOutQrList.size > 0){
            val adapter = CashOutQrAdapter(this, cashOutQrList)
            recyclerview.adapter = adapter

            adapter.setmOnViewClickListener(object : CashOutQrAdapter.OnViewClickListener {
                override fun onViewClickListener(data: CashOutQr, position: Int) {
                    qrSelected(data, position)
                }
            })
        } else {
            contentLayout.gone()
            emptyLayout.visible()
        }

        viewAnimator.displayedChild = 1
    }

    private fun qrSelected(data: CashOutQr, position: Int) {
        val intent = Intent(this, CashOutQrDetailActivity::class.java)
        intent.putExtra(CashOutQrDetailActivity.KEY_CASH_OUT_DATA, data)
        startActivity(intent)
    }


    //region API Call

    private fun getQrList() {
        CashOutDao(this).cashOutList(BaseDao.getInstance(this, KEY_API_CASH_OUT_LIST).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        ProgressDialogComponent.dismissProgressDialog(this)
        validateApiResponse(br)
        if (response != null && response.isSuccessful) {
            when (responseCode) {
                KEY_API_CASH_OUT_LIST -> if ((br as BaseResponseModel).meta.code == 200) {
                    (br as CashOutListResponse).data?.qrcodes?.let {
                        displayQrList(it)
                    }
                } else {
                    val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                    dialog.show()
                }
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        onApiResponseError()
    }

    //endregion API Call
}