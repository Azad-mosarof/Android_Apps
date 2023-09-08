package com.example.dise_roller

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    //you can consider onCreate function as main function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button:Button = findViewById((R.id.button))
        val imageView:ImageView = findViewById(R.id.imageView)

        val dice = DiceRoll(6)
        imageView.setImageResource(R.drawable.dice_4)

        button.setOnClickListener{
            var rollVal:Int = dice.roll()
            val drawableRes = when(rollVal){
                1 -> R.drawable.dice_1
                2 -> R.drawable.dice_2
                3 -> R.drawable.dice_3
                4 -> R.drawable.dice_4
                5 -> R.drawable.dice_5
                else -> R.drawable.dice_6
            }
            imageView.setImageResource(drawableRes)
        }
    }
}

class DiceRoll(private var side:Int){
    fun roll():Int{
        return (1..side).random()
    }
}