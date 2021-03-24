package com.erivera.apps.topcharts.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.erivera.apps.topcharts.R
import com.erivera.apps.topcharts.databinding.FragmentLoginBinding
import com.erivera.apps.topcharts.ui.activity.MainActivity
import com.erivera.apps.topcharts.ui.listener.LoginInteractionListener
import com.erivera.apps.topcharts.utils.addStatusBarTopPadding
import com.erivera.apps.topcharts.ui.viewmodel.MainViewModel
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(), LoginInteractionListener {

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login,
            container, false
        )
        binding.listener = this
        binding.mainViewModel = mainViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addStatusBarTopPadding()
        initObservers()
    }

    private fun initObservers(){
        mainViewModel.screenNavigationLiveData.observe(viewLifecycleOwner, Observer {
            if(it != null && it){
                view?.let { view ->
                    Navigation.findNavController(view)
                        .navigate(R.id.action_loginFragment_to_mainFragment)
                }
            }
        })
    }

    override fun loginButtonClick(request: AuthenticationRequest?) {
        request?.let {
            AuthenticationClient.openLoginActivity(requireActivity(), MainActivity.REQUEST_CODE, it)
        }
    }
}