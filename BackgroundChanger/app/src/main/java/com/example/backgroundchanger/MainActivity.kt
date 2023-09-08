package com.example.backgroundchanger

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.backgroundchanger.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        if(!Python.isStarted())
            Python.start(AndroidPlatform(this))

        binding.convertBtn.setOnClickListener{
            try{
                val py = Python.getInstance()
                val pyObj = py.getModule("script")
                binding.originalImg.setDrawingCacheEnabled(true)
                binding.originalImg.buildDrawingCache()
                val bitmap: Bitmap = Bitmap.createBitmap(binding.originalImg.getDrawingCache())
                val imgString = getStringImage(bitmap)

                val result = pyObj.callAttr("main", imgString, binding.url.text.toString()).toString()
                val data = Base64.decode(result, Base64.DEFAULT)
                val genImgBitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                binding.convertedImg.setImageBitmap(genImgBitmap)
                binding.convertedLayout.visibility = View.VISIBLE
            }
            catch (e: Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                Log.i("errorExp", e.message.toString())
            }
        }

    }

    private fun getStringImage(bitmap: Bitmap): String {
        val byteArray = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray)
        val imageBytes = byteArray.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }
}