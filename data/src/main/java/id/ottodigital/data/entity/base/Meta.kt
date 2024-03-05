package id.ottodigital.data.entity.base

import id.ottodigital.data.helper.MetaChecker

class Meta {

    init {

    }

    var code: Int = 0
        set(value) {
            field = value
            MetaChecker.sessionCheck(this)
        }

    var status: Boolean = false
    var message: String = ""
}
