package id.ottodigital.data.repo

import android.annotation.SuppressLint
import id.ottodigital.data.entity.idm.request.CheckAccountRequestModel
import id.ottodigital.data.helper.AuthKeyModule
import id.ottodigital.data.manager.IDMSessionManager
import id.ottodigital.data.remote.idm.IDMService
import javax.inject.Inject

@SuppressLint("CheckResult")
class LoginRepo @Inject constructor(val idmService: IDMService, val idmSessionManager: IDMSessionManager) : BaseRepo() {
    override fun getAuthKey(): String = AuthKeyModule.IDM_AUTH_VALUE

    fun doLogin(checkAccountRequestModel: CheckAccountRequestModel) =
            idmService.serviceGetToken(checkAccountRequestModel)
                    .doOnNext { tokenResponse ->
                        idmSessionManager.idmToken = tokenResponse.data?.auth_token
                        idmSessionManager.idmUserID = tokenResponse.data?.user_id?.toString()
                    }


    fun getAccount(userId: Long) =
            idmService.serviceGetAccount(auth, userId.toString())
                    .doOnNext { response ->
                        idmSessionManager.idmShippingCode = response.data.customers?.delivery_code
                        idmSessionManager.idmPhone = response.data.customers?.phone
                        idmSessionManager.idmName = response.data.customers?.name
                        idmSessionManager.idmCustId = response.data.customers?.id?.toString()
                        idmSessionManager.idmLocationCode = response.data.customers?.stockpoint?.location_code
                    }

}