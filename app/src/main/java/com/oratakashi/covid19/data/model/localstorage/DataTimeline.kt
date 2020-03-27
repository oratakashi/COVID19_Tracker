package com.oratakashi.covid19.data.model.localstorage


data class DataTimeline (
    val date : String?,
    val case : Int?,
    val confirm : Int?,
    val recovered : Int?,
    val death : Int?
)