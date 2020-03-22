package com.oratakashi.covid19.ui.main

import com.oratakashi.covid19.data.model.confirm.DataConfirm
import com.oratakashi.covid19.data.model.death.DataDeath
import com.oratakashi.covid19.data.model.province.DataProvince
import com.oratakashi.covid19.data.model.recovered.DataRecovered
import com.oratakashi.covid19.data.model.statistik.ResponseStatistik

interface MainInterfaces {
    fun resultStatistik(data : ResponseStatistik)
    fun resultConfirmed(data : List<DataConfirm>)
    fun resultRecovered(data : List<DataRecovered>)
    fun resultDeath(data : List<DataDeath>)
    fun resultProvince(data : List<DataProvince>)
    fun getLocation(lat : Double, lng : Double, zoom : Float = 5f)
}