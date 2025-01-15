package com.example.smamysuperalarm.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        var ringtone: Ringtone? = null
    }
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "Alarm went off!")
        Toast.makeText(context, "Alarm went off!", Toast.LENGTH_SHORT).show()


        var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        if (alarmUri == null) {

            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }

        val ringtoneInstance = alarmUri?.let { RingtoneManager.getRingtone(context, it) }
        if (ringtoneInstance != null) {
            ringtoneInstance.play()
            ringtone = ringtoneInstance
        } else {//
        }
    }
}