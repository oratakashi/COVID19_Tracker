package com.oratakashi.covid19.ui.death

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
abstract class DeathModule {
    @Module
    companion object{
        @JvmStatic
        @Provides
        fun providesApiEndpoint(@Named("global") retrofit: Retrofit) : ApiEndpoint =
            retrofit.create(ApiEndpoint::class.java)
    }
    @Binds
    @IntoMap
    @ViewModelKey(DeathViewModel::class)
    abstract fun bindViewModel(viewModel: DeathViewModel) : ViewModel
}