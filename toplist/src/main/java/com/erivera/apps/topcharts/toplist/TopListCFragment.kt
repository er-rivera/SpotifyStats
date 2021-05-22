package com.erivera.apps.topcharts.toplist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import com.erivera.apps.topcharts.toplist.databinding.FragmentTopCListBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

/**
 * A simple [Fragment] subclass.
 * Use the [TopListCFragment] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class TopListCFragment : Fragment() {

    lateinit var binding: FragmentTopCListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopCListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeView.setContent {
            TopList()
        }
    }
}
