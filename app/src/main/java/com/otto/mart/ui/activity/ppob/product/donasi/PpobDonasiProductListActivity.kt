package com.otto.mart.ui.activity.ppob.product.donasi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import com.google.gson.Gson
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.donasi.ProductDonasiResponse
import com.otto.mart.presenter.dao.DonasiDao
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.ppob.PpobDonasiProductAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.component.dialog.ErrorDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_ppob_denom.recyclerview
import kotlinx.android.synthetic.main.content_ppob_donasi_product_list.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class PpobDonasiProductListActivity : AppActivity() {

    val TAG = this.javaClass.simpleName

    private val KEY_PRODUCT_NAME = "DONASI"

    private val disposable = CompositeDisposable()

    private var mProviderList = mutableListOf<ProductDonasiResponse.Data.Denomination>()
    private var mIsFromInputForm: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppob_donasi_product_list)

        if (intent.hasExtra(AppKeys.KEY_PPOB_IS_FROM_INPUT_FORM)) {
            mIsFromInputForm = intent.getBooleanExtra(AppKeys.KEY_PPOB_IS_FROM_INPUT_FORM, false)
        }

        initView()
        initRecyclerView()
        getProductList()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear();
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_ppob_donasi_product_list)

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        disposable.add(
                etSearch.textChangeEvents()
                        .skipInitialValue()
                        .debounce(500, java.util.concurrent.TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(searchQuery()))
    }

    private fun initRecyclerView() {
        recyclerview.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        recyclerview.setLayoutManager(linearLayoutManager)
    }

    private fun searchQuery(): DisposableObserver<TextViewTextChangeEvent> {
        return object : DisposableObserver<TextViewTextChangeEvent>() {
            override fun onNext(textViewTextChangeEvent: TextViewTextChangeEvent) {
                val keyWord = textViewTextChangeEvent.text.toString()
                Log.d(TAG, "Keyword: " + keyWord)

                if (keyWord.equals("")) {
                    displayProductList(mProviderList)
                } else {
                    filterCategory(keyWord)
                }
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }
    }

    private fun filterCategory(it: String) {

        var searchList = mutableListOf<ProductDonasiResponse.Data.Denomination>()

        for (denom in mProviderList) {
            if (denom.operator.contains(it, true)) {
                searchList.add(denom)
            }
        }

        if (searchList.size > 0) {
            displayProductList(searchList)
        } else {
            dataNotAvailable()
        }
    }

    private fun displayProductList(denomination: List<ProductDonasiResponse.Data.Denomination>) {
        val adapter = PpobDonasiProductAdapter(this, denomination)
        recyclerview.adapter = adapter

        adapter.setmOnViewClickListener(object: PpobDonasiProductAdapter.OnViewClickListener {
            override fun onViewClickListener(item: ProductDonasiResponse.Data.Denomination, position: Int) {
                productSelected(item)
            }
        })

        recyclerview.visibility = View.VISIBLE
        emptyLayout.visibility = View.GONE
        viewAnimator.displayedChild = 1
    }

    private fun productSelected(item: ProductDonasiResponse.Data.Denomination) {
        if (mIsFromInputForm) {
            val intent = Intent()
            intent.putExtra(AppKeys.KEY_PPOB_DONASI_PRODUCT_DATA, Gson().toJson(item))
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            val intent = Intent(this, PpobDonasiProductInputActivity::class.java)
            intent.putExtra(AppKeys.KEY_PPOB_DONASI_PRODUCT_DATA, Gson().toJson(item))
            startActivity(intent)
            finish()
        }
    }

    private fun dataNotAvailable() {
        recyclerview.gone()
        emptyLayout.visible()
        viewAnimator.displayedChild = 1
    }


    //region API Call

    private fun getProductList() {
        DonasiDao(this).billerProductList(KEY_PRODUCT_NAME, BaseDao.getInstance(this, AppKeys.API_KEY_DONASI_PRODUCT_LIST).callback)
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        super.onApiResponseCallback(br, responseCode, response)
        when (responseCode) {
            AppKeys.API_KEY_DONASI_PRODUCT_LIST -> if ((br as BaseResponseModel).meta.code == 200) {
                mProviderList = (br as ProductDonasiResponse).data.denomination
                displayProductList(mProviderList)
            } else {
                val dialog = ErrorDialog(this, this, false, false, br.meta.message, "")
                dialog.setOnDismissListener {
                    finish()
                }
                dialog.show()
            }
        }
    }

    override fun onApiFailureCallback(message: String?, ac: BaseActivity?) {
        onApiResponseError()
    }

    //endregion API Call
}