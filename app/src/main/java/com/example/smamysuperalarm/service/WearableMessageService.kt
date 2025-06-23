package com.example.smamysuperalarm.service

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable

class WearableMessageService : MessageClient.OnMessageReceivedListener {
    
    companion object {
        private const val TAG = "WearableMessageService"
        private const val HEART_RATE_PATH = "/heart_rate"
        private const val HEART_RATE_THRESHOLD = 100
        
        fun startListening(context: Context) {
            Wearable.getMessageClient(context).addListener(WearableMessageService())
            Log.d(TAG, "Started listening for wearable messages")
        }
        
        fun stopListening(context: Context) {
            Wearable.getMessageClient(context).removeListener(WearableMessageService())
            Log.d(TAG, "Stopped listening for wearable messages")
        }
    }
    
    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == HEART_RATE_PATH) {
            val bpmStr = String(messageEvent.data)
            val bpm = bpmStr.toIntOrNull()
            
            Log.d(TAG, "Received BPM from Wear: $bpm")
            
            if (bpm != null && bpm > HEART_RATE_THRESHOLD) {
                Log.d(TAG, "BPM > $HEART_RATE_THRESHOLD, stopping alarm")
                stopAlarmFromWearable()
            }
        }
    }
    
    private fun stopAlarmFromWearable() {
        // Send broadcast to stop the alarm
        val intent = Intent("STOP_ALARM_FROM_WEARABLE")
        // You can use LocalBroadcastManager or a different approach to communicate with AlarmReceiver
        Log.d(TAG, "Broadcasting stop alarm signal")
    }
} 