package com.example.chart_bar_app

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chart_bar_app.databinding.ActivityBarBinding
import com.example.chart_bar_app.databinding.ActivityPieBinding
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class PieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list: ArrayList<PieEntry> = ArrayList()

        list.add(PieEntry(10f, 10f))
        list.add(PieEntry(20f, 40f))
        list.add(PieEntry(30f, 30f))
        list.add(PieEntry(40f, 50f))
        list.add(PieEntry(50f, 70f))

        val pieDataSet = PieDataSet(list, "List")

        pieDataSet.colors = arrayListOf(R.color.purple_200, R.color.purple_700, R.color.purple_500,
            R.color.teal_200)

        pieDataSet.valueTextSize = 15f

        pieDataSet.valueTextColor = Color.BLACK

        val pieData = PieData(pieDataSet)

        binding.pieChart.data = pieData

        binding.pieChart.description.text = "Pie Chart"
        binding.pieChart.centerText = "List"
        binding.pieChart.animateY(2000)
    }
}