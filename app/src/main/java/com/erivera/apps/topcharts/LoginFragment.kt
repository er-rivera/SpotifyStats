package com.erivera.apps.topcharts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.erivera.apps.topcharts.databinding.FragmentLoginBinding
import com.erivera.apps.topcharts.utils.addStatusBarTopPadding
import com.erivera.apps.topcharts.viewmodels.MainViewModel
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest

class LoginFragment : InjectableFragment(), LoginInteractionListener {

    private val mainViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(MainViewModel::class.java)
    }

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
        mainViewModel.screenNavigationLiveData.observe(this, Observer {
            if(it != null && it){
                view?.let { view ->
                    Navigation.findNavController(view)
                        .navigate(R.id.action_loginFragment_to_mainFragment)
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MainApplication).appComponent.inject(this)
    }

    override fun loginButtonClick(request: AuthenticationRequest?) {
        request?.let {
            AuthenticationClient.openLoginActivity(requireActivity(), MainActivity.REQUEST_CODE, it)
        }
    }
}