package com.erivera.apps.topcharts.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.erivera.apps.topcharts.MainApplication
import com.erivera.apps.topcharts.R
import com.erivera.apps.topcharts.databinding.FragmentTopListBinding
import com.erivera.apps.topcharts.models.domain.TopListTab
import com.erivera.apps.topcharts.ui.adapter.PagerRecyclerViewAdapter
import com.erivera.apps.topcharts.ui.viewmodel.TopListViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_top_list.*
import kotlinx.android.synthetic.main.fragment_top_list.view.*

class TopListFragment : InjectableFragment() {

    private val topListViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(TopListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topListViewModel.loadItems()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTopListBinding.inflate(inflater, container, false)
        binding.viewModel = topListViewModel
        binding.adapter =
            PagerRecyclerViewAdapter<TopListTab>()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MainApplication).appComponent?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.topListViewPager.post {
            TabLayoutMediator(topListTabLayout, topListViewPager) { tab, position ->
                tab.text = topListViewModel.getTabName(position)
            }.attach()
        }
    }
}