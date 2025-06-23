package com.example.smamysuperalarm.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.util.Log
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        private var ringtone: Ringtone? = null
        private var wakeLock: PowerManager.WakeLock? = null
        private const val TAG = "AlarmReceiver"
        
        fun stopAlarm() {
            try {
                ringtone?.stop()
                ringtone = null
                wakeLock?.release()
                wakeLock = null
                // Detach Firebase BPM listener
                WearableListenerService.stopFirebaseBpmListener()
                Log.d(TAG, "Alarm stopped")
            } catch (e: Exception) {
                Log.e(TAG, "Error stopping alarm: ${e.message}")
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        try {
            // Acquire wake lock to keep the alarm playing
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "Alarmify::AlarmWakeLock"
            ).apply {
                acquire(10*60*1000L) // 10 minutes timeout
            }

            // Get the alarm sound
            var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }

            // Create and configure the ringtone
        val ringtoneInstance = alarmUri?.let { RingtoneManager.getRingtone(context, it) }
        if (ringtoneInstance != null) {
                // Configure audio attributes for better reliability
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ringtoneInstance.audioAttributes = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                }

                // Set looping to true to ensure continuous playback
                ringtoneInstance.isLooping = true
                
                // Start playing
                ringtoneInstance.play()
            ringtone = ringtoneInstance
                
                Log.d(TAG, "Alarm started successfully")
                // Attach Firebase BPM listener if last line of defense is Wearable
                val prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                val lastLineOfDefense = prefs.getString("last_line_of_defense", "")
                if (lastLineOfDefense == "Wearable") {
                    Log.d(TAG, "Attaching Firebase BPM listener from AlarmReceiver")
                    WearableListenerService.startFirebaseBpmListener("testuser", 100) {
                        stopAlarm()
                    }
                }
            } else {
                Log.e(TAG, "Failed to create ringtone instance")
                Toast.makeText(context, "Failed to start alarm", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in alarm receiver: ${e.message}")
            Toast.makeText(context, "Error starting alarm", Toast.LENGTH_SHORT).show()
        }
    }
}