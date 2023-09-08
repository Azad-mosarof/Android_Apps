package com.example.bluetoothdiscovery

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val enableRequestCode = 101
    private lateinit var findDevice: Button
    private lateinit var devices: TextView
    private lateinit var discoverBtn: Button
    private lateinit var discoverDevices: TextView
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothManager: BluetoothManager

    lateinit var receiver: BluetoothReceiver
    lateinit var receiver2: DiscoverabilityReceiver

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findDevice = findViewById(R.id.find_device)
        devices = findViewById(R.id.devices_list)
        discoverBtn = findViewById(R.id.discoverBtn)
        discoverDevices = findViewById(R.id.discoverDevices)

        receiver = BluetoothReceiver()
        receiver2 = DiscoverabilityReceiver()

        fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)
        packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH) }?.also {
            Toast.makeText(this, R.string.bluetooth_not_supported, Toast.LENGTH_SHORT).show()
            finish()
        }
        packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) }?.also {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show()
            finish()
        }

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_SCAN) !=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_SCAN),22)
        }

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),22)
        }


        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        if(!bluetoothAdapter.isEnabled){
            bluetoothAdapter.enable()
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT
                    ,Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.ACCESS_BACKGROUND_LOCATION),11)
            }
        }

        findDevice.setOnClickListener{
            devices.text = ""
            if (!bluetoothAdapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, enableRequestCode)
            }
            else{
                //find pair devices
                val pairDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
                pairDevices?.forEach{ device ->
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address  //Mac address
                    devices.text = "${devices.text}\nDevice Name:$deviceName, Mac Address: $deviceHardwareAddress"
                }
            }
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            when(ContextCompat.checkSelfPermission(
                baseContext,Manifest.permission.ACCESS_COARSE_LOCATION
            )){
                PackageManager.PERMISSION_DENIED -> androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Runtime Permission")
                    .setMessage("Give Permission")
                    .setNeutralButton("Okay") { dialog, which ->
                        if (ContextCompat.checkSelfPermission(
                                baseContext,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) !=
                            PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                                1
                            )
                        }
                    }
                    .show()
//                        .findViewById<TextView>(androidx.appcompat.R.id.message)!!.movementMethod = LinkMovementMethod.getInstance()

                PackageManager.PERMISSION_GRANTED ->{
                    Log.d("discoverDevices","Permission Granted")
                }
            }
        }

        discoverBtn.setOnClickListener{
            discoverability()
//            bluetoothAdapter.bluetoothLeScanner.toString()
//            val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
//            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 20)
//            startActivity(discoverableIntent)
            discoverDevices()
        }
    }

    @SuppressLint("MissingPermission")
    private fun discoverDevices() {
        val filter = IntentFilter()
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        registerReceiver(discoverDeviceReceiver,filter)
        bluetoothAdapter.startDiscovery()
    }
    private val discoverDeviceReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission", "SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {
            var action: String = ""
            if (intent != null) action = intent.action.toString()
            when (action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    Log.d("discoverDevices1", "STATE CHANGED")
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Log.d("discoverDevices2", "Discovery Started")
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.d("discoverDevices3", "Disvcoery Fininshed")
                }
                BluetoothDevice.ACTION_FOUND -> {
                    Toast.makeText(context,"okey",Toast.LENGTH_SHORT).show()
                    discoverDevices.text = ""
                    val device = intent?.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    if(device!=null){
                        Log.d("discoverDevices4","${device.name}  ${device.address}")
                        val deviceName = device.name
                        val deviceHardwareAddress = device.address
                        discoverDevices.text = "${discoverDevices.text}\nDevice Name:$deviceName, Mac Address: $deviceHardwareAddress"
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private fun discoverability() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_ADVERTISE)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_ADVERTISE),25)
        }
        else{
            val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 20)
            startActivity(discoverableIntent)

            val intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(receiver2, intentFilter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

}