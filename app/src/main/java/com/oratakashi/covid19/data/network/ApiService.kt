package com.oratakashi.covid19.data.network

import com.oratakashi.covid19.BuildConfig
import com.oratakashi.covid19.data.model.confirm.DataConfirm
import com.oratakashi.covid19.data.model.recovered.DataRecovered
import com.oratakashi.covid19.data.model.death.DataDeath
import com.oratakashi.covid19.data.model.statistik.ResponseStatistik
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService {
    val api : ApiEndpoint

    init{
        val client = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else
                    HttpLoggingInterceptor.Level.NONE
            })
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        api = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiEndpoint::class.java)
    }

    fun getData() : Single<ResponseStatistik> {
        return api.getData()
    }

    fun getConfirm() : Single<List<DataConfirm>>{
        return api.getConfirmed()
    }

    fun getRecovered() : Single<List<DataRecovered>>{
        return api.getRecovered()
    }

    fun getDeath() : Single<List<DataDeath>>{
        return api.getDeaths()
    }
}