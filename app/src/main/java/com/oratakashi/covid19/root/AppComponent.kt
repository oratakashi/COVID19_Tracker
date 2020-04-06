package com.oratakashi.covid19.root


import com.oratakashi.covid19.di.builder.AppBuilder
import com.oratakashi.covid19.di.module.BNPBModule
import com.oratakashi.covid19.di.module.CoreModule
import com.oratakashi.covid19.di.module.GlobalModule
import com.oratakashi.covid19.di.module.OrataModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.AndroidSupportInjectionModule
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    CoreModule::class,
    OrataModule::class,
    BNPBModule::class,
    GlobalModule::class,
    AppBuilder::class
])
interface AppComponent : AndroidInjector<App>