package com.erivera.apps.topcharts

import android.animation.ArgbEvaluator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.erivera.apps.topcharts.databinding.FragmentHomeBinding
import com.erivera.apps.topcharts.models.domain.HomeTab
import com.erivera.apps.topcharts.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private val homeViewModel
            by lazy { ViewModelProviders.of(this.requireActivity()).get(HomeViewModel::class.java) }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.rootHomeLayout.addStatusBarTopPadding()
//        TabLayoutMediator(homeTabLayout, homeViewPager) { tab, position ->
//            tab.text = "OBJECT ${(position + 1)}"
//        }.attach()
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            //updateColor(position, positionOffset)
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }
    }

    fun updateColor(position: Int, positionOffset: Float) {
        val itemCount = homeViewPager.adapter?.itemCount?.minus(1) ?: 0
        val argbEvaluator = ArgbEvaluator()
        val colors = arrayListOf(Color.DKGRAY, Color.BLUE, Color.CYAN)

        if (position < itemCount && position < (colors.lastIndex)) {
            (argbEvaluator.evaluate(
                positionOffset,
                colors[position],
                colors[position + 1]
            ) as? Int)?.let {
                rootHomeLayout.setBackgroundColor(it)
            }
        } else {
            rootHomeLayout.setBackgroundColor(colors.last())
        }
    }

    override fun onResume() {
        super.onResume()
        view?.homeViewPager?.registerOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onPause() {
        super.onPause()
        view?.homeViewPager?.unregisterOnPageChangeCallback(onPageChangeCallback)
    }
}
