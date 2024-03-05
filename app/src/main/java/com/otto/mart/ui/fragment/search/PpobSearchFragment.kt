package com.otto.mart.ui.fragment.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.otto.mart.GLOBAL.CATEGORIES
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.olshop.Category
import com.otto.mart.model.localmodel.ppob.PpobMenu
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.pref.Pref
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.ppob.PpobMenuSearchAdapter
import com.otto.mart.ui.activity.dashboard.SearchActivity
import com.otto.mart.ui.activity.ppob.setup.PpobMenuSetup
import com.otto.mart.ui.component.dialog.Popup
import com.otto.mart.ui.fragment.AppFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_omzet.emptyLayout
import kotlinx.android.synthetic.main.content_omzet.recyclerview
import kotlinx.android.synthetic.main.fragment_ppob_search.contentLayout
import java.util.concurrent.Callable

class PpobSearchFragment : AppFragment() {

    var type: String? = ""
    val results = mutableListOf<PpobMenu>()

    lateinit var disposable: CompositeDisposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        disposable = CompositeDisposable()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ppob_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerview()
        search("")
    }

    private fun initRecyclerview() {
        recyclerview.setHasFixedSize(true)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        recyclerview.setLayoutManager(linearLayoutManager)
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    private fun displayAllMenu() {
        when (type) {
            SearchActivity.TYPE_PPOB -> {
                displayDefaultMenuPpob()
            }
            SearchActivity.TYPE_FINANCIAL -> {
                displayDefaultMenuFinancial()
            }
            SearchActivity.TYPE_TOKO_ONLINE -> {
                displayDefaultMenuTokoOnline()
            }
        }
    }

    private fun displayDefaultMenuPpob() {
        activity?.let {
            results.clear()

            for (allPpobMenu in PpobMenuSetup(it).getAllPpobMenu()) {
                results.add(allPpobMenu)
            }
            displayResult(results)
        }
    }

    private fun displayDefaultMenuFinancial() {
        activity?.let {
            results.clear()

            for (allPpobMenu in PpobMenuSetup(it).getPpobFinanceMenu()) {
                results.add(allPpobMenu)
            }
            displayResult(results)
        }
    }

    private fun displayDefaultMenuTokoOnline() {
        activity?.let {
            results.clear()
            if (Pref.getPreference().getString(CATEGORIES) != null && Pref.getPreference().getString(CATEGORIES).isNotEmpty()) {
                for (menu in PpobMenuSetup(it).getOlshopMenuForHome()) {
                    results.add(menu)
                }
            }
            displayResult(results)
        }
    }

    fun search(keyWord: String) {
        when (type) {
            SearchActivity.TYPE_PPOB -> {
                activity?.let {
                    results.clear()
                    for (menu in PpobMenuSetup(it).getAllPpobMenu()) {
                        if (menu.title.toString().contains(keyWord, true)) {
                            results.add(menu)
                        }
                    }
                }
                displayResult(results)
            }
            SearchActivity.TYPE_FINANCIAL -> {
                activity?.let {
                    results.clear()
                    for (menu in PpobMenuSetup(it).getPpobFinanceMenu()) {
                        if (menu.title.toString().contains(keyWord, true)) {
                            results.add(menu)
                        }
                    }
                }
                displayResult(results)
            }
            SearchActivity.TYPE_TOKO_ONLINE -> {
                activity?.let {
                    results.clear()
                    if (Pref.getPreference().getString(CATEGORIES) != null && Pref.getPreference().getString(CATEGORIES).isNotEmpty()) {
                        for (menu in PpobMenuSetup(it).getOlshopMenuForHome()) {
                            if (menu.title.toString().contains(keyWord, true)) {
                                results.add(menu)
                            }
                        }
                    }
                }
                displayResult(results)
            }
        }

        if (keyWord.equals("")) {
            displayAllMenu()
        }
    }

    private fun displayResult(results: MutableList<PpobMenu>) {
        if (results.size > 0) {
            activity?.let {
                val adapter = PpobMenuSearchAdapter(it, results)
                recyclerview.setAdapter(adapter)

                adapter.setmOnViewClickListener(object : PpobMenuSearchAdapter.OnViewClickListener {
                    override fun onViewClickListener(item: PpobMenu, position: Int) {
//                        if (item.intent != null) {
//                            if (type.equals(SearchActivity.TYPE_TOKO_ONLINE)) {
//                                selectOlShopCategory(item.intent)
//                            } else {
//                                if (type == SearchActivity.TYPE_FINANCIAL) {
//                                    comingSoonDialog()
//                                } else {
//                                    startActivity(item.intent)
//                                }
//                            }
//                        } else {
//                            comingSoonDialog()
//                        }
                        comingSoonDialog()
                    }
                })
            }
        }

        if (results.size > 0) {
            contentLayout.visible()
            emptyLayout.gone()
        } else {
            contentLayout.gone()
            emptyLayout.visible()
        }
    }

    private fun selectOlShopCategory(intent: Intent) {
        val menuId = intent.getIntExtra("menuId", -1)
        if (menuId != -1) {
            if (Pref.getPreference().getString(CATEGORIES) != null) {
                disposable.add(
                        Observable.fromCallable(Callable { Gson().fromJson<List<Category?>>(Pref.getPreference().getString(CATEGORIES), object : TypeToken<List<Category?>?>() {}.type) } as Callable<List<Category?>>)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe({ categories: List<Category?> ->
                                    gotoOlShop(categories, menuId, intent)
                                }) { obj: Throwable -> obj.printStackTrace() }
                )
            }
        }
    }

    private fun gotoOlShop(categories: List<Category?>, menuId: Int, intent: Intent) {
        for (category in categories) {
            if (category?.id == menuId) {
                intent.putExtra("category", Gson().toJson(categories))
                intent.putExtra("selectedCategory", Gson().toJson(category))
                startActivity(intent)
                return
            }
        }
    }

    private fun comingSoonDialog() {
        val dialog = Popup()
        dialog.isHideBtnNegative = true
        dialog.isHideBtnPositive = false
        dialog.positiveButton = getString(R.string.oke)
        dialog.title = getString(R.string.message_feature_coming_soon)
        dialog.show(childFragmentManager, "popupInfo")
    }
}