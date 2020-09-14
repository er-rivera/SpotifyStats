package com.erivera.apps.topcharts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.erivera.apps.topcharts.databinding.FragmentHomeBinding
import com.erivera.apps.topcharts.models.domain.HomeTab
import com.erivera.apps.topcharts.utils.addStatusBarTopPadding
import com.erivera.apps.topcharts.viewmodels.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : InjectableFragment() {

    private val homeViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.loadItems()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = homeViewModel
        binding.adapter = PagerRecyclerViewAdapter<HomeTab>()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MainApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragment?.parentFragment?.view?.findViewById<DrawerLayout>(R.id.drawerLayout)?.let {
            NavigationUI.setupWithNavController(homeToolbar, findNavController(), it)
            homeToolbar.title = ""
            homeTitle.text = homeViewModel.getHomeTitle()
        }
        view.rootHomeLayout.addStatusBarTopPadding()
        view.homeViewPager.post {
            TabLayoutMediator(homeTabLayout, homeViewPager) { tab, position ->
                tab.text = homeViewModel.getTabName(position)
            }.attach()
        }
    }
}