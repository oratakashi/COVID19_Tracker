package com.oratakashi.covid19.ui.recovered

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
abstract class RecoveredModule {
    @Module
    companion object{
        @JvmStatic
        @Provides
        fun providesApiEndpoint(@Named("global") retrofit: Retrofit) : ApiEndpoint =
            retrofit.create(ApiEndpoint::class.java)
    }
    @Binds
    @IntoMap
    @ViewModelKey(RecoveredViewModel::class)
    abstract fun bindViewModel(viewModel: RecoveredViewModel) : ViewModel
}