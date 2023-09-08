package com.example.fingerprintauth

import android.Manifest
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.security.Key
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class MainActivity : AppCompatActivity() {

    lateinit var fm: FingerprintManager
    lateinit var km: KeyguardManager

    lateinit var keyStore: KeyStore
    lateinit var keyGenerator: KeyGenerator
    var keyName = "my_key"

    lateinit var cipher: Cipher
    lateinit var cryptoObject: FingerprintManager.CryptoObject

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        km = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        fm = getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager

        if(!km.isKeyguardSecure){
            Toast.makeText(this,"Lock Screen Security not enabled in setting",Toast.LENGTH_LONG).show()
        }
        if(!fm.hasEnrolledFingerprints()){
            Toast.makeText(this,"Register at-least one fingerprint in settings",Toast.LENGTH_LONG).show()
        }
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.USE_FULL_SCREEN_INTENT),101)
        }
        else{
            validateFingerPrint()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            validateFingerPrint()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun validateFingerPrint(){
        try{
            keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore")
            keyStore.load(null)
            keyGenerator.init(KeyGenParameterSpec.Builder(keyName,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build())
            keyGenerator.generateKey()
        }catch (e: Exception){}
        //Initialization of cryptography
        if(initCipher()){
            cipher.let {
                cryptoObject = FingerprintManager.CryptoObject(it)
            }
        }
        var helper = FingerPrintHelper(this)
        if(fm != null && cryptoObject != null){
            helper.startAuth(fm, cryptoObject)
        }
    }

    private fun initCipher(): Boolean{
        try{
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"
                +KeyProperties.BLOCK_MODE_CBC+"/"
                +KeyProperties.ENCRYPTION_PADDING_PKCS7)
        }catch (e: Exception){}

        return try {
            keyStore.load(null)
            val key: SecretKey = keyStore.getKey(keyName,null) as SecretKey
            cipher.init(Cipher.ENCRYPT_MODE,key)
            true
        }catch (e: Exception){
            false
        }
    }
}