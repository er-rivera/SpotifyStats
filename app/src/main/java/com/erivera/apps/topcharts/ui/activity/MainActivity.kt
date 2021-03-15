package com.erivera.apps.topcharts.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.erivera.apps.topcharts.R
import com.erivera.apps.topcharts.ui.viewmodel.MainViewModel
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel.updateDefaultWidthHeight(windowManager, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, data)

            when (response.type) {
                // Response was successful and contains auth token
                AuthenticationResponse.Type.TOKEN -> {
                    mainViewModel.navigateToNextScreen()
                    mainViewModel.saveSpotifyCredential(response.accessToken)
                }

                // Auth flow returned an error
                AuthenticationResponse.Type.ERROR -> {
                }
                else -> {
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 1337
    }
}
