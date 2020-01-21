package com.erivera.apps.topcharts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.erivera.apps.topcharts.viewmodels.SplashViewModel

class SplashFragment : InjectableFragment() {

    private val splashViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            SplashViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false).apply {
            initObservers(this)
            splashViewModel.checkLoginStatus()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MainApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addStatusBarTopPadding()
    }

    private fun initObservers(rootView: View) {
        splashViewModel.navigationLiveData.observe(this, Observer { routeState ->
            rootView.postDelayed({
                when (routeState) {
                    StartupRouteState.Login -> {
                        Navigation.findNavController(rootView)
                            .navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                    StartupRouteState.Home -> {
                        Navigation.findNavController(rootView)
                            .navigate(R.id.action_splashFragment_to_mainFragment)
                    }
                }
            }, 2000)
        })
    }
}
