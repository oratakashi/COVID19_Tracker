package com.oratakashi.covid19.data.network

import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.data.model.confirm.DataConfirm
import com.oratakashi.covid19.data.model.death.DataDeath
import com.oratakashi.covid19.data.model.hospital.ResponseHospital
import com.oratakashi.covid19.data.model.instagram.ResponseInstagram
import com.oratakashi.covid19.data.model.news.ResponseNews
import com.oratakashi.covid19.data.model.province.ResponseProvince
import com.oratakashi.covid19.data.model.recovered.DataRecovered
import com.oratakashi.covid19.data.model.statistik.ResponseStatistik
import com.oratakashi.covid19.data.model.timeline.ResponseTimeline
import com.oratakashi.covid19.data.model.timeline.detail.ResponseDetailTimeline
import com.oratakashi.covid19.data.model.version.ResponseVersion
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
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
    @GET("{services}/FeatureServer/0/query")
    fun getDataBNPB(
        @Path("services") services : String,
        @Query("where") where : String = "1=1",
        @Query("outFields") outFields : String = "*",
        @Query("outSR") outSR : String = "4326",
        @Query("f") f : String = "json"
    ) : Single<ResponseProvince>
    @GET("{services}/FeatureServer/0/query")
    fun getDataTimeline(
        @Path("services") services : String,
        @Query("where") where : String = "1=1",
        @Query("outFields") outFields : String = "*",
        @Query("outSR") outSR : String = "4326",
        @Query("f") f : String = "json"
    ) : Single<ResponseTimeline>
    @GET("update")
    fun getUpdate(
        @Query("version") version : String = BuildConfig.VERSION_CODE.toString()
    ) : Single<ResponseVersion>
    @GET("instagram")
    fun getInstagram(
        @Query("limit") limit : Int
    ) : Single<ResponseInstagram>
    @GET("news")
    fun getNews(
        @Query("page") page : Int
    ) : Single<ResponseNews>
    @GET("{services}/FeatureServer/0/query")
    fun getDetailTimeline(
        @Path("services") services : String,
        @Query("where") where : String,
        @Query("outFields") outFields : String = "*",
        @Query("outSR") outSR : String = "4326",
        @Query("f") f : String = "json"
    ) : Single<ResponseDetailTimeline>
    @GET("{services}/FeatureServer/0/query")
    fun getHospital(
        @Path("services") services : String,
        @Query("where") where : String = "1=1",
        @Query("outFields") outFields : String = "*",
        @Query("outSR") outSR : String = "4326",
        @Query("f") f : String = "json"
    ) : Single<ResponseHospital>
}