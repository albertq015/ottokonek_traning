package com.otto.mart.ui.fragment.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.otto.mart.GLOBAL
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.olshop.Category
import com.otto.mart.model.localmodel.ppob.PpobMenu
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.pref.Pref
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.ppob.PpobMenuSearchAdapter
import com.otto.mart.ui.activity.ppob.setup.PpobMenuSetup
import com.otto.mart.ui.component.dialog.Popup
import com.otto.mart.ui.fragment.AppFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_all_search.*
import java.util.concurrent.Callable

class AllSearchFragment : AppFragment() {

    val ppobResults = mutableListOf<PpobMenu>()
    val financeResults = mutableListOf<PpobMenu>()
    val tokoOnlineResults = mutableListOf<PpobMenu>()

    lateinit var disposable: CompositeDisposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        disposable = CompositeDisposable()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerview()
        search("")
    }

    private fun initRecyclerview() {
        rvPpob.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvPpob.setLayoutManager(linearLayoutManager)

        rvFinance.setHasFixedSize(true)
        val financeLLM = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvFinance.setLayoutManager(financeLLM)

        rvTokoOnline.setHasFixedSize(true)
        val tokoOnlineLLM = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvTokoOnline.setLayoutManager(tokoOnlineLLM)
    }

    private fun displayDefaultMenuPpob() {
        activity?.let {
            ppobResults.clear()

            for (allPpobMenu in PpobMenuSetup(it).getAllPpobMenu()) {
                ppobResults.add(allPpobMenu)
            }

            displayResult(ppobResults, rvPpob)
        }
    }

    private fun displayDefaultMenuFinancial() {
        activity?.let {
            financeResults.clear()

            for (allPpobMenu in PpobMenuSetup(it).getPpobFinanceMenu()) {
                financeResults.add(allPpobMenu)
            }

            displayResult(financeResults, rvFinance)
        }
    }

    private fun displayDefaultMenuTokoOnline() {
        activity?.let {
            tokoOnlineResults.clear()

            if (Pref.getPreference().getString(GLOBAL.CATEGORIES) != null && Pref.getPreference().getString(GLOBAL.CATEGORIES).isNotEmpty()) {
                for (menu in PpobMenuSetup(it).getOlshopMenuForHome()) {
                    tokoOnlineResults.add(menu)
                }
            }

            displayResult(tokoOnlineResults, rvTokoOnline)
        }
    }

    fun search(keyWord: String) {
        activity?.let {
            ppobResults.clear()
            for (menu in PpobMenuSetup(it).getAllPpobMenu()) {
                if (menu.title.toString().contains(keyWord, true)) {
                    ppobResults.add(menu)
                }
            }
        }
        displayResult(ppobResults, rvPpob)

        activity?.let {
            financeResults.clear()
            for (menu in PpobMenuSetup(it).getPpobFinanceMenu()) {
                if (menu.title.toString().contains(keyWord, true)) {
                    financeResults.add(menu)
                }
            }
        }
        displayResult(financeResults, rvFinance)

        activity?.let {
            tokoOnlineResults.clear()
            if (Pref.getPreference().getString(GLOBAL.CATEGORIES) != null && Pref.getPreference().getString(GLOBAL.CATEGORIES).isNotEmpty()) {
                for (menu in PpobMenuSetup(it).getOlshopMenuForHome()) {
                    if (menu.title.toString().contains(keyWord, true)) {
                        tokoOnlineResults.add(menu)
                    }
                }
            }
        }
        displayResult(tokoOnlineResults, rvTokoOnline)

        if (keyWord.equals("")) {
            displayDefaultMenuPpob()
            displayDefaultMenuFinancial()
            displayDefaultMenuTokoOnline()
        }
    }

    private fun displayResult(results: MutableList<PpobMenu>, recyclerView: RecyclerView) {
        if (results.size > 0) {
            activity?.let {
                val adapter = PpobMenuSearchAdapter(it, results)
                recyclerView.setAdapter(adapter)

                adapter.setmOnViewClickListener(object : PpobMenuSearchAdapter.OnViewClickListener {
                    override fun onViewClickListener(item: PpobMenu, position: Int) {
                        if (item.intent != null) {
//                            if (recyclerView.id == R.id.rvTokoOnline) {
//                                selectOlShopCategory(item.intent)
//                            } else {
//                                if (recyclerView.id == R.id.rvFinance) {
//                                    comingSoonDialog()
//                                } else {
//                                    startActivity(item.intent)
//                                }
                        }
//                        } else {
                        comingSoonDialog()
//                        }
                    }
                })
            }
        }

        if (ppobResults.size > 0 || financeResults.size > 0 || tokoOnlineResults.size > 0) {
            tvTitlePpob.gone()
            tvTitleFinance.gone()
            tvTitleTokoOnline.gone()

            if (ppobResults.size > 0) {
                tvTitlePpob.visible()
            }

            if (financeResults.size > 0) {
                tvTitleFinance.visible()
            }

            if (tokoOnlineResults.size > 0) {
                tvTitleTokoOnline.visible()
            }

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
            if (Pref.getPreference().getString(GLOBAL.CATEGORIES) != null) {
                disposable.add(
                        Observable.fromCallable(Callable { Gson().fromJson<List<Category?>>(Pref.getPreference().getString(GLOBAL.CATEGORIES), object : TypeToken<List<Category?>?>() {}.type) } as Callable<List<Category?>>)
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