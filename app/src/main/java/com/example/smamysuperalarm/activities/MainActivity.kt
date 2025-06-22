package com.example.smamysuperalarm.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.smamysuperalarm.R
import com.example.smamysuperalarm.service.AlarmReceiver
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var alarmManager: AlarmManager
    //private lateinit var pendingIntent: PendingIntent
    private lateinit var setAlarmButton: Button
    private lateinit var stopAlarmButton: Button
    private var snoozeHandler: Handler? = null
    private var snoozeRunnable: Runnable? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        setAlarmButton = findViewById(R.id.set_alarm_button)
        stopAlarmButton = findViewById(R.id.stop_alarm_button)

    // functions caleled here
        setAlarmButton.setOnClickListener {
            showTimePickerDialog()
        }
        stopAlarmButton.setOnClickListener {
            showAlarmOptionsDialog()
        }
    }

    private fun showAlarmOptionsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alarm Options")

        val options = arrayOf("Stop Alarm", "Snooze (2 min)")
        
        builder.setSingleChoiceItems(options, -1) { dialog, which ->
            when (which) {
                0 -> {
                    dialog.dismiss()
                    AlarmReceiver.stopAlarm()
                }
                1 -> {
                    dialog.dismiss()
                    snoozeAlarm()
                }
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun snoozeAlarm() {
        // Stop the current alarm
        AlarmReceiver.stopAlarm()
        
        // Show snooze confirmation
        Toast.makeText(this, "Alarm snoozed for 2 minutes", Toast.LENGTH_SHORT).show()
        
        // Cancel any existing snooze
        snoozeHandler?.removeCallbacks(snoozeRunnable!!)
        
        // Create new snooze handler
        snoozeHandler = Handler(Looper.getMainLooper())
        snoozeRunnable = Runnable {
            // Restart the alarm after 2 minutes
            val intent = Intent(this, AlarmReceiver::class.java)
            sendBroadcast(intent)
            Toast.makeText(this, "Snooze time is up!", Toast.LENGTH_SHORT).show()
        }
        
        // Schedule the snooze
        snoozeHandler?.postDelayed(snoozeRunnable!!, 2 * 60 * 1000) // 2 minutes in milliseconds
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)


        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)


                if (calendar.timeInMillis <= System.currentTimeMillis()) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }


                scheduleAlarm(calendar.timeInMillis)

                val chosenTime = String.format(Locale.US,"%02d:%02d", hourOfDay, minute)
                Toast.makeText(this, "Alarm set for $chosenTime", Toast.LENGTH_SHORT).show()
            },
            currentHour,
            currentMinute,
            true
        )
        timePickerDialog.show()
    }

    private fun scheduleAlarm(triggerTimeMillis: Long) {
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent
            )
        } else {//aici mi-a dat de furca Alarm Managerul
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent
            )
        }
    }
}