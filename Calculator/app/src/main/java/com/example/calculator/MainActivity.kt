package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calculator.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener(){
            calculateTip()
        }
    }

    private fun calculateTip() {
        val inputFieldVal = binding.Cost.text.toString()
        val cost = inputFieldVal.toDouble()
        val tipPercentage = when(binding.radioGroup.checkedRadioButtonId){
            R.id.radioButton3 -> 0.20
            R.id.radioButton2 -> 0.15
            else -> 0.10
        }
        var tip = tipPercentage * cost
        val roundUp = binding.switch1.isChecked

        if(roundUp){
            tip = kotlin.math.ceil(tip)
        }
        val formatedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount,formatedTip)
    }
}