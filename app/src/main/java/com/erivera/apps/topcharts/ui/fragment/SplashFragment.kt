package com.erivera.apps.topcharts.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.erivera.apps.topcharts.R
import com.erivera.apps.topcharts.StartupRouteState
import com.erivera.apps.topcharts.utils.addStatusBarTopPadding
import com.erivera.apps.topcharts.ui.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val splashViewModel: SplashViewModel by viewModels()

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
        splashViewModel.navigationLiveData.observe(viewLifecycleOwner, Observer { routeState ->
            rootView.postDelayed({
                if (isAdded.not()) return@postDelayed
                when (routeState) {
                    StartupRouteState.Login -> {
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                    StartupRouteState.Home -> {
                        findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
                    }
                }
            }, 2000)
        })
    }
}
