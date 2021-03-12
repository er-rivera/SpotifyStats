package com.erivera.apps.topcharts.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.erivera.apps.topcharts.databinding.FragmentTopListBinding
import com.erivera.apps.topcharts.models.domain.TopListTab
import com.erivera.apps.topcharts.ui.adapter.PagerRecyclerViewAdapter
import com.erivera.apps.topcharts.ui.viewmodel.TopListViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopListFragment : Fragment() {

    private val topListViewModel: TopListViewModel by viewModels()

    var binding: FragmentTopListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topListViewModel.loadItems()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopListBinding.inflate(inflater, container, false)
        binding?.viewModel = topListViewModel
        binding?.adapter =
            PagerRecyclerViewAdapter<TopListTab>()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.topListViewPager?.post {
            binding?.apply {
                TabLayoutMediator(topListTabLayout, topListViewPager) { tab, position ->
                    tab.text = topListViewModel.getTabName(position)
                }.attach()
            }
        }
    }
}