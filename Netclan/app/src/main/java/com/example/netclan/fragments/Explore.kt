package com.example.netclan.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.netclan.R
import com.example.netclan.adapters.DescriptionPagerAdapter
import com.example.netclan.databinding.FragmentExploreBinding
import com.google.android.material.tabs.TabLayoutMediator

class Explore : Fragment() {

    private lateinit var binding: FragmentExploreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val informationFragments = arrayListOf<Fragment>(
            Personal(),
            Business(),
            Marchant()
        )
        binding.viewPagerHome.isUserInputEnabled = false
        val descriptionPager2Adapter = DescriptionPagerAdapter(informationFragments,childFragmentManager,lifecycle)
        binding.viewPagerHome.adapter = descriptionPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPagerHome){tab, position ->
            when(position){
                0 -> tab.text = "Personal"
                1 -> tab.text = "Business"
                2 -> tab.text = "Merchant"
            }
        }.attach()
    }
}