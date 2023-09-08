package com.example.capturevideo

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.MediaController
import androidx.core.app.ActivityCompat
import com.example.capturevideo.databinding.ActivityMainBinding
import com.mvp.handyopinion.URIPathHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var ourRequestCode:Int  = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button2.isEnabled = false

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),101)
        }
        else
            binding.button2.isEnabled = true

        binding.button2.setOnClickListener{
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            if(intent.resolveActivity(packageManager) != null){
                startActivityForResult(intent,ourRequestCode)
            }
        }

        //now set up the media controller for play pause previous and next
        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ourRequestCode && resultCode == RESULT_OK) {
            //get data from uri
            val videoUri = data?.data
            saveVideo(videoUri!!)
            binding.videoView.setVideoURI(videoUri)
            binding.videoView.start()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == ourRequestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            binding.button2.isEnabled = true
        }
    }

    fun saveVideo(videoUri: Uri){
        val uriHelper = URIPathHelper()
        val filepath = uriHelper.getPath(this,videoUri)
        Log.i("file_name" , filepath!!)
    }
}