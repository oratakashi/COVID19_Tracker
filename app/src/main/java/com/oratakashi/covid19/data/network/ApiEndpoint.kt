package com.oratakashi.covid19.data.network

import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.data.model.confirm.DataConfirm
import com.oratakashi.covid19.data.model.death.DataDeath
import com.oratakashi.covid19.data.model.province.ResponseProvince
import com.oratakashi.covid19.data.model.recovered.DataRecovered
import com.oratakashi.covid19.data.model.statistik.ResponseStatistik
import com.oratakashi.covid19.data.model.version.ResponseVersion
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiEndpoint {
    @GET("api")
    fun getData() : Single<ResponseStatistik>
    @GET("api/confirmed")
    fun getConfirmed() : Single<List<DataConfirm>>
    @GET("api/recovered")
    fun getRecovered() : Single<List<DataRecovered>>
    @GET("api/deaths")
    fun getDeaths() : Single<List<DataDeath>>
    @GET("query")
    fun getDataBNPB(
        @Query("where") where : String = "1=1",
        @Query("outFields") outFields : String = "Provinsi,Kasus_Terkonfirmasi_Akumulatif,Kasus_Sembuh_Akumulatif,Kasus_Meninggal_Akumulatif,Pembaruan",
        @Query("outSR") outSR : String = "4326",
        @Query("f") f : String = "json"
    ) : Single<ResponseProvince>
    @GET("update")
    fun getUpdate(
        @Query("version") version : String = BuildConfig.VERSION_CODE.toString()
    ) : Single<ResponseVersion>
}