package com.example.smamysuperalarm.activities

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.smamysuperalarm.R
import com.example.smamysuperalarm.databinding.ActivityMain4Binding
import com.example.smamysuperalarm.service.AlarmReceiver
import java.util.Calendar

// main4 and main more or less the same
class MainActivity4 : AppCompatActivity() {

    private lateinit var binding4: ActivityMain4Binding
    private lateinit var alarmManager: AlarmManager
    //private lateinit var pendingIntent: PendingIntent
    private lateinit var setAlarmButton: Button
    private lateinit var stopAlarmButton: Button
    private lateinit var correctPassword: String
    private var snoozeHandler: Handler? = null
    private var snoozeRunnable: Runnable? = null
    private lateinit var snoozeTimeSpinner: Spinner
    private lateinit var enableDefaultSleepCheckbox: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding4 = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(R.layout.activity_main4)
        setContentView(binding4.root)

        // Get the password from SharedPreferences
        val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        correctPassword = prefs.getString("alarm_code", "1234") ?: "1234"  // Fallback to "1234" if not set

        binding4.back4.setOnClickListener {
            val intent = Intent(this, MainActivity6::class.java)
            startActivity(intent)
        }

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        setAlarmButton = findViewById(R.id.deployS_button)
        stopAlarmButton = findViewById(R.id.cancelS_button)
        snoozeTimeSpinner = findViewById(R.id.snooze_time_spinner)
        enableDefaultSleepCheckbox = findViewById(R.id.enable_default_sleep_checkbox)

        // Set up the snooze time spinner
        val snoozeTimes = arrayOf("2", "4", "6", "8")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, snoozeTimes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        snoozeTimeSpinner.adapter = adapter

        setAlarmButton.setOnClickListener {
            if (enableDefaultSleepCheckbox.isChecked) {
                scheduleDefaultSleepAlarm()
            } else {
                showTimePickerDialog()
            }
        }

        stopAlarmButton.setOnClickListener {
            showAlarmOptionsDialog()
        }

    }

    private fun scheduleDefaultSleepAlarm() {
        val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val defaultSleepHours = prefs.getInt("sleep_hours", 8) // Default to 8 hours if not set
        
        // Calculate alarm time: current time + sleep hours
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, defaultSleepHours)
        
        // Schedule the alarm
        scheduleAlarm(calendar.timeInMillis)
        
        val alarmTime = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
        Toast.makeText(this, "Alarm set for $alarmTime (${defaultSleepHours} hours from now)", Toast.LENGTH_SHORT).show()
    }

    private fun showAlarmOptionsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alarm Options")

        val options = arrayOf("Stop Alarm", "Snooze")
        
        builder.setSingleChoiceItems(options, -1) { dialog, which ->
            when (which) {
                0 -> {
                    dialog.dismiss()
                    showPasswordDialog()
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
        // Get the selected snooze time from spinner
        val selectedSnoozeTime = snoozeTimeSpinner.selectedItem.toString().toInt()
        
        // Stop the current alarm
        AlarmReceiver.stopAlarm()
        
        // Show snooze confirmation with the selected time
        Toast.makeText(this, "Alarm snoozed for $selectedSnoozeTime minutes", Toast.LENGTH_SHORT).show()
        
        // Cancel any existing snooze
        snoozeHandler?.removeCallbacks(snoozeRunnable!!)
        
        // Create new snooze handler
        snoozeHandler = Handler(Looper.getMainLooper())
        snoozeRunnable = Runnable {
            // Restart the alarm after the selected time
            val intent = Intent(this, AlarmReceiver::class.java)
            sendBroadcast(intent)
            Toast.makeText(this, "Snooze time is up!", Toast.LENGTH_SHORT).show()
        }
        
        // Schedule the snooze with the selected time
        snoozeHandler?.postDelayed(snoozeRunnable!!, (selectedSnoozeTime * 60 * 1000).toLong()) // Convert minutes to milliseconds
    }

    private fun showPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Last Line of Defense")

        // Create options for the user
        val options = arrayOf("Password", "Puzzle", "Catch the Circle")
        
        builder.setSingleChoiceItems(options, -1) { dialog, which ->
            when (which) {
                0 -> {
                    dialog.dismiss()
                    showPasswordInput()
                }
                1 -> {
                    dialog.dismiss()
                    showPuzzle()
                }
                2 -> {
                    dialog.dismiss()
                    startGame()
                }
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showPasswordInput() {
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
                AlarmReceiver.stopAlarm()
                Toast.makeText(this, "Alarm stopped", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                // Password is incorrect
                Toast.makeText(this, "Incorrect password. Try again.", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showPuzzle() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Solve the Puzzle to Stop Alarm")

        // Randomly choose between math puzzle and word puzzle
        val puzzleType = (0..1).random()
        
        if (puzzleType == 0) {
            // Math puzzle
            val num1 = (1..10).random()
            val num2 = (1..10).random()
            val answer = num1 + num2

            val input = EditText(this).apply {
                hint = "What is $num1 + $num2?"
            }
            builder.setView(input)

            builder.setPositiveButton("OK") { dialog, _ ->
                val userAnswer = input.text.toString().trim()
                if (userAnswer == answer.toString()) {
                    // Puzzle solved; stop the alarm
                    AlarmReceiver.stopAlarm()
                    Toast.makeText(this, "Correct! Alarm stopped", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    // Wrong answer
                    Toast.makeText(this, "Wrong answer. Try again.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Word puzzle
            val wordPuzzles = listOf(
                Pair("white + black", "gray"),
                Pair("white + red", "pink"),
                Pair("red+blue", "magenta"),
                Pair("yellow + blue", "green"),
                Pair("yellow + red", "orange")
            )
            
            val selectedPuzzle = wordPuzzles.random()
            
            val input = EditText(this).apply {
                hint = selectedPuzzle.first
            }
            builder.setView(input)

            builder.setPositiveButton("OK") { dialog, _ ->
                val userAnswer = input.text.toString().trim().lowercase()
                if (userAnswer == selectedPuzzle.second.lowercase()) {
                    // Puzzle solved; stop the alarm
                    AlarmReceiver.stopAlarm()
                    Toast.makeText(this, "Correct! Alarm stopped", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    // Wrong answer
                    Toast.makeText(this, "Wrong answer. Try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun startGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
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
