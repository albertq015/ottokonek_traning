package com.otto.mart.ui.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.otto.mart.R
import com.otto.mart.model.APIModel.Request.PostAcquisitionRequest
import com.otto.mart.model.APIModel.Response.register.SearchMerchantData
import com.otto.mart.support.util.formValidation.FormValidation
import com.otto.mart.support.util.gone
import com.otto.mart.support.util.showToast
import com.otto.mart.support.util.visible
import com.otto.mart.ui.Partials.adapter.SpinnerAdapter
import com.otto.mart.ui.Partials.adapter.SpinnerData
import com.otto.mart.ui.activity.AppActivity
import com.otto.mart.ui.activity.register.data.SetupStoreInformation
import com.otto.mart.ui.activity.register.data.StoreCategory
import com.otto.mart.ui.activity.register.data.StoreInformation
import com.otto.mart.ui.activity.register.registerFromSales.registerPhotoInput.RegisterPhotoMerchantActivity
import kotlinx.android.synthetic.main.activity_active_point_menu.*
import kotlinx.android.synthetic.main.activity_store_information.*
import kotlinx.android.synthetic.main.activity_store_information.btnNext
import kotlinx.android.synthetic.main.content_cash_out_select_amount.*
import kotlinx.android.synthetic.main.toolbar.*

class StoreInformationActivity : AppActivity() {

    private var mBussinessLocation = ""
    private var mBussinessType = ""
    private var mBussinessCategory = ""
    private var mOperationalHour = ""
    private var mVisitorHour = ""

    var postAcquisitionRequest = PostAcquisitionRequest()
    var merchantData = SearchMerchantData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_information)

        if (intent.hasExtra(RegisterPhotoMerchantActivity.KEY_DATA_POST_ACQUISITIO)) {
            var dataJson = intent.getStringExtra(RegisterPhotoMerchantActivity.KEY_DATA_POST_ACQUISITIO)
            postAcquisitionRequest = Gson().fromJson(dataJson, PostAcquisitionRequest::class.java)
        }

        merchantData = Gson().fromJson(intent.getStringExtra("merchantData"), SearchMerchantData::class.java)

        initView()
        displayBussinessLocation()
        displayBussinessType()
        initBussinessCategory()
        displayOperationalHour()
        displayVisitorHour()
    }

    private fun initView() {
        tvToolbarTitle.text = "Store Information"

        btnToolbarBack.setOnClickListener {
            onBackPressed()
        }

        btnNext.setOnClickListener {
            if (isValidInput) {
                postAcquisitionRequest.business_location = mBussinessLocation
                postAcquisitionRequest.business_type = mBussinessType
                postAcquisitionRequest.business_category = mBussinessCategory
                postAcquisitionRequest.operational_hour = mOperationalHour
                postAcquisitionRequest.visiting_hour = mVisitorHour

                val intent = Intent(this, StoreLocationActivity::class.java)
                intent.putExtra(RegisterPhotoMerchantActivity.KEY_DATA_POST_ACQUISITIO, Gson().toJson(postAcquisitionRequest))
                intent.putExtra("merchantData", Gson().toJson(merchantData))
                startActivity(intent)
            }
        }
    }

    private val isValidInput: Boolean
        get() {
            var status = true

            if (mBussinessLocation.equals("")) {
                "Select business location!".showToast(this)
                status = false
            } else if (mBussinessType.equals("")) {
                "Select business type!".showToast(this)
                status = false
            } else if (mBussinessCategory.equals("")) {
                "Select business category!".showToast(this)
                status = false
            } else if (mOperationalHour.equals("")) {
                "Select operational hour!".showToast(this)
                status = false
            } else if (mVisitorHour.equals("")) {
                "Select visitor hour!".showToast(this)
                status = false
            }

            return status
        }

    private fun displayBussinessLocation() {
        val bussinessLocationList = mutableListOf<SpinnerData>().apply {
            add(SpinnerData("Permanent (Store)", "17041"))
            add(SpinnerData("Semi Permanent (Kiosk)", "17042"))
            add(SpinnerData("Non Permanent (Street Hawker)", "17043"))
        }

        spBussinessLocation.adapter = SpinnerAdapter(this, bussinessLocationList,
                android.R.layout.simple_spinner_dropdown_item,
                SpinnerData("Select business location", "0"))

        spBussinessLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if (position >= 1) {
                    mBussinessLocation = bussinessLocationList[position - 1].code
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
    }

    private fun displayBussinessType() {
        val bussinessTypelist = SetupStoreInformation().getBussinessType()
        val dataList = mutableListOf<SpinnerData>()

        for (storeInformation in bussinessTypelist) {
            dataList.add(SpinnerData(storeInformation.name.toString(), storeInformation.code.toString()))
        }

        spBussinessType.adapter = SpinnerAdapter(this, dataList,
                android.R.layout.simple_spinner_dropdown_item,
                SpinnerData("Select business type", "0"))

        spBussinessType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if (position >= 1) {
                    mBussinessType = dataList[position - 1].code
                    displayBussinessCategory(bussinessTypelist[position - 1])
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
    }

    private fun initBussinessCategory() {
        val dataList = mutableListOf<SpinnerData>()

        spBussinessCategory.adapter = SpinnerAdapter(this, dataList,
                android.R.layout.simple_spinner_dropdown_item,
                SpinnerData("Select business category", "0"))
    }

    private fun displayBussinessCategory(storeInformation: StoreInformation) {
        var categoryList = mutableListOf<StoreCategory>()
        val dataList = mutableListOf<SpinnerData>()

        storeInformation.category?.let {
            categoryList = it
        }

        dataList.clear()

        for (storeCategory in categoryList) {
            dataList.add(SpinnerData(storeCategory.name.toString(), storeCategory.code.toString()))
        }

        spBussinessCategory.adapter = SpinnerAdapter(this, dataList,
                android.R.layout.simple_spinner_dropdown_item,
                SpinnerData("Select business category", "0"))

        spBussinessCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if (position >= 1) {
                    mBussinessCategory = categoryList[position - 1].code.toString()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
    }

    private fun displayOperationalHour() {
        val dataList = mutableListOf<SpinnerData>().apply {
            add(SpinnerData("12:00am - 11:59pm", "1"))
            add(SpinnerData("7:00am - 7:00pm", "2"))
            add(SpinnerData("9:00am - 9:00pm", "3"))
            add(SpinnerData("5:00am to 5:00pm", "4"))
        }

        spOperationalHour.adapter = SpinnerAdapter(this, dataList,
                android.R.layout.simple_spinner_dropdown_item,
                SpinnerData("Select operational hour", "0"))

        spOperationalHour.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if (position >= 1) {
                    mOperationalHour = dataList[position - 1].code
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
    }

    private fun displayVisitorHour() {
        val dataList = mutableListOf<SpinnerData>().apply {
            add(SpinnerData("12:00 am - 11:59pm", "1"))
            add(SpinnerData("7:00am - 7:00pm", "2"))
            add(SpinnerData("9:00am - 9:00pm", "3"))
            add(SpinnerData("5:00am to 5:00pm", "4"))
        }

        spVisitorHour.adapter = SpinnerAdapter(this, dataList,
                android.R.layout.simple_spinner_dropdown_item,
                SpinnerData("Select visitor hour", "0"))

        spVisitorHour.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if (position >= 1) {
                    mVisitorHour = dataList[position - 1].code
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
    }
}