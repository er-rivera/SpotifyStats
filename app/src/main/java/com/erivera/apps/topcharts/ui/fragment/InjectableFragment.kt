package com.erivera.apps.topcharts.ui.fragment

import androidx.fragment.app.Fragment
import com.erivera.apps.topcharts.ui.viewmodel.ViewModelFactory
import javax.inject.Inject

open class InjectableFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
    }
}