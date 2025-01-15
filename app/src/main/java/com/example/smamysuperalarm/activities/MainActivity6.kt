package com.example.smamysuperalarm.activities

import android.app.AlarmManager
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.smamysuperalarm.R
import com.example.smamysuperalarm.databinding.ActivityMain6Binding
import com.example.smamysuperalarm.utils.Util.setupDialog
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar


class MainActivity6 : AppCompatActivity() {
    //private lateinit var pick: MaterialTimePicker
    //private lateinit var cal: Calendar
    //private lateinit var pendingIntent: PendingIntent
    private val binding6: ActivityMain6Binding by lazy{
        ActivityMain6Binding.inflate(layoutInflater)
    }
    private lateinit var alarmManager: AlarmManager
    /*private val timeDialogLayout: Dialog by lazy{
        Dialog(this, R.style.DialogCustomTheme).apply { setupDialog(R.layout.time_dialog_layout) }
    }*/
   //val okButton: Button = Dialog.findViewById(R.id.ok_button)
    //val cancelButton: Button = Dialog.findViewById(R.id.backDiag)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding6.root)
        setContentView(R.layout.activity_main6)

        val continueButton: Button = findViewById(R.id.add_alarm)
        continueButton.setOnClickListener {
            // Navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

       binding6.back6.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        val continueButtonSalarm: Button = findViewById(R.id.add_Salarm)
        continueButtonSalarm.setOnClickListener {
            val intent = Intent(this, MainActivity4::class.java)
            startActivity(intent)
            finish()
        }
        /*
                binding6.addAlarm.setOnClickListener {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }*/
    }
}