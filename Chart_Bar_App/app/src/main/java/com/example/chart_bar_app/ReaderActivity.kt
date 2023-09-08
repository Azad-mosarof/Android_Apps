package com.example.chart_bar_app

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chart_bar_app.databinding.ActivityReaderBinding
import com.github.mikephil.charting.data.*

class ReaderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list: ArrayList<RadarEntry> = ArrayList()

        list.add(RadarEntry(10f, 10f))
        list.add(RadarEntry(20f, 40f))
        list.add(RadarEntry(30f, 30f))
        list.add(RadarEntry(40f, 50f))
        list.add(RadarEntry(50f, 70f))
        list.add(RadarEntry(60f, 80f))
        list.add(RadarEntry(70f, 90f))
        list.add(RadarEntry(80f, 20f))
        list.add(RadarEntry(90f, 60f))
        list.add(RadarEntry(100f, 70f))

        val radarDataSet = RadarDataSet(list, "List")

        radarDataSet.setColors(Color.GREEN)

        radarDataSet.lineWidth = 2f
        radarDataSet.valueTextSize = 14f
        radarDataSet.valueTextColor = Color.RED
        val radarData = RadarData(radarDataSet)

        binding.raderChart.data = radarData
        binding.raderChart.description.text = "Radar Chart"
        binding.raderChart.animateY(2000)
    }
}