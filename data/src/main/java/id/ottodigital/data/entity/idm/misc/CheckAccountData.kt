package id.ottodigital.data.entity.idm.misc

data class CheckAccountData (
    var area: UserArea? = null,
    var division: Division? = null,
    var role: Int= 0,
    var phone: String? = null,
    var price: PriceData? = null,
    var name: String? = null,
    var stockpoint: UserStockpoint? = null,
    var id :Int= 0,
    var auth_token: String? = null,
    var delivery_code: String? = null
)