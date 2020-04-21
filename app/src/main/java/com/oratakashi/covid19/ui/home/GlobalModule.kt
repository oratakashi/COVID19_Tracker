package com.oratakashi.covid19.ui.home

import androidx.lifecycle.ViewModel
import com.oratakashi.covid19.data.network.ApiEndpoint
import com.oratakashi.covid19.di.scope.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit
import javax.inject.Named

@Module
abstract class GlobalModule {
    @Module
    companion object{
        @JvmStatic
        @Provides
        @Named("global")
        fun providesApiEndPoint(@Named("global") retrofit: Retrofit) : ApiEndpoint =
            retrofit.create(ApiEndpoint::class.java)
    }
    @Binds
    @IntoMap
    @ViewModelKey(GlobalViewModel::class)
    abstract fun bindViewModel(viewModel: GlobalViewModel) : ViewModel
}