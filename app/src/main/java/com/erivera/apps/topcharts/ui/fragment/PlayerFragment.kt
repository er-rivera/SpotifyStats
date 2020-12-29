package com.erivera.apps.topcharts.ui.fragment

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.erivera.apps.topcharts.BR
import com.erivera.apps.topcharts.MainApplication
import com.erivera.apps.topcharts.R
import com.erivera.apps.topcharts.databinding.FragmentPlayerV2Binding
import com.erivera.apps.topcharts.models.domain.AudioItem
import com.erivera.apps.topcharts.ui.listener.PlayerInteractionListener
import com.erivera.apps.topcharts.ui.viewmodel.PlayerViewModel
import com.erivera.apps.topcharts.utils.CollapsibleToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_audio_feature.view.*
import kotlinx.android.synthetic.main.fragment_player_v2.*
import kotlinx.android.synthetic.main.player_media_v3.*

class PlayerFragment : InjectableFragment(),
    PlayerInteractionListener {

    private val playerViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(PlayerViewModel::class.java)
    }

    val listener = object : CollapsibleToolbar.OffsetChangedListener {
        override fun onOffsetChanged(verticalOffset: Int, progress: Float, totalRange: Float) {
            content?.motionProgress = progress
        }
    }

    private val gestureDetector by lazy {
        GestureDetectorCompat(requireContext(),MyGestureListener())
    }

    private class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(event: MotionEvent): Boolean {
            Log.d("MainActivity", "MyGestureListener:onDown: $event")
            return true
        }

        override fun onFling(
            event1: MotionEvent,
            event2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            Log.d("MainActivity", "MyGestureListener:onFling: $event1 $event2")
            return true
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPlayerV2Binding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@PlayerFragment.viewLifecycleOwner
            viewModel = playerViewModel
            itemBinding =
                playerViewModel.getItemBinding().bindExtra(BR.listener, this@PlayerFragment)
            gridLayoutManager = GridLayoutManager(context, 2)
            listener = this@PlayerFragment
            executePendingBindings()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerViewModel.setPlayerDefaultValues()
        playerMediaMotionLayout.setListener(listener)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MainApplication).appComponent?.inject(this)
    }

    override fun onDestroyView() {
        playerMediaMotionLayout.removeListener()
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

    override fun onArrowDownClick() {
        videoMotionLayout.transitionToStart()
    }

    override fun onInfoMenuClick() {
        val list = playerViewModel.albumColors.value ?: emptyArray()
        val bundle = bundleOf(ColorsFragment.ARG_COLOR_LIST to list.toIntArray())
        findNavController().navigate(R.id.action_fragment_player_to_colorsFragment, bundle)
    }

    override fun onGridItemClick(audioItem: AudioItem) {
        showDialog(audioItem)
    }

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
                        val view = LayoutInflater.from(this.context)
                            .inflate(R.layout.dialog_audio_feature, null)
                        view.chartImageView.setImageResource(audioItem.dialogDrawable)
                        setView(view)
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
                R.font.lekton_bold
            )
            textView?.typeface = face
        }
        dialog.show()
    }
}
