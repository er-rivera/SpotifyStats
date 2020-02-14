package com.erivera.apps.topcharts.dagger

import android.content.Context
import com.erivera.apps.topcharts.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: PlayerFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: MainFragment)
    fun inject(fragment: SplashFragment)
}