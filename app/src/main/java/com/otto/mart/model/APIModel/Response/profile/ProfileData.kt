package com.otto.mart.model.APIModel.Response.profile

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProfileData(

        @field:JsonProperty("addressProvince")
        val addressProvince: String? = null,

        @field:JsonProperty("mpan")
        val mpan: String? = null,

        @field:JsonProperty("addressRegionCode")
        val addressRegionCode: String? = null,

        @field:JsonProperty("bin")
        val bin: String? = null,

        @field:JsonProperty("mid")
        val mid: String? = null,

        @field:JsonProperty("nmid")
        val nmid: String? = null,

        @field:JsonProperty("accountNumber")
        val accountNumber: String? = null,

        @field:JsonProperty("addressMunicipalityCode")
        val addressMunicipalityCode: String? = null,

        @field:JsonProperty("tid")
        val tid: String? = null,

        @field:JsonProperty("addressBarangay")
        val addressBarangay: String? = null,

        @field:JsonProperty("addressBarangayCode")
        val addressBarangayCode: String? = null,

        @field:JsonProperty("ownerName")
        val ownerName: String? = null,

        @field:JsonProperty("addressMunicipality")
        val addressMunicipality: String? = null,

        @field:JsonProperty("merchantGroupName")
        val merchantGroupName: String? = null,

        @field:JsonProperty("storeName")
        val storeName: String? = null,

        @field:JsonProperty("memberType")
        val memberType: String? = null,

        @field:JsonProperty("addressRegion")
        val addressRegion: String? = null,

        @field:JsonProperty("category")
        val category: String? = null,

        @field:JsonProperty("addressProvinceCode")
        val addressProvinceCode: String? = null,

        @field:JsonProperty("email")
        val email: String? = null,

        @field:JsonProperty("completeAddress")
        val completeAddress: String? = null,

        @field:JsonProperty("profilePictureUrl")
        val profilePictureUrl: String? = null
)
