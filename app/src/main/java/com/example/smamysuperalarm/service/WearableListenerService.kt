package com.example.smamysuperalarm.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.firebase.database.*

class WearableListenerService : Service(), MessageClient.OnMessageReceivedListener {
    
    companion object {
        private const val TAG = "WearableListenerService"
        private const val HEART_RATE_PATH = "/heart_rate"
        private const val HEART_RATE_THRESHOLD = 100
        private var firebaseListener: ValueEventListener? = null
        private var firebaseRef: DatabaseReference? = null
        private var isActive = false
        
        fun startListening(context: android.content.Context) {
            if (!isActive) {
                Wearable.getMessageClient(context).addListener(WearableListenerService())
                isActive = true
                Log.d(TAG, "Started listening for wearable messages")
            }
        }
        
        fun stopListening(context: android.content.Context) {
            if (isActive) {
                Wearable.getMessageClient(context).removeListener(WearableListenerService())
                isActive = false
                Log.d(TAG, "Stopped listening for wearable messages")
            }
        }

        fun startFirebaseBpmListener(userId: String, threshold: Int = HEART_RATE_THRESHOLD, stopAlarm: () -> Unit) {
            val dbUrl = "https://superalarm-e9b39-default-rtdb.europe-west1.firebasedatabase.app/"
            firebaseRef = FirebaseDatabase.getInstance(dbUrl)
                .getReference("users").child(userId).child("bpm")
            Log.d("FirebasePhone", "Attaching Firebase BPM listener to $dbUrl/users/$userId/bpm")
            firebaseListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val bpm = snapshot.getValue(Int::class.java) ?: return
                    Log.d("FirebasePhone", "BPM value from Firebase: $bpm")
                    if (bpm > threshold) {
                        Log.d("FirebasePhone", "BPM $bpm > $threshold, stopping alarm")
                        stopAlarm()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebasePhone", "Firebase listener cancelled: ${error.message}")
                }
            }
            firebaseRef?.addValueEventListener(firebaseListener!!)
        }

        fun stopFirebaseBpmListener() {
            firebaseListener?.let { firebaseRef?.removeEventListener(it) }
            firebaseListener = null
            firebaseRef = null
        }
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "WearableListenerService started")
        return START_STICKY
    }
    
    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d(TAG, "Received message: ${messageEvent.path}")
        
        if (messageEvent.path == "/heart_rate") {
            val bpmStr = String(messageEvent.data)
            val bpm = bpmStr.toIntOrNull()
            
            Log.d(TAG, "Received BPM from Wear: $bpm")
            
            if (bpm != null && bpm > 100) {
                Log.d(TAG, "BPM > 100, stopping alarm from wearable")
                AlarmReceiver.stopAlarm()
                // Note: We can't show Toast here since this is not in an Activity context
            }
        }
    }
} 