package com.erivera.apps.topcharts.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.erivera.apps.topcharts.R
import com.erivera.apps.topcharts.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    var binding: FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.findFragmentById(R.id.innerNavHostFragment)?.view?.let { navView ->
            val navController = Navigation.findNavController(navView)
            binding?.bottomNavigation?.setupWithNavController(navController)
        }
        placePlayerFragment()
    }

    private fun placePlayerFragment() {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment())
            .commit()
    }
}
