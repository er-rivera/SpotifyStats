package com.erivera.apps.topcharts

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.erivera.apps.topcharts.databinding.FragmentPlayerBinding
import com.erivera.apps.topcharts.utils.CustomGradientDrawable
import com.erivera.apps.topcharts.viewmodels.PlayerViewModel
import com.erivera.apps.topcharts.viewmodels.SpotifyRemoteViewModel
import kotlinx.android.synthetic.main.fragment_player.*


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

    private var animDrawable: AnimationDrawable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPlayerBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@PlayerFragment.viewLifecycleOwner
            viewModel = playerViewModel
            gridLayoutManager = GridLayoutManager(context, 2)
            listener = this@PlayerFragment
            executePendingBindings()
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MainApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spotifyRemoteViewModel.currentTrack.observe(viewLifecycleOwner, Observer {
            it?.let {
                playerViewModel.updateTrackInfo(it)
                Log.d(PlayerFragment::class.java.name, "Track: ${it.name}")
            }
        })
        spotifyRemoteViewModel.isPlaying.observe(viewLifecycleOwner, Observer {
            it?.let {
                playerViewModel.updatePlayState(it)
            }
        })
        playerViewModel.albumColors.observe(viewLifecycleOwner, Observer {
            it?.let {
                generateAnimatedDrawable(it)
            }
        })
    }

    override fun onAlbumArtLoaded(drawable: Drawable) {
        playerViewModel.analyzeDrawable(drawable)
    }

    private fun generateAnimatedDrawable(array: Array<Int>) {
        val firstColor = array[0]
        val secondColor = array[1]
        val thirdColor = array[2]
        val gradient1 = CustomGradientDrawable(
            firstColor,
            secondColor,
            thirdColor
        )
        val gradient2 = CustomGradientDrawable(
            secondColor,
            thirdColor,
            firstColor
        )
        val gradient3 = CustomGradientDrawable(
            thirdColor,
            firstColor,
            secondColor
        )
        val drawable = AnimationDrawable().apply {
            addFrame(gradient1, 5000)
            addFrame(gradient2, 5000)
            addFrame(gradient3, 5000)
            isOneShot = false
        }
        gradientView.background = drawable
        animDrawable = (gradientView.background as? AnimationDrawable)?.apply {
            setEnterFadeDuration(10)
            setExitFadeDuration(5000)
            start()
        }
    }

    override fun onResume() {
        super.onResume()
        if (animDrawable?.isRunning == false) {
            animDrawable?.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (animDrawable?.isRunning == true) {
            animDrawable?.stop()
        }
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