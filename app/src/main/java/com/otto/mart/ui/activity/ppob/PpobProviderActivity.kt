package com.otto.mart.ui.activity.ppob

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import app.beelabs.com.codebase.base.BaseDao
import app.beelabs.com.codebase.base.response.BaseResponse
import com.google.gson.Gson
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Misc.OttoagDenomModel
import com.otto.mart.model.APIModel.Request.PpobOttoagDenomRequestModel
import com.otto.mart.model.APIModel.Response.BaseModel.BaseResponseModel
import com.otto.mart.model.APIModel.Response.PpobOttoagDenomResponseModel
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.presenter.dao.PpobDao
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.ppob.PpobProviderAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.ppob.product.bpjs.PpobBpjsProductInputActivity
import com.otto.mart.ui.activity.ppob.product.donasi.PpobDonasiProductInputActivity
import com.otto.mart.ui.activity.ppob.product.hiburan.PpobHiburanDenomActivity
import com.otto.mart.ui.activity.ppob.product.ppobWithProvider.PpobWithProviderProductInputActivity
import com.otto.mart.ui.activity.ppob.product.topUp.PpobTopUpProductInputActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_ppob_denom.recyclerview
import kotlinx.android.synthetic.main.content_ppob_favorite_list.emptyLayout
import kotlinx.android.synthetic.main.content_ppob_favorite_list.viewAnimator
import kotlinx.android.synthetic.main.content_ppob_provider.etSearch
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Response

class PpobProviderActivity : AppActivity() {

    val TAG = this.javaClass.simpleName

    val KEY_PPOB_PRODUCT_LIST = 100
    val KEY_PPOB_DENOM_LIST_OLD = 200

    private var mSearchStatus: Boolean = false
    private var mIsFromInputForm: Boolean = false

    private var mProviderList = mutableListOf<OttoagDenomModel>()

    private val disposable = CompositeDisposable()

