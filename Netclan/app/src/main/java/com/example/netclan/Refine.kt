package com.example.netclan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.netclan.databinding.ActivityRefineBinding

class Refine : AppCompatActivity() {

    private lateinit var binding: ActivityRefineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRefineBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}