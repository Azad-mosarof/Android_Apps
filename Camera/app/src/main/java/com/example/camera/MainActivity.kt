package com.example.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.camera.databinding.ActivityMainBinding
import java.io.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var dirPath = Environment.getExternalStorageDirectory().absolutePath + "/" + "Camera" + "/"

    var outputStream: OutputStream? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE),21)
        }else{
            binding.button.isEnabled = false

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)
            } else
                binding.button.isEnabled = true

            binding.button.setOnClickListener{
                var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent,101)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 101){
            var img: Bitmap? = data?.getParcelableExtra<Bitmap>("data")
            binding.imageView.setImageBitmap(img)
            saveImage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            binding.button.isEnabled = true
        }
    }


    private fun saveImage() {
        val dir = File(Environment.getExternalStorageDirectory(), "SaveImage")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val drawable = binding.imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val file = File(dir, System.currentTimeMillis().toString() + ".png")
        try {
            outputStream = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
            Toast.makeText(this@MainActivity, "Successfuly Saved", Toast.LENGTH_SHORT).show()
            outputStream?.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            outputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
