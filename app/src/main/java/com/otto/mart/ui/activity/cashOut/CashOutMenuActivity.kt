package com.otto.mart.ui.activity.cashOut

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.otto.mart.R
import com.otto.mart.model.localmodel.profile.ProfileMenu
import com.otto.mart.ui.Partials.adapter.setting.SettingMenuAdapter
import com.otto.mart.ui.activity.AppActivity
import kotlinx.android.synthetic.main.content_setting.*
import kotlinx.android.synthetic.main.toolbar.*

class CashOutMenuActivity : AppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_out_menu)

        initView()
        initRecyclerview()
        displayMenu()
    }

    private fun initView() {
        tvToolbarTitle.text = getString(R.string.title_activity_cash_out_menu)

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecyclerview() {
        recyclerview.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
    }

    private fun displayMenu() {
        val menuList = mutableListOf<ProfileMenu>()

        //Generate QR
        val generateQr = ProfileMenu()
        generateQr.name = getString(R.string.label_menu_cash_out_generate_qr)
        generateQr.intent = Intent(this, CashOutSelectAmountActivity::class.java)
        menuList.add(generateQr)

        //QR List
        val qrList = ProfileMenu()
        qrList.name = getString(R.string.label_menu_cash_out_qr_list)
        qrList.intent = Intent(this, CashOutQrListActivity::class.java)
        menuList.add(qrList)

        val adapter = SettingMenuAdapter(this as Activity, menuList)
        recyclerview.adapter = adapter

        adapter.setmOnViewClickListener(object : SettingMenuAdapter.OnViewClickListener {
            override fun onViewClickListener(data: ProfileMenu, position: Int) {
                menuSelected(data, position)
            }
        })
    }

    private fun menuSelected(data: ProfileMenu, position: Int) {
        if (data.intent != null) {
            startActivity(data.intent)
        }
    }
}