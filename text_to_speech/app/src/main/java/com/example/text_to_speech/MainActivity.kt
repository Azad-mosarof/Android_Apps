package com.example.text_to_speech

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.util.Patterns
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var webView: WebView
    private var URL = "https://www.google.com"
    private lateinit var editText: EditText
    lateinit var tts: TextToSpeech
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        webView = findViewById(R.id.webView)
        editText = findViewById(R.id.editText)
        button = findViewById(R.id.button)

        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
        ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET),101)
            SPEAK("Please give the internet permission")
        }
        else{
            SPEAK("Welcome to web view application")
        }

        button.setOnClickListener(){
            val text = editText.text.toString()
            val search = "https://www.google.com/search?q=$text"
            webView.apply {
                loadUrl(search)
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            SPEAK("Permission Granted")
        }
    }

    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }
        else{
            super.onBackPressed()
        }
    }

    private fun SPEAK(text: String){
        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            if(it == TextToSpeech.SUCCESS){
                tts.language = Locale.getDefault()
                tts.setSpeechRate(1.0f)
                tts.speak(text, TextToSpeech.QUEUE_ADD,null)
            }
        })
    }
}