package com.oratakashi.covid19.di.builder

import androidx.lifecycle.ViewModelProvider
import com.oratakashi.covid19.di.factory.ViewModelFactory
import com.oratakashi.covid19.di.scope.Presentation
import com.oratakashi.covid19.ui.confirm.ConfirmFragment
import com.oratakashi.covid19.ui.confirm.ConfirmModule
import com.oratakashi.covid19.ui.death.DeathFragment
import com.oratakashi.covid19.ui.death.DeathModule
import com.oratakashi.covid19.ui.main.MainActivity
import com.oratakashi.covid19.ui.province.ProvinceFragment
import com.oratakashi.covid19.ui.province.ProvinceModule
import com.oratakashi.covid19.ui.recovered.RecoveredFragment
import com.oratakashi.covid19.ui.recovered.RecoveredModule
import com.oratakashi.covid19.ui.splash.SplashActivity
import com.oratakashi.covid19.ui.splash.SplashModule
import com.oratakashi.covid19.ui.statistik.StatistikFragment
import com.oratakashi.covid19.ui.statistik.StatistikModule
import com.oratakashi.covid19.ui.timeline.TimelineActivity
import com.oratakashi.covid19.ui.timeline.TimelineModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * for Register Activity and Fragment to Depedency Injection
 */
@Module
abstract class AppBuilder {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    /**
     * Register Splash Activity
     */
    @Presentation
    @ContributesAndroidInjector(modules = [SplashModule::class])
    abstract fun contributeSplashActivity() : SplashActivity

    /**
     * Register Main Activity
     */
    @Presentation
    @ContributesAndroidInjector
    abstract fun contributeMainActivity() : MainActivity

    /**
     * Register Timeline Activity
     */
    @Presentation
    @ContributesAndroidInjector(modules = [TimelineModule::class])
    abstract fun contributeTimelineActivity() : TimelineActivity

    /**
     * Register Province Fragment
     */
    @Presentation
    @ContributesAndroidInjector(modules = [ProvinceModule::class])
    abstract fun contributeProvinceFragment() : ProvinceFragment

    /**
     * Register Statistik Fragment
     */
    @Presentation
    @ContributesAndroidInjector(modules = [StatistikModule::class])
    abstract fun contributeStatistikFragment() : StatistikFragment

    /**
     * Register Confirmed Fragment
     */
    @Presentation
    @ContributesAndroidInjector(modules = [ConfirmModule::class])
    abstract fun contributeConfirmFragment() : ConfirmFragment

    /**
     * Register Recovered Fragment
     */
    @Presentation
    @ContributesAndroidInjector(modules = [RecoveredModule::class])
    abstract fun contributeRecoveredFragment() : RecoveredFragment

    /**
     * Register Death Fragment
     */
    @Presentation
    @ContributesAndroidInjector(modules = [DeathModule::class])
    abstract fun contributeDeathFragment() :DeathFragment
}