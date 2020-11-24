package com.erivera.apps.topcharts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.erivera.apps.topcharts.databinding.DialogAudioFeatureBinding

class AudioFeatureDialogFragment(
    private val title: String,
    private val chartDrawable: Int?,
    private val description: String
) : DialogFragment(), AudioFeatureListener {

    companion object {
        val TAG = AudioFeatureDialogFragment::class.java.name

        fun newInstance(
            title: String,
            chartDrawable: Int?,
            description: String
        ): AudioFeatureDialogFragment {
            return AudioFeatureDialogFragment(title, chartDrawable, description)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: DialogAudioFeatureBinding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_audio_feature,
            container, false
        )
        binding.title = title
        binding.chartDrawable = chartDrawable
        binding.description = description
        binding.listener = this
        return binding.root
    }

    override fun onNegativeClick() {
        dialog?.dismiss()
    }
}