    private var mPpobProductType: PpobProductType = PpobProductType()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppob_provider)

        if (intent.hasExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA)) {
            mPpobProductType = intent.getParcelableExtra<Parcelable>(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA) as PpobProductType
        }

        if (intent.hasExtra(AppKeys.KEY_PPOB_CATEGORY_PICKER_SEARCH_STATUS)) {
            mSearchStatus = intent.getBooleanExtra(AppKeys.KEY_PPOB_CATEGORY_PICKER_SEARCH_STATUS, false)
        }

        if (intent.hasExtra(AppKeys.KEY_PPOB_IS_FROM_INPUT_FORM)) {
            mIsFromInputForm = intent.getBooleanExtra(AppKeys.KEY_PPOB_IS_FROM_INPUT_FORM, false)
        }

        initView()
        initRecyclerView()

        if(mPpobProductType.isOldApi == true) {
            getDenomListOld()
        } else {
            var productCode = mPpobProductType.code?.toUpperCase().toString()

            if (mPpobProductType.code == AppKeys.PPOB_TYPE_TOP_UP) {
                //ISISALDO is OCBI Code for Top Up
                productCode = "ISISALDO"
            }

            callBillerProductListApi(productCode)
        }
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_ppob_provider)

        when (mPpobProductType.code) {
            AppKeys.PPOB_TYPE_TOP_UP -> tvToolbarTitle.text = getString(R.string.ppob_top_up_select_wallet_title)
            AppKeys.PPOB_TYPE_BPJS -> tvToolbarTitle.text = getString(R.string.ppob_top_up_select_product_title)
        }

        if (mSearchStatus) {
            etSearch.visible()
        }

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
                    displayProvider(mProviderList)
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

        var searchList = mutableListOf<OttoagDenomModel>()

        for (denom in mProviderList) {
            if (denom.product_name.contains(it, true)) {
                searchList.add(denom)
            }
        }

        if (searchList.size > 0) {
            displayProvider(searchList)
        } else {
            dataNotAvailable()
        }
    }

    private fun displayProvider(providerrList: MutableList<OttoagDenomModel>){
        if(mProviderList.size > 0) {
            val adapter = PpobProviderAdapter(this, providerrList)
            recyclerview.adapter = adapter

            adapter.setmOnViewClickListener(object : PpobProviderAdapter.OnViewClickListener {
                override fun onViewClickListener(provider: OttoagDenomModel, position: Int) {
                    providerSelected(provider, position)
                }
            })

            recyclerview.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
            viewAnimator.displayedChild = 1
        } else {
            dataNotAvailable()
        }
    }

    private fun providerSelected(provider: OttoagDenomModel, position: Int) {

        if (mIsFromInputForm) {
            val intent = Intent()
            intent.putExtra(AppKeys.KEY_PPOB_PROVIDER_DATA, Gson().toJson(provider))
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            when {
                mPpobProductType.code.equals(AppKeys.PPOB_TYPE_VOUCHER) -> {
                    val intent = Intent(Intent(this, PpobHiburanDenomActivity::class.java))
                    intent.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, mPpobProductType)
                    intent.putExtra(AppKeys.KEY_PPOB_PROVIDER_DATA, Gson().toJson(provider))
                    startActivity(intent)
                }
                mPpobProductType.code.equals(AppKeys.PPOB_TYPE_DONASI) -> {
                    val intent = Intent(Intent(this, PpobDonasiProductInputActivity::class.java))
                    intent.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, mPpobProductType)
                    intent.putExtra(AppKeys.KEY_PPOB_PROVIDER_DATA, Gson().toJson(provider))
                    startActivity(intent)
                }
                mPpobProductType.code.equals(AppKeys.PPOB_TYPE_TOP_UP) -> {
                    val intent = Intent(Intent(this, PpobTopUpProductInputActivity::class.java))
                    intent.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, mPpobProductType)
                    intent.putExtra(AppKeys.KEY_PPOB_PROVIDER_DATA, Gson().toJson(provider))
                    startActivity(intent)
                }
                mPpobProductType.code.equals(AppKeys.PPOB_TYPE_BPJS) -> {
                    val intent = Intent(Intent(this, PpobBpjsProductInputActivity::class.java))
                    intent.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, mPpobProductType)
                    intent.putExtra(AppKeys.KEY_PPOB_PROVIDER_DATA, Gson().toJson(provider))
                    startActivity(intent)
                }
                else -> {
                    val intent = Intent(Intent(this, PpobWithProviderProductInputActivity::class.java))
                    intent.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, mPpobProductType)
                    intent.putExtra(AppKeys.KEY_PPOB_PROVIDER_DATA, Gson().toJson(provider))
                    startActivity(intent)
                }
            }
        }
    }

    private fun dataNotAvailable() {
        recyclerview.gone()
        emptyLayout.visible()
        viewAnimator.displayedChild = 1
    }


    //region API Call

    private fun getDenomListOld() {
        var ppobOttoagDenomRequestModel = PpobOttoagDenomRequestModel()
        ppobOttoagDenomRequestModel.prefix = ""
        ppobOttoagDenomRequestModel.keyword = ""

        PpobDao(this).ppobDenomListOld(mPpobProductType.code, ppobOttoagDenomRequestModel, BaseDao.getInstance(this, KEY_PPOB_DENOM_LIST_OLD).callback)
    }

    private fun callBillerProductListApi(productName: String) {
        if (mPpobProductType.code.equals( AppKeys.PPOB_TYPE_VOUCHER)) {
            PpobDao(this).billerProductListCashback(mPpobProductType.code, "", "", "", BaseDao.getInstance(this, KEY_PPOB_PRODUCT_LIST).callback)
        } else {
            PpobDao(this@PpobProviderActivity).billerProductList(productName, "", "",
                    BaseDao.getInstance(this@PpobProviderActivity, KEY_PPOB_PRODUCT_LIST).callback)
        }
    }

    override fun onApiResponseCallback(br: BaseResponse?, responseCode: Int, response: Response<*>?) {
        super.onApiResponseCallback(br, responseCode, response)
        if (responseCode == KEY_PPOB_DENOM_LIST_OLD) {
            if ((br as BaseResponseModel).meta.code == 200) {
                mProviderList = (br as PpobOttoagDenomResponseModel).data.denomination
                displayProvider(mProviderList)
            } else {
                dataNotAvailable()
            }
        } else if (responseCode == KEY_PPOB_PRODUCT_LIST) {
            if ((br as BaseResponseModel).meta.code == 200) {
                mProviderList = (br as PpobOttoagDenomResponseModel).data.denomination
                displayProvider(mProviderList)
            } else {
                dataNotAvailable()
            }
        }
    }

    override fun onApiResponseError() {
        onApiResponseError()
    }

    //endregion API Call
}