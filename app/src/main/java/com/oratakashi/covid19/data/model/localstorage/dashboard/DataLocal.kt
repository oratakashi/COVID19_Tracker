package com.oratakashi.covid19.data.model.localstorage.dashboard

data class DataLocal (
    val confirm : Int?,
    val recovered : Int?,
    val death : Int?,
    val date : String?,
    val name : String = ""
)