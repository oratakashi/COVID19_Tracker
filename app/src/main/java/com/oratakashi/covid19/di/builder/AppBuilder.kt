package com.oratakashi.covid19.di.builder

import androidx.lifecycle.ViewModelProvider
import com.oratakashi.covid19.di.factory.ViewModelFactory
import com.oratakashi.covid19.di.scope.Presentation
import com.oratakashi.covid19.ui.confirm.ConfirmFragment
import com.oratakashi.covid19.ui.confirm.ConfirmModule
import com.oratakashi.covid19.ui.death.DeathFragment
import com.oratakashi.covid19.ui.death.DeathModule
import com.oratakashi.covid19.ui.home.global_module.GlobalModule
import com.oratakashi.covid19.ui.home.HomeFragment
import com.oratakashi.covid19.ui.home.local_module.LocalModule
import com.oratakashi.covid19.ui.home.news_module.NewsModule
import com.oratakashi.covid19.ui.hospital.HospitalActivity
import com.oratakashi.covid19.ui.hospital.HospitalModule
import com.oratakashi.covid19.ui.main.v1.MainActivity
import com.oratakashi.covid19.ui.main.v2.GlobalDetailActivity
import com.oratakashi.covid19.ui.main.v2.SecondaryActivity
import com.oratakashi.covid19.ui.province.ProvinceFragment
import com.oratakashi.covid19.ui.province.ProvinceModule
import com.oratakashi.covid19.ui.recovered.RecoveredFragment
import com.oratakashi.covid19.ui.recovered.RecoveredModule
import com.oratakashi.covid19.ui.splash.SplashActivity
import com.oratakashi.covid19.ui.splash.SplashModule
import com.oratakashi.covid19.ui.statistik.StatistikFragment
import com.oratakashi.covid19.ui.statistik.StatistikModule
import com.oratakashi.covid19.ui.timeline.TimelineActivity
import com.oratakashi.covid19.ui.timeline.TimelineFragment
import com.oratakashi.covid19.ui.timeline.TimelineModule
import com.oratakashi.covid19.ui.timeline.detail.DetailTimelineActivity
import com.oratakashi.covid19.ui.timeline.detail.DetailTimelineModule
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

    /**
     * Register Secondary Activity
     */
    @Presentation
    @ContributesAndroidInjector
    abstract fun contributeSecondaryActivity() : SecondaryActivity

    /**
     * Register Home Fragment
     */
    @Presentation
    @ContributesAndroidInjector(modules = [LocalModule::class, GlobalModule::class, NewsModule::class])
    abstract fun contributeHomeFragment() :HomeFragment

    /**
     * Register for Global Detail Activity
     */
    @Presentation
    @ContributesAndroidInjector
    abstract fun contributeGlobalDetailActivity() : GlobalDetailActivity

    /**
     * Register Timeline Fragment
     */
    @Presentation
    @ContributesAndroidInjector(modules = [TimelineModule::class])
    abstract fun contributeTimelineFragment() : TimelineFragment

    /**
     * Register Detail Timeline Activity
     */
    @Presentation
    @ContributesAndroidInjector(modules = [DetailTimelineModule::class])
    abstract fun contributeDetailTimelineActivity() : DetailTimelineActivity

    /**
     * Register Hospital Activity
     */
    @Presentation
    @ContributesAndroidInjector(modules = [HospitalModule::class])
    abstract fun contributeHospitalActivity() : HospitalActivity
}