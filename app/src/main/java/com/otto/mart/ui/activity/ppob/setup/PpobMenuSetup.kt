package com.otto.mart.ui.activity.ppob.setup

import android.content.Context
import android.content.Intent
import com.gaelmarhic.quadrant.QuadrantConstants
import com.otto.mart.R
import com.otto.mart.keys.AppKeys
import com.otto.mart.model.APIModel.Response.merchant.featureProduct.FeatureProduct
import com.otto.mart.model.localmodel.ppob.PpobMenu
import com.otto.mart.presenter.sessionManager.SessionManager
import com.otto.mart.ui.activity.olshop.CatalogActivity
import com.otto.mart.ui.activity.ppob.PpobProductTypeActivity
import com.otto.mart.ui.activity.ppob.PpobProviderActivity
import com.otto.mart.ui.activity.ppob.product.donasi.PpobDonasiProductListActivity
import com.otto.mart.ui.activity.ppob.product.game.VoucherGameActivity
import com.otto.mart.ui.activity.ppob.product.listrik.PpobListrikProductInputActivity
import com.otto.mart.ui.activity.ppob.product.paketData.PpobPaketDataProductInputActivity
import com.otto.mart.ui.activity.ppob.product.pulsa.PpobPulsaProductInputActivity
import java.util.*


class PpobMenuSetup(context: Context) {

    val HOME_PRODUCT_DISPLAY_LIMIT = 7
    val KEY_STATUS_ACTIVE = "active"

    var mContext: Context? = null

    init {
        mContext = context
    }

    fun getPpobMenuForHome(): MutableList<PpobMenu> {
        val ppobMenuList: MutableList<PpobMenu> = ArrayList()

        if (SessionManager.getFeatureProduct() != null && SessionManager.getFeatureProduct().ppob != null &&
                SessionManager.getFeatureProduct().ppob.size > 0) {
            if (SessionManager.getFeatureProduct().ppob[0].feature_product != null &&
                    SessionManager.getFeatureProduct().ppob[0].feature_product.size > 0) {
                for (featureProduct in SessionManager.getFeatureProduct().ppob[0].feature_product) {
                    if (featureProduct.status.equals(KEY_STATUS_ACTIVE, true)) {
                        generatePpobMenu(featureProduct)?.let {
                            ppobMenuList.add(it)

                            if (ppobMenuList.size == HOME_PRODUCT_DISPLAY_LIMIT) {
                                ppobMenuList.add(getMoreMenu())
                                return ppobMenuList
                            }
                        }
                    }
                }
            }
        }
        return ppobMenuList
    }

    fun getPpobMenuForMoreMenu(): MutableList<PpobMenu> {
        val ppobMenuList: MutableList<PpobMenu> = ArrayList()

        if (SessionManager.getFeatureProduct() != null && SessionManager.getFeatureProduct().ppob != null &&
                SessionManager.getFeatureProduct().ppob.size > 0) {
            if (SessionManager.getFeatureProduct().ppob[0].feature_product != null &&
                    SessionManager.getFeatureProduct().ppob[0].feature_product.size > 0) {
                var i = 0
                for (featureProduct in SessionManager.getFeatureProduct().ppob[0].feature_product) {
                    if (featureProduct.status.equals(KEY_STATUS_ACTIVE, true)) {
                        generatePpobMenu(featureProduct)?.let {
                            if (i >= HOME_PRODUCT_DISPLAY_LIMIT) {
                                ppobMenuList.add(it)
                            }
                        }
                    }
                    i++;
                }
            }
        }
        return ppobMenuList
    }

    fun getAllPpobMenu(): MutableList<PpobMenu> {
        val ppobMenuList: MutableList<PpobMenu> = ArrayList()

        if (SessionManager.getFeatureProduct() != null && SessionManager.getFeatureProduct().ppob != null &&
                SessionManager.getFeatureProduct().ppob.size > 0) {
            if (SessionManager.getFeatureProduct().ppob[0].feature_product != null &&
                    SessionManager.getFeatureProduct().ppob[0].feature_product.size > 0) {
                for (featureProduct in SessionManager.getFeatureProduct().ppob[0].feature_product) {
                    if (featureProduct.status.equals(KEY_STATUS_ACTIVE, true)) {
                        generatePpobMenu(featureProduct)?.let {
                            ppobMenuList.add(it)
                        }
                    }
                }
            }
        }

        return ppobMenuList
    }

