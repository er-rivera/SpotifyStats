package com.erivera.apps.topcharts.toplist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import com.erivera.apps.topcharts.toplist.databinding.FragmentTopCListBinding
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

/**
 * A simple [Fragment] subclass.
 * Use the [TopListCFragment] factory method to
 * create an instance of this fragment.
 */
class TopListCFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentTopCListBinding.inflate(inflater, container, false).apply {
            composeView.setContent {
                MaterialTheme {
                    ProvideWindowInsets {
                        TopList()
                    }
                }
            }
        }.root
    }
}
