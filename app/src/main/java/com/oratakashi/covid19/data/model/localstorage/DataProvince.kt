package com.oratakashi.covid19.data.model.localstorage

import com.google.gson.annotations.SerializedName

data class DataProvince (
    val provinsi : String?,
    val confirm : Int?,
    val recovered : Int?,
    val death : Int?,
    val lat : Float,
    val lang : Float
)