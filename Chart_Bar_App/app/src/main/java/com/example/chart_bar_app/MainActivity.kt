package com.example.chart_bar_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chart_bar_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.barActivity.setOnClickListener{
            startActivity(Intent(this, BarActivity::class.java))
        }

        binding.pieChartActivity.setOnClickListener{
            startActivity(Intent(this, PieActivity::class.java))
        }

        binding.readerActivity.setOnClickListener{
            startActivity(Intent(this, ReaderActivity::class.java))
        }

        binding.lineChartActivity.setOnClickListener{
            startActivity(Intent(this, LineChart::class.java))
        }
    }
}