package com.otto.mart.ui.activity.dashboard

import android.os.Bundle
import android.util.Log
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.otto.mart.R
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.FragmentPagerAdapter
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.fragment.search.AllSearchFragment
import com.otto.mart.ui.fragment.search.PpobSearchFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_search.*
import kotlinx.android.synthetic.main.toolbar_search.btnToolbarBack
import kotlinx.android.synthetic.main.toolbar_search_4.*

class SearchActivity : AppActivity() {

    companion object {
        val TYPE_PPOB = "ppob"
        val TYPE_FINANCIAL = "financial"
        val TYPE_TOKO_ONLINE = "toko_online"
    }

    val TAG = this.javaClass.simpleName

    private val disposable = CompositeDisposable()

    val allSearchFragment = AllSearchFragment()
    val ppobSearchFragment = PpobSearchFragment()
    val financialSearchFragment = PpobSearchFragment()
    val tokoonlineSearchFragment = PpobSearchFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initView()
        initContent()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    private fun initView() {
        btnToolbarBack.setOnClickListener {
            finish()
        }

        ivClose.setOnClickListener {
            etSearch.setText("")
        }

        disposable.add(
                etSearch.textChangeEvents()
                        .skipInitialValue()
                        .debounce(500, java.util.concurrent.TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(searchQuery()))
    }

    private fun searchQuery(): DisposableObserver<TextViewTextChangeEvent> {
        return object : DisposableObserver<TextViewTextChangeEvent>() {
            override fun onNext(textViewTextChangeEvent: TextViewTextChangeEvent) {
                val keyWord = textViewTextChangeEvent.text.toString()
                Log.d(TAG, "Keyword: " + keyWord)

                if (keyWord.equals("")) {
                    ivClose.gone()
                } else {
                    ivClose.visible()
                }
                allSearchFragment.search(keyWord)
                ppobSearchFragment.search(keyWord)
                financialSearchFragment.search(keyWord)
                tokoonlineSearchFragment.search(keyWord)
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }
    }

    private fun initContent() {
        val adapter = FragmentPagerAdapter(supportFragmentManager)

        ppobSearchFragment.type = TYPE_PPOB
        financialSearchFragment.type = TYPE_FINANCIAL
        tokoonlineSearchFragment.type = TYPE_TOKO_ONLINE

        adapter.addFragment(allSearchFragment, getString(R.string.search_label_tab_all))
        adapter.addFragment(ppobSearchFragment, getString(R.string.search_label_tab_ppob))
//        adapter.addFragment(financialSearchFragment, getString(R.string.search_label_tab_financial))
//        adapter.addFragment(tokoonlineSearchFragment, getString(R.string.search_label_tab_tokoonline))

        viewPager.setAdapter(adapter)
        viewPager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(viewPager)

        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }
}
