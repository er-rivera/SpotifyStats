package com.erivera.apps.topcharts.ui.fragment

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.erivera.apps.topcharts.BR
import com.erivera.apps.topcharts.MainApplication
import com.erivera.apps.topcharts.R
import com.erivera.apps.topcharts.databinding.FragmentPlayerBinding
import com.erivera.apps.topcharts.models.domain.AudioItem
import com.erivera.apps.topcharts.ui.listener.PlayerInteractionListener
import com.erivera.apps.topcharts.ui.viewmodel.PlayerViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_audio_feature.view.*

class PlayerFragment : InjectableFragment(),
    PlayerInteractionListener {

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
                ?: ContextCompat.getColor(requireContext(),
                    R.color.white
                )
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color)

            val textView = dialog.findViewById<TextView>(R.id.alertTitle)
            val face = ResourcesCompat.getFont(requireContext(),
                R.font.lekton_bold
            )
            textView?.typeface = face
        }
        dialog.show()
    }
}
