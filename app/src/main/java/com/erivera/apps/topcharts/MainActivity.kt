package com.erivera.apps.topcharts

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.erivera.apps.topcharts.dagger.ParentDependency
import com.erivera.apps.topcharts.viewmodels.MainViewModel
import com.erivera.apps.topcharts.viewmodels.SpotifyRemoteViewModel
import com.erivera.apps.topcharts.viewmodels.ViewModelFactory
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val mainViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(MainViewModel::class.java)
    }

    private val spotifyRemoteViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(SpotifyRemoteViewModel::class.java)
    }

    @Inject
    lateinit var parentDependency: ParentDependency

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MainApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
