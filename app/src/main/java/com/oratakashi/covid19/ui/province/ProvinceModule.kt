package com.oratakashi.covid19.ui.province

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
abstract class ProvinceModule {
    @Module
    companion object{
        @JvmStatic
        @Provides
        fun providesApiEndPoint(@Named("bnpb") retrofit: Retrofit) : ApiEndpoint =
            retrofit.create(ApiEndpoint::class.java)
    }
    @Binds
    @IntoMap
    @ViewModelKey(ProvinceViewModel::class)
    abstract fun bindViewModel(viewModel: ProvinceViewModel) : ViewModel
}