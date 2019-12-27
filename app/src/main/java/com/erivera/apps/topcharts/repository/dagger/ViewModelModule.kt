package com.erivera.apps.topcharts.repository.dagger

import androidx.lifecycle.ViewModel
import com.erivera.apps.topcharts.viewmodels.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun providesMainViewModel(mainViewModel: MainViewModel) : ViewModel
}