    fun getPpobFinanceMenu(): MutableList<PpobMenu> {
        val ppobMenuList: MutableList<PpobMenu> = ArrayList()

        if (SessionManager.getFeatureProduct() != null && SessionManager.getFeatureProduct().product_finansial != null &&
                SessionManager.getFeatureProduct().product_finansial.size > 0) {
            if (SessionManager.getFeatureProduct().product_finansial[0].feature_product != null &&
                    SessionManager.getFeatureProduct().product_finansial[0].feature_product.size > 0) {
                for (featureProduct in SessionManager.getFeatureProduct().product_finansial[0].feature_product) {
                    if (featureProduct.status.equals(KEY_STATUS_ACTIVE, true)) {
                        generatePpobMenu(featureProduct)?.let {
                            ppobMenuList.add(it)
                        }
                    }
                }
            }
        }

        return ppobMenuList
    }

    fun getOasisMenu(): MutableList<PpobMenu> {
        val ppobMenuList: MutableList<PpobMenu> = ArrayList()

        if (SessionManager.getFeatureProduct() != null && SessionManager.getFeatureProduct().toko_grosir != null &&
                SessionManager.getFeatureProduct().toko_grosir.size > 0) {
            if (SessionManager.getFeatureProduct().toko_grosir[0].feature_product != null &&
                    SessionManager.getFeatureProduct().toko_grosir[0].feature_product.size > 0) {
                for (featureProduct in SessionManager.getFeatureProduct().toko_grosir[0].feature_product) {
                    if (featureProduct.status.equals(KEY_STATUS_ACTIVE, true)) {
                        generatePpobMenu(featureProduct)?.let {
                            ppobMenuList.add(it)
                        }
                    }
                }
            }
        }

        return ppobMenuList
    }


    private fun generatePpobMenu(featureProduct: FeatureProduct?): PpobMenu? {
        var ppobMeenu: PpobMenu? = null

        when (featureProduct?.code?.toUpperCase()) {
            "PULSA" -> return getPulsa(featureProduct)
            "PAKETDATA" -> return getPaketData(featureProduct)
            "PLN" -> return getPLN(featureProduct)
            "GAME" -> return getGame(featureProduct)
            "VOUCHER" -> return getVoucher(featureProduct)
            "BPJS" -> return getBpjs(featureProduct)
            "TELKOM" -> return getTelkom(featureProduct)
            "ISISALDO" -> return getTopUp(featureProduct)
            "PDAM" -> return getAirPdam(featureProduct)
            "INTERNET" -> return getInternet(featureProduct)
            "ASURANSI" -> return getAsuransi(featureProduct)
            "DONASI" -> return getDonasi(featureProduct)
            "PENDIDIKAN" -> return getPendidikan(featureProduct)
            "CICILAN" -> return getCicilan(featureProduct)
            "KIRIM_UANG" -> return getKirimUang(featureProduct)
            "EMAS" -> return getEmas(featureProduct)
            "UMROH" -> return getUmroh(featureProduct)
            "PESANBRG" -> return getOasisPesanBarang(featureProduct)
            "RIWAYAT" -> return getOasisRiwayatPesanan(featureProduct)
            //"BAYARPESAN" -> return getOasisBayarPesanan(featureProduct)
        }

        return ppobMeenu
    }

    //PULSA
    private fun getPulsa(featureProduct: FeatureProduct): PpobMenu {
        return PpobMenu(mContext?.getString(R.string.ppob_product_name_pulsa), null,
                featureProduct.icon, Intent(mContext, PpobPulsaProductInputActivity::class.java))
    }

    //PAKET DATA
    private fun getPaketData(featureProduct: FeatureProduct): PpobMenu {
        val intentPaketData = Intent(mContext, PpobPaketDataProductInputActivity::class.java)
        intentPaketData.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductPaketData())

