package com.erivera.apps.topcharts.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.erivera.apps.topcharts.BR
import com.erivera.apps.topcharts.R
import com.erivera.apps.topcharts.databinding.FragmentPlayerV2Binding
import com.erivera.apps.topcharts.models.domain.AudioItem
import com.erivera.apps.topcharts.ui.listener.PlayerInteractionListener
import com.erivera.apps.topcharts.ui.viewmodel.PlayerViewModel
import com.erivera.apps.topcharts.utils.CollapsibleToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.OnBackPressedCallback
import com.erivera.apps.topcharts.databinding.DialogAudioFeatureBinding


@AndroidEntryPoint
class PlayerFragment : Fragment(),
    PlayerInteractionListener {

    private val playerViewModel: PlayerViewModel by viewModels()

    val listener = object : CollapsibleToolbar.OffsetChangedListener {
        override fun onOffsetChanged(verticalOffset: Int, progress: Float, totalRange: Float) {
            binding?.content?.motionProgress = progress
        }
    }

    var binding: FragmentPlayerV2Binding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    binding?.videoMotionLayout?.let {
                        if (it.progress != 0.0F) {
                            it.transitionToStart()
                        }
                    }
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerV2Binding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@PlayerFragment.viewLifecycleOwner
            viewModel = playerViewModel
            itemBinding =
                playerViewModel.getItemBinding().bindExtra(BR.listener, this@PlayerFragment)
            gridLayoutManager = GridLayoutManager(context, 2)
            listener = this@PlayerFragment
            executePendingBindings()
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerViewModel.setPlayerDefaultValues()
        binding?.mediaPlayerV3?.playerMediaMotionLayout?.setListener(listener)

    }

    override fun onDestroyView() {
        binding?.mediaPlayerV3?.playerMediaMotionLayout?.removeListener()
        super.onDestroyView()
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

    override fun onExpandClick() {
        binding?.videoMotionLayout?.transitionToEnd()
    }

    override fun onArrowDownClick() {
        binding?.videoMotionLayout?.transitionToStart()
    }

    override fun onInfoMenuClick() {
        val list = playerViewModel.albumColors.value ?: emptyArray()
        val bundle = bundleOf(ColorsFragment.ARG_COLOR_LIST to list.toIntArray())
        findNavController().navigate(R.id.action_fragment_player_to_colorsFragment, bundle)
    }

    override fun onGridItemClick(audioItem: AudioItem) {
        showDialog(audioItem)
    }

    @SuppressLint("InflateParams")
    private fun showDialog(audioItem: AudioItem) {
        // Create the fragment and show it as a dialog.
        val dialog =
            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.ThemeOverlay_App_MaterialAlertDialog
            )
                .setTitle(audioItem.displayTitle ?: "")
                .setMessage(audioItem.dialogText ?: "")
                .setPositiveButton("Cancel") { _: DialogInterface, _: Int ->
                }.apply {
                    if (audioItem.dialogDrawable != null) {
                        val inflater = LayoutInflater.from(this.context)
                        val binding = DialogAudioFeatureBinding.inflate(inflater, null, false)
                        binding.chartImageView.setImageResource(audioItem.dialogDrawable)
                        setView(binding.root)
                    }
                }
                .create()
        dialog.setOnShowListener {
            val colorArray = playerViewModel.albumColors.value
            val color =
                colorArray?.getOrNull(0) ?: colorArray?.getOrNull(1) ?: colorArray?.getOrNull(2)
                ?: ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color)

            val textView = dialog.findViewById<TextView>(R.id.alertTitle)
            val face = ResourcesCompat.getFont(
                requireContext(),
                R.font.inconsolata_regular
            )
            textView?.typeface = face
        }
        dialog.show()
    }
}
