package com.erivera.apps.topcharts

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.erivera.apps.topcharts.databinding.FragmentPlayerBinding
import com.erivera.apps.topcharts.utils.CollapsibleToolbar
import com.erivera.apps.topcharts.viewmodels.PlayerViewModel
import kotlinx.android.synthetic.main.player_media_v3.view.*


class PlayerFragment : InjectableFragment(), PlayerInteractionListener {

    private val playerViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(PlayerViewModel::class.java)
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerViewModel.setPlayerMaxHeightMinHeight()
        view.motionLayout.setListener(object: CollapsibleToolbar.OffsetChangedListener {
            override fun onOffsetChanged(
                verticalOffset: Int,
                progress: Float,
                totalRange: Float
            ) {
                playerViewModel.updateOffset(verticalOffset,progress,totalRange)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MainApplication).appComponent?.inject(this)
    }

    override fun onAlbumArtLoaded(drawable: Drawable) {
        playerViewModel.analyzeDrawable(drawable)
    }

    override fun onNextClick() {
        playerViewModel.next()
    }

    override fun onPlayPauseClick() {
        playerViewModel.togglePlayPause()
    }

    override fun onPrevClick() {
        playerViewModel.previous()
    }

    override fun onInfoMenuClick() {
        val list = playerViewModel.albumColors.value ?: emptyArray()
        val bundle = bundleOf(ColorsFragment.ARG_COLOR_LIST to list.toIntArray())
        findNavController().navigate(R.id.action_fragment_player_to_colorsFragment, bundle)
    }
}