        return PpobMenu(mContext?.getString(R.string.ppob_product_name_paket_data), null,
                featureProduct.icon, intentPaketData)
    }

    //PLN
    private fun getPLN(featureProduct: FeatureProduct): PpobMenu {
        return PpobMenu(mContext?.getString(R.string.ppob_product_name_pln), null, featureProduct.icon,
                Intent(mContext, PpobListrikProductInputActivity::class.java))
    }

    //GAME
    private fun getGame(featureProduct: FeatureProduct): PpobMenu {
        return PpobMenu(mContext?.getString(R.string.ppob_product_name_game), null, featureProduct.icon,
                Intent(mContext, VoucherGameActivity::class.java))
    }

    //VOUCHER
    private fun getVoucher(featureProduct: FeatureProduct): PpobMenu {
        val intentPpobHiiburan = Intent(mContext, PpobProviderActivity::class.java)
        intentPpobHiiburan.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductVoucher())
        intentPpobHiiburan.putExtra(AppKeys.KEY_PPOB_CATEGORY_PICKER_SEARCH_STATUS, true)

        return PpobMenu(mContext?.getString(R.string.ppob_product_name_voucher), null, featureProduct.icon, intentPpobHiiburan)
    }

    //BPJS
    private fun getBpjs(featureProduct: FeatureProduct): PpobMenu {
        val intentPpobBpjs = Intent(mContext, PpobProviderActivity::class.java)
        intentPpobBpjs.putExtra(AppKeys.KEY_PPOB_CATEGORY_PICKER_SEARCH_STATUS, true)
        intentPpobBpjs.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductBpjs())

        return PpobMenu(mContext?.getString(R.string.ppob_product_name_bpjs), null, featureProduct.icon, intentPpobBpjs)
    }

    //TELKOM
    private fun getTelkom(featureProduct: FeatureProduct): PpobMenu {
        return PpobMenu(mContext?.getString(R.string.ppob_product_name_telkom), null, featureProduct.icon, Intent(mContext, PpobProductTypeActivity::class.java))
    }

    //TOP UP
    private fun getTopUp(featureProduct: FeatureProduct): PpobMenu {
        val intentPpobTopUp = Intent(mContext, PpobProviderActivity::class.java)
        intentPpobTopUp.putExtra(AppKeys.KEY_PPOB_CATEGORY_PICKER_SEARCH_STATUS, true)
        intentPpobTopUp.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductTopUp())

        return PpobMenu(mContext?.getString(R.string.ppob_product_name_top_up), null, featureProduct.icon, intentPpobTopUp)
    }

    //Air PDAM
    private fun getAirPdam(featureProduct: FeatureProduct): PpobMenu {
        val intentPpobAir = Intent(mContext, PpobProviderActivity::class.java)
        intentPpobAir.putExtra(AppKeys.KEY_PPOB_CATEGORY_PICKER_SEARCH_STATUS, true)
        intentPpobAir.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductAir())

        return PpobMenu(mContext?.getString(R.string.ppob_product_name_air_pdam), null, featureProduct.icon, intentPpobAir)
    }

    //INTERNET
    private fun getInternet(featureProduct: FeatureProduct): PpobMenu {
        val intentPpobInternet = Intent(mContext, PpobProviderActivity::class.java)
        intentPpobInternet.putExtra(AppKeys.KEY_PPOB_CATEGORY_PICKER_SEARCH_STATUS, true)
        intentPpobInternet.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductInternet())

        return PpobMenu(mContext?.getString(R.string.ppob_product_name_internet), null, featureProduct.icon, intentPpobInternet)
    }

    //ASURANSI
    private fun getAsuransi(featureProduct: FeatureProduct): PpobMenu {
        val intentPpobAsuransi = Intent(mContext, PpobProviderActivity::class.java)
        intentPpobAsuransi.putExtra(AppKeys.KEY_PPOB_CATEGORY_PICKER_SEARCH_STATUS, true)
        intentPpobAsuransi.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductAsuransi())

        return PpobMenu(mContext?.getString(R.string.ppob_product_name_asuransi), null, featureProduct.icon, intentPpobAsuransi)
    }

    //DONASI
    private fun getDonasi(featureProduct: FeatureProduct): PpobMenu {
        val intentPpobDonasi = Intent(mContext, PpobDonasiProductListActivity::class.java)
        return PpobMenu(mContext?.getString(R.string.ppob_product_name_donasi), null, featureProduct.icon, intentPpobDonasi)
    }

    //PENDIDIKAN
    private fun getPendidikan(featureProduct: FeatureProduct): PpobMenu {
        val intentPpobPendidikan = Intent(mContext, PpobProviderActivity::class.java)
        intentPpobPendidikan.putExtra(AppKeys.KEY_PPOB_CATEGORY_PICKER_SEARCH_STATUS, true)
        intentPpobPendidikan.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductPendidikan())

        return PpobMenu(mContext?.getString(R.string.ppob_product_name_pendidikan), null, featureProduct.icon, intentPpobPendidikan)
    }

    //CICILAN
    private fun getCicilan(featureProduct: FeatureProduct): PpobMenu {
        var intentPpobCicilan: Intent? = null

        intentPpobCicilan = Intent(mContext, PpobProviderActivity::class.java)
        intentPpobCicilan.putExtra(AppKeys.KEY_PPOB_CATEGORY_PICKER_SEARCH_STATUS, true)
        intentPpobCicilan.putExtra(AppKeys.KEY_PPOB_PRODUCT_TYPE_DATA, PpobProductTypeSetup().getProductCicilan())

        return PpobMenu(mContext?.getString(R.string.ppob_product_name_cicilan), null, featureProduct.icon, intentPpobCicilan)
    }

    //KIRIM UANG
    private fun getKirimUang(featureProduct: FeatureProduct): PpobMenu {
        var intentPpobKirimUang: Intent? = null

        return PpobMenu(mContext?.getString(R.string.ppob_product_name_kirim_uang), null, featureProduct.icon, intentPpobKirimUang)
    }

    //EMAS
    private fun getEmas(featureProduct: FeatureProduct): PpobMenu {
        var intentPpobEmas: Intent? = null

        return PpobMenu(mContext?.getString(R.string.ppob_product_name_emas), null, featureProduct.icon, intentPpobEmas)
    }

    //UMROH
    private fun getUmroh(featureProduct: FeatureProduct): PpobMenu {
        var intentPpobUmroh: Intent? = null

        return PpobMenu(mContext?.getString(R.string.ppob_product_name_umroh), null, featureProduct.icon, intentPpobUmroh)
    }

    //OASIS PESAN BARANG
    private fun getOasisPesanBarang(featureProduct: FeatureProduct): PpobMenu {
        var intent: Intent? = Intent().apply {
            mContext?.let {
                setClassName(it, QuadrantConstants.GROSIR_HOME)
            }
        }

        return PpobMenu(mContext?.getString(R.string.ppob_product_name_oasis_pesan_barang), null, featureProduct.icon, intent)
    }

    //OASIS RiWAYAT PESANAN
    private fun getOasisRiwayatPesanan(featureProduct: FeatureProduct): PpobMenu {
        var intent: Intent? = Intent().apply {
            mContext?.let {
                setClassName(it, QuadrantConstants.GROSIR_CHECK_STATUS_ACTIVITY)
            }
        }

        return PpobMenu(mContext?.getString(R.string.ppob_product_name_oasis_riwayat_pesanan), null, featureProduct.icon, intent)
    }

    //OASIS BAYAR PESANAN
    private fun getOasisBayarPesanan(featureProduct: FeatureProduct): PpobMenu {
        var intent: Intent? = null

        return PpobMenu(mContext?.getString(R.string.ppob_product_name_oasis_bayar_pesanan), null, featureProduct.icon, intent)
    }

    //MORE MENU
    private fun getMoreMenu(): PpobMenu {
        return PpobMenu("Other",
                R.drawable.icon_button_more, null, null)
    }

    //OlShopMenu
    fun getOlshopMenuForHome(): MutableList<PpobMenu> {
        val olshopMenuList: MutableList<PpobMenu> = ArrayList()

        if (SessionManager.getFeatureProduct() != null && SessionManager.getFeatureProduct().toko_online != null &&
                SessionManager.getFeatureProduct().toko_online.size > 0) {
            if (SessionManager.getFeatureProduct().toko_online[0].feature_product != null &&
                    SessionManager.getFeatureProduct().toko_online[0].feature_product.size > 0) {
                for (featureProduct in SessionManager.getFeatureProduct().toko_online[0].feature_product) {
                    if (featureProduct.status.equals(KEY_STATUS_ACTIVE, true)) {
                        olshopMenuList.add(PpobMenu(featureProduct.name, null, featureProduct.icon,
                                Intent(mContext, CatalogActivity::class.java).apply { putExtra("menuId", featureProduct.id) }))
                    }
                }
            }
        }
        return olshopMenuList
    }
}