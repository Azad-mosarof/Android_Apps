package com.example.bluetoothfinder2

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity2 : AppCompatActivity() {

    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var bluetoothManager: BluetoothManager
    lateinit var receiver: BluetoothReceiver
    lateinit var receiver2: DiscoverabilityReceiver
    var permission: Boolean = false
    val REQUEST_ACCESS_COARSE_LOCATION = 101

    private lateinit var btOnOff: Button
    private lateinit var btDiscover: Button
    private lateinit var btPaired: Button
    private lateinit var btDiscoverDevice: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        btOnOff = findViewById(R.id.btONOFF)
        btDiscover = findViewById(R.id.btDiscover)
        btPaired = findViewById(R.id.btGetPairedDevices)
        btDiscoverDevice = findViewById(R.id.btDiscoverDevices)

        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        receiver = BluetoothReceiver()
        receiver2 = DiscoverabilityReceiver()
        enableDisableBluetooth()
        btDiscover.setOnClickListener {
            discoverability()
        }
        btPaired.setOnClickListener {
            getPairedDevices()
        }
        btDiscoverDevice.setOnClickListener{
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                when(ContextCompat.checkSelfPermission(
                    baseContext, Manifest.permission.ACCESS_COARSE_LOCATION
                )){
                    PackageManager.PERMISSION_DENIED -> androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Runtime Permission")
                        .setMessage("Give Permission")
                        .setNeutralButton("Okay", DialogInterface.OnClickListener{ dialog, which ->
                            if(ContextCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_COARSE_LOCATION)!=
                                PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),REQUEST_ACCESS_COARSE_LOCATION)
                            }
                        })
                        .show()
                        .findViewById<TextView>(android.R.id.message)!!.movementMethod = LinkMovementMethod.getInstance()

                    PackageManager.PERMISSION_GRANTED ->{
                        Log.d("discoverDevices","Permission Granted")
                    }
                }
            }
            discoverDevices()
        }
    }

    @SuppressLint("MissingPermission")
    private fun discoverDevices() {
        val filter = IntentFilter()
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(discoverDeviceReceiver,filter)
        bluetoothAdapter.startDiscovery()
    }
    private val discoverDeviceReceiver = object : BroadcastReceiver(){
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            var action = ""
            if(intent!=null){
                action = intent.action.toString()
            }
            when(action){
                BluetoothAdapter.ACTION_STATE_CHANGED ->{
                    Log.d("discoverDevices1","STATE CHANGED")
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED ->{
                    Log.d("discoverDevices2","Discovery Started")
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED ->{
                    Log.d("discoverDevices3","Disvcoery Fininshed")
                }
                BluetoothDevice.ACTION_FOUND ->{
                    val device = intent?.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    if(device!=null){
                        Log.d("discoverDevices4","${device.name}  ${device.address}")
                    }
                }
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun getPairedDevices() {
        var arr = bluetoothAdapter.bondedDevices
        Log.d("bondedDevices",arr.size.toString())
        Log.d("bondedDevices",arr.toString())
        for(device in arr){
            Log.d("bondedDevices",device.name+"   "+device.address + "   "+device.bondState)
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableDisableBluetooth() {
        btOnOff.setOnClickListener {
            if (!bluetoothAdapter.isEnabled) {
                bluetoothAdapter.enable()
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivity(intent)

                val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
                registerReceiver(receiver, intentFilter)
            }
            if (bluetoothAdapter.isEnabled) {
                bluetoothAdapter.disable()

                val intentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
                registerReceiver(receiver, intentFilter)
            }
        }
    }


    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.BASE)
    private fun discoverability() {
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 20)
        startActivity(discoverableIntent)

        val intentFilter = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        registerReceiver(receiver2, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

}