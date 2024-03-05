package com.otto.mart.ui.activity.ppob

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.localmodel.ppob.PpobProductType
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.ppob.PpobProductTypeAdapter
import com.otto.mart.ui.activity.PpobActivity
import com.otto.mart.ui.activity.ppob.product.paketData.PpobPaketDataProductInputActivity
import com.otto.mart.ui.activity.ppob.setup.PpobProductTypeSetup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_ppob_product_type.*
import kotlinx.android.synthetic.main.content_ppob_product_type.recyclerview
import kotlinx.android.synthetic.main.toolbar.btnToolbarBack
import kotlinx.android.synthetic.main.toolbar.tvToolbarTitle
import kotlinx.android.synthetic.main.toolbar_ppob.*

class PpobProductTypeActivity : PpobActivity() {

    val TAG = javaClass.simpleName

    val mProductName = "Telkom"

    private val disposable = CompositeDisposable()

    var mProductList = arrayListOf<PpobProductType>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppob_product_type)

        initView()
        initRecyclerView()
        displayAllProduct()
    }

    private fun initView() {
        tvToolbarTitle.text = mProductName

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        btnToolbarTitle.setOnClickListener {
            showAllPpobMenu(mProductName)
        }

        etSearch.visible()

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
                    displayProduct(mProductList)
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

    private fun filterCategory(keyword: String) {

        var searchList = mutableListOf<PpobProductType>()

        for (product in mProductList) {
            product.name?.let {
                if (it.contains(keyword, true)) {
                    searchList.add(product)
                }
            }
        }

        if (searchList.size > 0) {
            displayProduct(searchList)
        } else {
            dataNotAvailable()
        }
    }

    private fun displayAllProduct() {
        mProductList.add(PpobProductTypeSetup().getProductTelkom())
        mProductList.add(PpobProductTypeSetup().getProductTelkomInternet())

        displayProduct(mProductList)
    }

    private fun displayProduct(productList: List<PpobProductType>) {
        val adapter = PpobProductTypeAdapter(this, productList)
        recyclerview.adapter = adapter

        adapter.setmOnViewClickListener(object : PpobProductTypeAdapter.OnViewClickListener {
            override fun onViewClickListener(productType: PpobProductType, position: Int) {
                val intent = Intent(this@PpobProductTypeActivity, PpobPaketDataProductInputActivity::class.java)
                intent.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, productType)
                startActivity(intent)
            }
        })

        recyclerview.visible()
        emptyLayout.gone()
        viewAnimator.displayedChild = 1
    }

    private fun dataNotAvailable() {
        recyclerview.gone()
        emptyLayout.visible()
        viewAnimator.displayedChild = 1
    }

}