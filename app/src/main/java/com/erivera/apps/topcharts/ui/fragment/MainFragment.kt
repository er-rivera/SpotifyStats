package com.erivera.apps.topcharts.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.erivera.apps.topcharts.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.findFragmentById(R.id.innerNavHostFragment)?.view?.let { navView ->
            val navController = Navigation.findNavController(navView)
            bottomNavigation.setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.fragment_home -> {
                        navController.navigate(R.id.fragment_home)

                        true
                    }
                    R.id.fragment_top_list -> {
                        navController.navigate(R.id.fragment_top_list)
                        true
                    }
                    else -> false
                }
            }
        }
        placePlayerFragment()
    }

    private fun placePlayerFragment() {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment())
            .commit()
    }
}
