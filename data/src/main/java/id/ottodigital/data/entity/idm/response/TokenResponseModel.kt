package id.ottodigital.data.entity.idm.response

import id.ottodigital.data.entity.base.BaseResponse

data class TokenResponseModel(var data: TokenResponseData? = null) : BaseResponse()

data class TokenResponseData(
        var auth_token: String? = null,
        var user_id: Long = 0,
        var phone: String? = null
)