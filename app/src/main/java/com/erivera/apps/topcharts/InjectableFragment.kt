package com.erivera.apps.topcharts

import androidx.fragment.app.Fragment
import com.erivera.apps.topcharts.viewmodels.ViewModelFactory
import javax.inject.Inject

open class InjectableFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
}