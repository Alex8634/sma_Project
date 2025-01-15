package com.example.smamysuperalarm.activities

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.smamysuperalarm.R
import com.example.smamysuperalarm.databinding.ActivityMain4Binding
import com.example.smamysuperalarm.service.AlarmReceiver
import java.util.Calendar


class MainActivity4 : AppCompatActivity() {

    private lateinit var binding4: ActivityMain4Binding
    private lateinit var alarmManager: AlarmManager
    //private lateinit var pendingIntent: PendingIntent
    private lateinit var setAlarmButton: Button
    private lateinit var stopAlarmButton: Button
    private val correctPassword = "1234"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding4 = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(R.layout.activity_main4)
        setContentView(binding4.root)

        binding4.back4.setOnClickListener {
            val intent = Intent(this, MainActivity6::class.java)
            startActivity(intent)
        }

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        setAlarmButton = findViewById(R.id.deployS_button)
        stopAlarmButton = findViewById(R.id.cancelS_button)

        setAlarmButton.setOnClickListener {
            showTimePickerDialog()
        }

        stopAlarmButton.setOnClickListener {

            showPasswordDialog()
        }

    }
    private fun showPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Password to Stop Alarm")

        val input = EditText(this).apply {
            hint = "Password"
        }
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val enteredPassword = input.text.toString().trim()
            if (enteredPassword == correctPassword) {
                // Password is correct; stop the alarm
                AlarmReceiver.ringtone?.stop()
                Toast.makeText(this, "Alarm stopped", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                // Password is incorrect
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
            }
        }


        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
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

                // Schedule the alarm for that time
                scheduleAlarm(calendar.timeInMillis)

                val chosenTime = String.format("%02d:%02d", hourOfDay, minute)
                Toast.makeText(this, "Alarm set for $chosenTime", Toast.LENGTH_SHORT).show()
            },
            currentHour,
            currentMinute,
            true
        )


        timePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    /*private fun scheduleAlarm(secondsFromNow: Int) {
        // Get the current time and add desired seconds to schedule the alarm
        val calendar = Calendar.getInstance().apply {
            add(Calendar.SECOND, secondsFromNow)
        }
        val triggerTimeMillis = calendar.timeInMillis

        // Create an Intent pointing to the BroadcastReceiver
        val intent = Intent(this, AlarmReceiver::class.java)

        // Create the PendingIntent
        pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )

        // Schedule the alarm
        // For an exact alarm, use setExact(...) on Android versions that support it.
        // If you need it to wake up the device, use RTC_WAKEUP or ELAPSED_REALTIME_WAKEUP.
        if (alarmManager.canScheduleExactAlarms()) {
            scheduleExactAlarm(triggerTimeMillis, pendingIntent)
        }  else {
        // Fallback for older versions (API < 31) — just schedule the alarm
        scheduleExactAlarm(triggerTimeMillis, pendingIntent)
        }
    }*/
    private fun scheduleExactAlarm(triggerTimeMillis: Long, pendingIntent: PendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent
            )
        }
    }

    private fun scheduleAlarm(triggerTimeMillis: Long) {
        //val triggerTimeMillis = System.currentTimeMillis() + (secondsFromNow * 1000L)
        val intent = Intent(this, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        PendingIntent.FLAG_IMMUTABLE
                    else 0)
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent
            )
        }
    }

}
