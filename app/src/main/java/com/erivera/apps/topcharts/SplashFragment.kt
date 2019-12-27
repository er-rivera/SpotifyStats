package com.erivera.apps.topcharts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.erivera.apps.topcharts.repository.dagger.ViewModelFactory
import com.erivera.apps.topcharts.viewmodels.SplashViewModel
import javax.inject.Inject

class SplashFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val splashViewModel by lazy { ViewModelProviders.of(this).get(SplashViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false).apply {
            initObservers(this)
            splashViewModel.checkLoginStatus()
        }
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
                        Navigation.findNavController(rootView).navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                    StartupRouteState.Home -> {
                        Navigation.findNavController(rootView).navigate(R.id.action_splashFragment_to_mainFragment)
                    }
                }
            }, 2000)
        })
    }
}
