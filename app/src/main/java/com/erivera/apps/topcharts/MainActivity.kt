package com.erivera.apps.topcharts

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.erivera.apps.topcharts.viewmodels.MainViewModel
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse

class MainActivity : AppCompatActivity() {
    private val mainViewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        // Check if result comes from the correct activity
        if (requestCode == LoginFragment.REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, data)

            when (response.type) {
                // Response was successful and contains auth token
                AuthenticationResponse.Type.TOKEN -> {
                    mainViewModel.setAuthenticationResponse(response)
                }

                // Auth flow returned an error
                AuthenticationResponse.Type.ERROR -> {
                }
                else -> {}
            }
        }
    }
}
