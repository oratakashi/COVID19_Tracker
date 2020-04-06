package com.oratakashi.covid19.di.module

import com.oratakashi.covid19.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class BNPBModule {
    @Provides
    @Named("bnpb")
    fun providesHttpAdapter(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            client(client)
            baseUrl(BuildConfig.BASE_URL_BNPB)
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        }.build()
    }
}