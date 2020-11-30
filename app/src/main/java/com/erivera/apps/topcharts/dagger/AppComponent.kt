package com.erivera.apps.topcharts.dagger

import android.app.Application
import com.erivera.apps.topcharts.ui.activity.MainActivity
import com.erivera.apps.topcharts.ui.fragment.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder?
        fun build(): AppComponent?
    }

    fun inject(mainActivity: MainActivity)
    fun inject(fragment: TopListFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: PlayerFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: MainFragment)
    fun inject(fragment: SplashFragment)
}