package com.example.smamysuperalarm.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri // ne folosim de tipul URI ca sa obtinem fisierele de tip sunet pt alarma

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        var ringtone: Ringtone? = null
    }
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Alarm went off!", Toast.LENGTH_SHORT).show()

        var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM) //sunet de alarma
        //var alarmUri = RingtoneManager.getDefaultUri(music1)
        if (alarmUri == null) {

            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)// sunet de notificare
        }

        val ringtoneInstance = alarmUri?.let { RingtoneManager.getRingtone(context, it) }
        if (ringtoneInstance != null) {
            ringtoneInstance.play() //canta
            ringtone = ringtoneInstance
        } else {//
        }
    }
}