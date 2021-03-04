package com.erivera.apps.topcharts.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erivera.apps.topcharts.MainApplication
import com.erivera.apps.topcharts.R

class TopListComposeFragment : InjectableFragment() {

    //private val topListViewModel: TopListViewModel = viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MainApplication).appComponent?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_top_list_compose, container, false).apply {
//            findViewById<ComposeView>(R.id.composeView).setContent {
//                MaterialTheme {
//                    ProvideWindowInsets {
//                        TopList()
//                    }
//                }
//            }
        }
    }

}