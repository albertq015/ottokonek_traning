package com.otto.mart.ui.activity.multibank

import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.model.APIModel.Response.multibank.ValidateQrResponse
import com.otto.mart.ui.activity.AppActivity

class SelectAccountPaymentScanQrActivity : AppActivity() {

    lateinit var model: ValidateQrResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_multibank)

        model = Gson().fromJson(intent.getStringExtra("dataAccount"), ValidateQrResponse::class.java)

        Log.d("hasilnyaa",model.toString())

    }
}