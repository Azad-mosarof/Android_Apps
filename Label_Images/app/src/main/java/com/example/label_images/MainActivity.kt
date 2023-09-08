package com.example.label_images

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import com.example.label_images.databinding.ActivityMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class MainActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectImage.setOnClickListener{
            binding.display.text = ""
            var intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"Select Image"),1)
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET, Manifest.permission.CAMERA),101)
        }
        else{
            binding.btCap.setOnClickListener{
                binding.display.text = ""
                var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent,11)
            }

            binding.labelBtn.setOnClickListener{
                binding.display.text = ""
                val bitmap:Bitmap = (binding.imageView.drawable as BitmapDrawable).bitmap
                val image = InputImage.fromBitmap(bitmap, 0)
                labelImage(image)
            }
        }
    }

    private fun labelImage(image:InputImage){
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        labeler.process(image)
            .addOnSuccessListener { labels ->
                var count = 0
                for (label in labels) {
                    count+=1
                    val text = label.text
                    val confidence = label.confidence
                    val index = label.index
                    binding.display.text = "${binding.display.text}\n $count. Class: $text, Confidence: $confidence, index: $index"
                }
            }
            .addOnFailureListener { e ->
                binding.display.text = "Sorry, Image Labeling is Failed!!"
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            if(data != null){
                binding.imageView.setImageURI(data.data)
            }
        }
        if(requestCode == 11){
            var img = data?.getParcelableExtra<Bitmap>("data")
            binding.imageView.setImageBitmap(img)
        }
    }
}