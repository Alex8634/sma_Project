package com.exemplu.wearapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.wearable.Wearable

class MainActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(android.R.layout.simple_list_item_1) // Simplu, nu ne trebuie UI complex

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BODY_SENSORS), 100)
        } else {
            startHeartRateMonitoring()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startHeartRateMonitoring()
        }
    }

    private fun startHeartRateMonitoring() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

        heartRateSensor?.let {
            sensorManager.registerListener(sensorListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: Log.e("Wear", "Nu există senzor de puls pe acest dispozitiv.")
    }

    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_HEART_RATE) {
                val bpm = event.values.firstOrNull()?.toIntOrNull() ?: return
                Log.d("Wear", "Puls detectat: $bpm BPM")
                if (bpm > 100) {
                    sendHeartRateToPhone(bpm)
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Nu e necesar aici
        }
    }

    private fun sendHeartRateToPhone(bpm: Int) {
        val message = bpm.toString()
        val nodeClient = Wearable.getNodeClient(this)
        nodeClient.connectedNodes.addOnSuccessListener { nodes ->
            for (node in nodes) {
                Wearable.getMessageClient(this)
                    .sendMessage(node.id, "/heart_rate", message.toByteArray())
                    .addOnSuccessListener {
                        Log.d("Wear", "Trimis BPM: $bpm către ${node.displayName}")
                    }
                    .addOnFailureListener {
                        Log.e("Wear", "Eroare la trimiterea pulsului", it)
                    }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(sensorListener)
    }
}