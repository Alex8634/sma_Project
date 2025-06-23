package com.example.wearapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Wearable
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

class MainActivityWearableWorks : Activity() {

    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WearTest", "onCreate called")
        FirebaseApp.initializeApp(this)
        val app = FirebaseApp.getInstance()
        Log.d("WearTest", "FirebaseApp name: ${app.name}, options: ${app.options.toString()}")
        Log.d("WearTest", "FirebaseApp initialized")

        setContentView(android.R.layout.simple_list_item_1) // Simplu, nu ne trebuie UI complex

        // Test message - send immediately to test communication
        sendMessageToPhone("105")

        if (checkSelfPermission(android.Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED) {
            Log.d("WearTest", "BODY_SENSORS permission already granted")
            startHeartRateMonitoring()
        } else {
            Log.d("WearTest", "Requesting BODY_SENSORS permission")
            requestPermissions(arrayOf(android.Manifest.permission.BODY_SENSORS), 100)
        }
    }

    override fun onStart() {
        super.onStart()
        // Start listening for messages (in case we need to receive any)
        Wearable.getMessageClient(this).addListener { messageEvent ->
            Log.d("WearTest", "Received message: ${messageEvent.path}")
        }
    }

    override fun onStop() {
        super.onStop()
        // Stop listening for messages
        Wearable.getMessageClient(this).removeListener { }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        Log.d("WearTest", "onRequestPermissionsResult called: requestCode=$requestCode, grantResults=${grantResults.joinToString()}")
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("WearTest", "BODY_SENSORS permission granted in onRequestPermissionsResult")
            startHeartRateMonitoring()
        } else {
            Log.d("WearTest", "BODY_SENSORS permission denied in onRequestPermissionsResult")
        }
    }

    private fun startHeartRateMonitoring() {
        Log.d("WearTest", "startHeartRateMonitoring called")
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        if (heartRateSensor == null) {
            Log.e("WearTest", "No heart rate sensor available on this device.")
        }
        heartRateSensor?.let {
            sensorManager.registerListener(sensorListener, it, SensorManager.SENSOR_DELAY_NORMAL)
            Log.d("WearTest", "Heart rate monitoring started")
        } ?: Log.e("WearTest", "Nu există senzor de puls pe acest dispozitiv.")
    }

    private val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_HEART_RATE) {
                val bpm = event.values.firstOrNull()?.toInt() ?: return
                Log.d("WearTest", "Puls detectat: $bpm BPM")
                // Send BPM to Firebase
                val userId = "testuser" // TODO: Replace with real user ID if available
                Log.d("WearTest", "Calling sendHeartRateToFirebase($userId, $bpm)")
                sendHeartRateToFirebase(userId, bpm)
                // Optionally, keep sending to phone if needed
                // sendHeartRateToPhone(bpm)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Nu e necesar aici
        }
    }

    private fun sendHeartRateToPhone(bpm: Int) {
        val message = bpm.toString()
        sendMessageToPhone(message)
    }

    private fun sendHeartRateToFirebase(userId: String, bpm: Int) {
        val database = FirebaseDatabase.getInstance("https://superalarm-e9b39-default-rtdb.europe-west1.firebasedatabase.app/")
        val bpmRef = database.getReference("users").child(userId).child("bpm")
        bpmRef.setValue(bpm)
            .addOnSuccessListener {
                Log.d("FirebaseWrite", "Successfully wrote BPM $bpm to Firebase for user $userId")
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseWrite", "Failed to write BPM to Firebase: ", e)
            }
        Log.d("FirebaseWrite", "setValue called on bpmRef: $bpmRef")
    }

    private fun sendMessageToPhone(message: String) {
        Thread {
            try {
                val nodes = Tasks.await(Wearable.getNodeClient(this).connectedNodes)
                Log.d("WearTest", "Found ${nodes.size} connected nodes")

                for (node in nodes) {
                    Log.d("WearTest", "Trimit mesaj către ${node.displayName} (${node.id})")
                    Wearable.getMessageClient(this)
                        .sendMessage(node.id, "/heart_rate", message.toByteArray())
                        .addOnSuccessListener {
                            Log.d("WearTest", "Mesaj TRIMIS cu succes: $message către ${node.displayName}")
                        }
                        .addOnFailureListener {
                            Log.e("WearTest", "Eroare la trimitere: ${it.message}")
                        }
                }

                if (nodes.isEmpty()) {
                    Log.w("WearTest", "No connected nodes found!")
                }
            } catch (e: Exception) {
                Log.e("WearTest", "Error sending message: ${e.message}")
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(sensorListener)
    }
}