package com.example.netclan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.netclan.databinding.ActivityMainBinding
import com.example.netclan.fragments.Chat
import com.example.netclan.fragments.Contacts
import com.example.netclan.fragments.Explore
import com.example.netclan.fragments.Groups
import com.example.netclan.fragments.Network

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.selectedItemId = R.id.explore
        replaceFragment(Explore())

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.explore -> replaceFragment(Explore())
                R.id.network -> replaceFragment(Network())
                R.id.chat -> replaceFragment(Chat())
                R.id.contacts -> replaceFragment(Contacts())
                R.id.groups -> replaceFragment(Groups())
                else -> {

                }
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}