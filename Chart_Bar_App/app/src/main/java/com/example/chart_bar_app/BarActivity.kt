package com.example.chart_bar_app

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chart_bar_app.databinding.ActivityBarBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class BarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list: ArrayList<BarEntry> = ArrayList()

        list.add(BarEntry(10f, 10f))
        list.add(BarEntry(20f, 40f))
        list.add(BarEntry(30f, 30f))
        list.add(BarEntry(40f, 50f))
        list.add(BarEntry(50f, 70f))
        list.add(BarEntry(60f, 80f))
        list.add(BarEntry(70f, 90f))
        list.add(BarEntry(80f, 20f))
        list.add(BarEntry(90f, 60f))
        list.add(BarEntry(100f, 70f))

        val barDataSet = BarDataSet(list, "List")

        barDataSet.setColors(Color.parseColor("#ff5633"))
        barDataSet.valueTextColor = Color.GRAY

        val barData = BarData(barDataSet)

        binding.barChart.setFitBars(true)
        binding.barChart.data = barData
        binding.barChart.description.text = "Bar Chart"
        binding.barChart.animateY(2000)
    }
}