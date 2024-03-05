package id.ottodigital.data.repo

import id.ottodigital.data.helper.AuthKeyModule
import id.ottodigital.data.remote.idm.IDMService
import javax.inject.Inject

class OrderStatusRepo @Inject constructor(val remoteService: IDMService) : BaseRepo() {
    override fun getAuthKey(): String = AuthKeyModule.IDM_AUTH_VALUE

    fun getOrderStatus(status: String, params: HashMap<String, String>) =
        remoteService.serviceOrderHistories(auth, status, params)
}