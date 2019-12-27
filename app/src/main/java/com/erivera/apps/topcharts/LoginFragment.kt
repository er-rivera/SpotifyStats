package com.erivera.apps.topcharts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.erivera.apps.topcharts.databinding.FragmentLoginBinding
import com.erivera.apps.topcharts.viewmodels.MainViewModel
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse


class LoginFragment : Fragment(), LoginInteractionListener {
    private val mainViewModel by lazy { ViewModelProviders.of(this.requireActivity()).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.authenticationResponseLiveData.observe(this, Observer {
            it?.let { authReponse ->
                this.view?.let {view ->
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainFragment)
                    mainViewModel.saveSpotifyCredential(authReponse.accessToken)
                }
            }
        })
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addStatusBarTopPadding()
    }

    override fun loginButtonClick() {
        val builder = AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
        builder.setScopes(arrayOf("streaming","user-read-recently-played","user-top-read"))
        AuthenticationClient.openLoginActivity(requireActivity(), REQUEST_CODE, builder.build())
        Log.i(javaClass.simpleName, "Click")
    }

    companion object {
        const val REQUEST_CODE = 1337
        private const val REDIRECT_URI = "https://github.com/er-rivera/SpotifyStats/"
        private const val CLIENT_ID = "8796497bbe804de1af6eb358bdcdc53f"
    }
}