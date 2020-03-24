package com.oratakashi.covid19.data.model.localstorage

import com.google.gson.annotations.SerializedName

data class DataGlobal (
    val provinceState : String?,
    val countryRegion : String?,
    val lat : Float?,
    val long : Float?,
    val confirmed : Int?,
    val recovered : Int?,
    val deaths : Int?
)