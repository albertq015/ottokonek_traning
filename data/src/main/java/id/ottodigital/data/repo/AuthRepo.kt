package id.ottodigital.data.repo

import android.annotation.SuppressLint
import id.ottodigital.data.helper.AuthKeyModule
import id.ottodigital.data.model.register.request.SearchMerchantRequest
import id.ottodigital.data.remote.op.OPService
import javax.inject.Inject

@SuppressLint("CheckResult")
class AuthRepo @Inject constructor(val service: OPService) : BaseRepo() {
    override fun getAuthKey(): String = AuthKeyModule.OP_AUTH_VALUE

    fun doSearchMerchant(searchMerchantRequest: SearchMerchantRequest) =
            service.searchMerchantService(searchMerchantRequest)

}