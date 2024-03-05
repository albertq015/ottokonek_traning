package com.otto.mart.ui.activity.ppob.setup

import android.content.Context
import com.otto.mart.model.APIModel.Misc.WidgetBuilderModel
import com.otto.mart.model.APIModel.Request.donasi.DonasiPaymentRequest
import com.otto.mart.model.APIModel.Response.PpobOttoagInquiryResponseModel
import com.otto.mart.model.APIModel.Response.ppob.PpobInquiryResponse

class PpobConfirmationDataSetup(context: Context) {

    var mContext: Context? = null

    init {
        mContext = context
    }

    fun getPulsa(inquiryResponse: PpobInquiryResponse): MutableList<WidgetBuilderModel> {
        var dataList = mutableListOf<WidgetBuilderModel>()

        var noHp = WidgetBuilderModel()
        noHp.key = "Nomor HP"
        noHp.value = inquiryResponse.data.cust_id
        dataList.add(noHp)

        var provider = WidgetBuilderModel()
        provider.key = "Provider"
        provider.value = inquiryResponse.data.list_detail.company
        dataList.add(provider)

        var nominal = WidgetBuilderModel()
        nominal.key = "Nominal"
        nominal.value = inquiryResponse.data.amount.toString()
        dataList.add(nominal)

        return dataList
    }

    fun getDefaultKeyValue(ppobInquiryOldResponse: PpobOttoagInquiryResponseModel): MutableList<WidgetBuilderModel> {
        var dataList: MutableList<WidgetBuilderModel>

        dataList = ppobInquiryOldResponse.data.key_value_list

        return dataList
    }

    fun getDefaultKeyValue(ppobInquiryResponse: PpobInquiryResponse): MutableList<WidgetBuilderModel> {
        var dataList: MutableList<WidgetBuilderModel>

        dataList = ppobInquiryResponse.data.key_value_list

        return dataList
    }

    fun getPulsaPsacabayar(ppobInquiryResponse: PpobInquiryResponse): MutableList<WidgetBuilderModel> {
        var dataList: MutableList<WidgetBuilderModel>

        dataList = ppobInquiryResponse.data.key_value_list

        return dataList
    }

    fun getPaketData(inquiryResponse: PpobInquiryResponse): MutableList<WidgetBuilderModel> {
        var dataList = mutableListOf<WidgetBuilderModel>()

        var noHp = WidgetBuilderModel()
        noHp.key = "Nomor HP"
        noHp.value = inquiryResponse.data.cust_id
        dataList.add(noHp)

        var provider = WidgetBuilderModel()
        provider.key = "Provider"
        provider.value = inquiryResponse.data.list_detail.company
        dataList.add(provider)

        var nominal = WidgetBuilderModel()
        nominal.key = "Nominal"
        nominal.value = inquiryResponse.data.amount.toString()
        dataList.add(nominal)

        return dataList
    }

    fun getListrikToken(inquiryResponse: PpobInquiryResponse): MutableList<WidgetBuilderModel> {
        var dataList: MutableList<WidgetBuilderModel>

        dataList = inquiryResponse.data.key_value_list

        return dataList
    }

    fun getListrikTagihan(inquiryResponse: PpobInquiryResponse): MutableList<WidgetBuilderModel> {
        var dataList: MutableList<WidgetBuilderModel>

        dataList = inquiryResponse.data.key_value_list

        return dataList
    }

    fun getAir(inquiryResponse: PpobInquiryResponse): MutableList<WidgetBuilderModel> {
        val dataList = mutableListOf<WidgetBuilderModel>()

        for (widgetBuilderModel in inquiryResponse.data.key_value_list) {
            if (widgetBuilderModel.value != null && !widgetBuilderModel.value.isEmpty()) {
                dataList.add(widgetBuilderModel)
            }
        }

        return dataList
    }

    fun getInternet(ppobInquiryResponse: PpobInquiryResponse): MutableList<WidgetBuilderModel> {
        var dataList: MutableList<WidgetBuilderModel>

        dataList = ppobInquiryResponse.data.key_value_list

        return dataList
    }

    fun getAsuransi(ppobInquiryOldResponse: PpobOttoagInquiryResponseModel): MutableList<WidgetBuilderModel> {
        var dataList: MutableList<WidgetBuilderModel>

        dataList = ppobInquiryOldResponse.data.key_value_list

        return dataList
    }

    fun getTopUp(ppobInquiryResponse: PpobInquiryResponse): MutableList<WidgetBuilderModel> {
        var dataList: MutableList<WidgetBuilderModel>

        dataList = ppobInquiryResponse.data.key_value_list

        return dataList
    }

    fun getDonasi(productName: String, donasiPaymentRequest: DonasiPaymentRequest): MutableList<WidgetBuilderModel> {

        val dataSplitList = donasiPaymentRequest.customer_reference.split("|").toTypedArray()

        var dataList = mutableListOf<WidgetBuilderModel>()

        var product = WidgetBuilderModel()
        product.key = "Produk"
        product.value = productName
        dataList.add(product)

        var name = WidgetBuilderModel()
        name.key = "Nama Donatur"
        name.value = dataSplitList[0].trim()
        dataList.add(name)

        var phone = WidgetBuilderModel()
        phone.key = "Nomor HP"
        phone.value = dataSplitList[2].trim()
        dataList.add(phone)

        if (!dataSplitList[3].trim().equals("")) {
            var npwp = WidgetBuilderModel()
            npwp.key = "NPWP Donatur"
            npwp.value = dataSplitList[3].trim()
            dataList.add(npwp)
        }

        if (!dataSplitList[1].trim().equals("")) {
            var email = WidgetBuilderModel()
            email.key = "Email Donatur"
            email.value = dataSplitList[1].trim()
            dataList.add(email)
        }

        return dataList
    }
}