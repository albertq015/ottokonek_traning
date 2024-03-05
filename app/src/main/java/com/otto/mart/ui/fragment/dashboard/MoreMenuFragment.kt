package com.otto.mart.ui.fragment.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.otto.mart.BuildConfig
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.localmodel.ppob.PpobMenu
import com.otto.mart.model.localmodel.ui.DashboardIconModel
import com.otto.mart.ui.Partials.adapter.ppob.PpobMenuAdapter
import com.otto.mart.ui.activity.dashboard.DashboardActivity
import com.otto.mart.ui.activity.ppob.product.donasi.PpobDonasiProductInputActivity
import com.otto.mart.ui.activity.ppob.product.ppobWithProvider.PpobWithProviderProductInputActivity
import com.otto.mart.ui.activity.ppob.product.topUp.PpobTopUpProductInputActivity
import com.otto.mart.ui.activity.ppob.setup.PpobMenuSetup
import com.otto.mart.ui.activity.ppob.setup.PpobProductTypeSetup
import com.otto.mart.ui.component.dialog.Popup
import kotlinx.android.synthetic.main.fragment_more_menu.*
import java.util.*

/**O
 * A simple [Fragment] subclass.
 */
class MoreMenuFragment : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_more_menu, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerview()
        displayMoreMenu()
    }

    private fun initRecyclerview() {
        recyclerview.setHasFixedSize(false)
        val glmPpobMenu = GridLayoutManager(activity, 4)
        recyclerview.setLayoutManager(glmPpobMenu)
    }

    private fun displayMoreMenu() {

        activity?.let {
            val ppobMenuAdapter = PpobMenuAdapter(it, PpobMenuSetup(it).getPpobMenuForMoreMenu())
            recyclerview.adapter = ppobMenuAdapter

            ppobMenuAdapter.setmOnViewClickListener(object : PpobMenuAdapter.OnViewClickListener {
                override fun onViewClickListener(item: PpobMenu, position: Int) {
                    comingSoonDialog()
                    try {
                        (activity as DashboardActivity).hideMoreMenuFragment()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
//                    if (item.intent != null) {
//                        startActivity(item.intent)
//                        try {
//                            (activity as DashboardActivity).hideMoreMenuFragment()
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//                    } else {
//                        val dialog = ErrorDialog(it, it, false, false, getString(R.string.dialog_msg_coming_soon), "")
//                        dialog.show()
//                    }
                }
            })
        }

        val shopcontent: MutableList<DashboardIconModel> = ArrayList()

        val intentPpobCicilan = Intent(activity, PpobWithProviderProductInputActivity::class.java)
        intentPpobCicilan.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductCicilan())

        val intentPpobPendidikan = Intent(activity, PpobWithProviderProductInputActivity::class.java)
        intentPpobPendidikan.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductPendidikan())

        val intentPpobTopUp = Intent(activity, PpobTopUpProductInputActivity::class.java)
        intentPpobTopUp.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductTopUp())

        val intentPpobInternet = Intent(activity, PpobWithProviderProductInputActivity::class.java)
        intentPpobInternet.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductInternet())

        val intentPpobAsurance = Intent(activity, PpobWithProviderProductInputActivity::class.java)
        intentPpobAsurance.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductAsuransi())

        val intentPpobDonation = Intent(activity, PpobDonasiProductInputActivity::class.java)
        intentPpobDonation.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductDonasi())

        val intentPpobAir = Intent(activity, PpobWithProviderProductInputActivity::class.java)
        intentPpobAir.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductAir())

        shopcontent.add(DashboardIconModel("Isi Saldo", intentPpobTopUp, R.drawable.icon_button_topupemoney, null, 6))
        shopcontent.add(DashboardIconModel("Tagihan Air", intentPpobAir, R.drawable.icon_button_tagihanair, null, 5))
        shopcontent.add(DashboardIconModel("Internet", intentPpobInternet, R.drawable.icon_button_cbn, null, 7))
        shopcontent.add(DashboardIconModel("Asuransi", intentPpobAsurance, R.drawable.icon_button_asuransi, null, 8))
        shopcontent.add(DashboardIconModel("Donasi", intentPpobDonation, R.drawable.icon_button_donasi, null, 9))

        if (BuildConfig.FLAVOR == AppKeys.FLAVOR_DEVELOPMENT) {
            shopcontent.add(DashboardIconModel("Cicilan", intentPpobCicilan, R.drawable.icon_button_cicilan, null, 10))
        }

        shopcontent.add(DashboardIconModel("Pendidikan", intentPpobPendidikan, R.drawable.icon_button_education, null, 11))
    }

    private fun comingSoonDialog() {
        activity?.let { activity ->
            Popup().apply {
                isHideBtnNegative = true
                isHideBtnPositive = false
                positiveButton = activity.getString(R.string.button_ok)
                title = activity.getString(R.string.message_feature_coming_soon)
            }.show(activity.supportFragmentManager, "popupInfo")
        }
    }
}