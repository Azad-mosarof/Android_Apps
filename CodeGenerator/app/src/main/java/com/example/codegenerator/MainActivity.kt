package com.example.codegenerator

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.codegenerator.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val RQ_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch {
            try {
                genIMage()
            }
            catch (e: Exception){
                Log.i("response", e.message.toString())
            }
        }

        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO),102)
            Toast.makeText(this,"Please give the Microphone Access", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this,"Please tap on the microphone to say something", Toast.LENGTH_SHORT).show()
        }

        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET),102)
            Toast.makeText(this,"Please give the Internet Access", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this,"Internet access is granted", Toast.LENGTH_SHORT).show()
        }

        binding.microPhone.setOnClickListener{
            askSpeechInput()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RQ_CODE && resultCode == RESULT_OK) {
            val result: ArrayList<String>? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            Log.i("response", result?.get(0).toString())
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response: String = openAiResponse().getResponse(result?.get(0))
                    runOnUiThread {
                            binding.codePlate.text = response
                            Log.i("response", response)
                        }
                }
                catch (e: Exception){
                    runOnUiThread {
                        Log.i("response", e.message.toString())
                        binding.codePlate.text = e.message.toString()
                    }
                }
            }
        }
    }

    private fun askSpeechInput(){
        val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something please!")
        try {
            this.startActivityForResult(i,RQ_CODE)
        } catch (e: Exception) {
            // on below line we are displaying error message in toast
            Toast
                .makeText(
                    this, " " + e.message,
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }

    private fun genIMage(){
        val apiKey = "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"
        val model = "image-alpha-001"
        val prompt = "Generate an image of a cat"
        val options = mapOf(
            "size" to "256x256",
            "response_format" to "url"
        )

        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("model", model)
            .add("prompt", prompt)
            .add("size", options["size"])
            .add("response_format", options["response_format"])
            .build()

        val request = Request.Builder()
            .url("https://api.openai.com/v1/images/generations")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("response", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.i("response",response.code().toString())
                    Log.i("response",response.body().toString())
                    return
                }
            }
        })

    }
}