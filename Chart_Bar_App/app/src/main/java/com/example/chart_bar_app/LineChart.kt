package com.example.chart_bar_app

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chart_bar_app.databinding.ActivityLineChartBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*


class LineChart : AppCompatActivity() {


    private lateinit var binding: ActivityLineChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLineChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list: ArrayList<Entry> = ArrayList()

//        list.add(Entry(10f, 10f))
//        list.add(Entry(20f, 40f))
//        list.add(Entry(30f, 30f))
//        list.add(Entry(40f, 50f))
//        list.add(Entry(50f, 70f))

        for(x in (-10..10)){
            list.add(Entry(x.toFloat(), (x*x).toFloat()))
        }

        val lineDataSet = LineDataSet(list, "List")

        lineDataSet.colors = arrayListOf(R.color.teal_200)

        lineDataSet.valueTextSize = 15f

        lineDataSet.valueTextColor = Color.BLACK

        val lineData = LineData(lineDataSet)

//        binding.lineChart.data = lineData
//        binding.lineChart.description.text = "Line Chart"
//        binding.lineChart.animateX(1000)
//        binding.lineChart.invalidate()
//        binding.lineChart.setBackgroundColor(Color.parseColor("#F4F6FB"))
//        val description = Description()
//        description.text = "This is an lineChart"
//        binding.lineChart.description = description
//        binding.lineChart.description.textSize = 15f
//        binding.lineChart.description.setPosition(500f,200f)


        binding.lineChart.getDescription().setEnabled(false)
        binding.lineChart.setDrawGridBackground(false)

        val xAxis: XAxis = binding.lineChart.getXAxis()
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        val leftAxis: YAxis = binding.lineChart.getAxisLeft()
        leftAxis.setLabelCount(5, false)
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


        val rightAxis: YAxis = binding.lineChart.getAxisRight()
        rightAxis.setLabelCount(5, false)
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


        // set data

        // set data
        binding.lineChart.setData(lineData as LineData?)

        // do not forget to refresh the chart
        // holder.chart.invalidate();

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        binding.lineChart.animateX(750)
    }
}