package com.erivera.apps.topcharts

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.erivera.apps.topcharts.databinding.FragmentPlayerBinding
import com.erivera.apps.topcharts.viewmodels.PlayerViewModel
import com.erivera.apps.topcharts.viewmodels.SpotifyRemoteViewModel

class PlayerFragment : InjectableFragment(), PlayerInteractionListener {

    private val playerViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(PlayerViewModel::class.java)
    }

    private val spotifyRemoteViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(SpotifyRemoteViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPlayerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel = playerViewModel
        binding.listener = this
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MainApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spotifyRemoteViewModel.currentTrack.observe(this, Observer {
            it?.let {
                playerViewModel.updateTrackInfo(it)
                Log.d(PlayerFragment::class.java.name, "Track: ${it.name}")
            }
        })
        spotifyRemoteViewModel.isPlaying.observe(this, Observer {
            it?.let {
                playerViewModel.updatePlayState(it)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        spotifyRemoteViewModel.connect()
    }

    override fun onStop() {
        super.onStop()
        spotifyRemoteViewModel.disconnect()
    }

    override fun onNextClick() {
        spotifyRemoteViewModel.next()
    }

    override fun onPlayPauseClick() {
        playerViewModel.getUri()?.let {
            spotifyRemoteViewModel.togglePlayPause(it)
        }
    }

    override fun onPrevClick() {
        spotifyRemoteViewModel.previous()
    }
}
