package com.example.smamysuperalarm.activities

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smamysuperalarm.R
import com.example.smamysuperalarm.databinding.ActivityMain4Binding
import com.example.smamysuperalarm.service.AlarmReceiver
import com.example.smamysuperalarm.service.WearableListenerService
import java.util.Calendar
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.MessageClient.OnMessageReceivedListener
import com.google.android.gms.wearable.Node
import com.google.firebase.FirebaseApp

// main4 and main more or less the same
class MainActivity4 : AppCompatActivity(), OnMessageReceivedListener {

    private lateinit var binding4: ActivityMain4Binding
    private lateinit var alarmManager: AlarmManager
    private lateinit var setAlarmButton: Button
    private lateinit var stopAlarmButton: Button
    private lateinit var correctPassword: String
    private var snoozeHandler: Handler? = null
    private var snoozeRunnable: Runnable? = null
    private lateinit var snoozeTimeSpinner: Spinner
    private lateinit var enableDefaultSleepCheckbox: CheckBox
    private lateinit var lastLineOfDefenseSpinner: Spinner
    private var isFirebaseBpmListenerActive = false
    private val userId = "basic"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding4 = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(R.layout.activity_main4)
        setContentView(binding4.root)

        // Get the password from SharedPreferences
        val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        correctPassword = prefs.getString("alarm_code", "1234") ?: "1234" 
        binding4.back4.setOnClickListener {
            val intent = Intent(this, MainActivity6::class.java)
            startActivity(intent)
        }

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        setAlarmButton = findViewById(R.id.deployS_button)
        stopAlarmButton = findViewById(R.id.cancelS_button)
        snoozeTimeSpinner = findViewById(R.id.snooze_time_spinner)
        enableDefaultSleepCheckbox = findViewById(R.id.enable_default_sleep_checkbox)
        lastLineOfDefenseSpinner = findViewById(R.id.last_line_of_defense_spinner)

        // Set up the snooze time spinner
        val snoozeTimes = arrayOf("2", "4", "6", "8")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, snoozeTimes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        snoozeTimeSpinner.adapter = adapter

        setAlarmButton.setOnClickListener {
            // Check if wearable is selected in the spinner
            val selectedDefense = lastLineOfDefenseSpinner.selectedItem.toString()
            Log.d("FirebasePhone", "lastLineOfDefenseSpinner selected: $selectedDefense")
            val isWearableEnabled = selectedDefense == "Wearable"
            
            // Start or stop wearable listener service
            if (isWearableEnabled) {
                WearableListenerService.startListening(this)
                Toast.makeText(this, "Wearable monitoring enabled", Toast.LENGTH_SHORT).show()
            } else {
                WearableListenerService.stopListening(this)
            }
            
            // Request body sensors permission if wearable is enabled
            if (isWearableEnabled) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BODY_SENSORS)
                    != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.BODY_SENSORS), 100)
                    Toast.makeText(this, "Body sensors permission needed for wearable functionality", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }
            
            if (enableDefaultSleepCheckbox.isChecked) {
                scheduleDefaultSleepAlarm()
            } else {
                showTimePickerDialog()
            }
        }

        stopAlarmButton.setOnClickListener {
            showAlarmOptionsDialog()
        }

        // Add test button for wearable communication
        findViewById<Button>(R.id.test_wearable_button).setOnClickListener {
            testWearableMessage()
        }

    }
    override fun onStart() {
        super.onStart()
        Wearable.getMessageClient(this).addListener(this)
    }

    override fun onStop() {
        Wearable.getMessageClient(this).removeListener(this)
        super.onStop()
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d("WearDebug", "message from watch received: ${messageEvent.path}")

        when (messageEvent.path) {
            "/heart_rate" -> {
                val bpmStr = String(messageEvent.data, Charsets.UTF_8)
                val bpm = bpmStr.toIntOrNull()
                Log.d("WearDebug", "current BPM: $bpm")

                if (bpm != null && bpm > 100) {
                    runOnUiThread {
                        Toast.makeText(this, "BPM > 100 – SuperAlarma se opreste!", Toast.LENGTH_LONG).show()
                        AlarmReceiver.stopAlarm()
                    }
                }
            }
            "/test_connection_response" -> {
                val response = String(messageEvent.data, Charsets.UTF_8)
                Log.d("WearableTest", "Received test connection response: $response")
                runOnUiThread {
                    Toast.makeText(this, "Wearable communication confirmed: $response", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Log.d("WearDebug", "Unknown message path: ${messageEvent.path}")
            }
        }
    }

    //permission for watch usage
    override fun onRequestPermissionsResult(
        requestCode: Int, 
        permissions: Array<out String>, 
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Body sensors permission granted", Toast.LENGTH_SHORT).show()
            if (enableDefaultSleepCheckbox.isChecked) {
                scheduleDefaultSleepAlarm()
            } else {
                showTimePickerDialog()
            }
        } else {
            Toast.makeText(this, "Body sensors permission denied. Wearable functionality disabled.", Toast.LENGTH_LONG).show()
            WearableListenerService.stopListening(this)
        }
    }

    fun testWearableMessage() {
        val selectedDefense = lastLineOfDefenseSpinner.selectedItem.toString()
        if (selectedDefense == "Wearable") {
            // Check if wearable devices are actually connected
            checkWearableConnection()
        } else {
            Toast.makeText(this, "Please select 'Wearable' in the spinner first", Toast.LENGTH_SHORT).show()
        }
    }

    // see if watcth is connected
    private fun checkWearableConnection() {
        Wearable.getNodeClient(this).connectedNodes.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val nod = task.result
                if (nod.isNotEmpty()) {
                    Log.d("WearableTest", "Connected to watch!")
                    runOnUiThread {
                        Toast.makeText(this, "Watch is up!: ${nod.size}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("WearableTest", "No watch found")
                    runOnUiThread {
                        Toast.makeText(this, "no watch found:", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Log.e("WearableTest", "Failed to get connected nod")
                runOnUiThread {
                    Toast.makeText(this, "Failed to check wearable connection", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun scheduleDefaultSleepAlarm() {
        val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val defaultSleepHours = prefs.getInt("sleep_hours", 8) // Default to 8 normal hours if not set
        val selectedDefense = lastLineOfDefenseSpinner.selectedItem.toString()
        Log.d("FirebasePhone", "lastLineOfDefenseSpinner selected: $selectedDefense")
        prefs.edit().putString("last_line_of_defense", selectedDefense).apply()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, defaultSleepHours)
        scheduleAlarm(calendar.timeInMillis)
        val alarmTime = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
        Toast.makeText(this, "Alarm set for $alarmTime (${defaultSleepHours} hours from now)", Toast.LENGTH_SHORT).show()

        // start Firebase BPM listener if Wearable from spinner is selected
        if (selectedDefense == "Wearable" && !isFirebaseBpmListenerActive) {
            Log.d("FirebasePhone", "Starting Firebase BPM listener for userId=$userId")
            Toast.makeText(this, "Starting Firebase BPM listener", Toast.LENGTH_SHORT).show()
            try {
                Log.d("FirebasePhone", "Calling startFirebaseBpmListener in MainActivity4")
                WearableListenerService.startFirebaseBpmListener(userId, 100) {
                    runOnUiThread {
                        Toast.makeText(this, "BPM > 100 – alarma se oprește!", Toast.LENGTH_LONG).show()
                        AlarmReceiver.stopAlarm()
                        stopFirebaseBpmListenerIfActive()
                    }
                }
            } catch (e: Exception) {
                Log.e("FirebasePhone", "Exception when starting Firebase BPM listener", e)
            }
            isFirebaseBpmListenerActive = true
        }
    }

    private fun stopFirebaseBpmListenerIfActive() {
        if (isFirebaseBpmListenerActive) {
            WearableListenerService.stopFirebaseBpmListener()
            isFirebaseBpmListenerActive = false
        }
    }

    //shoes avalaible options for alarm handling cancel/snooze
    private fun showAlarmOptionsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alarm Options")
        val options = arrayOf("Stop Alarm", "Snooze")
        builder.setSingleChoiceItems(options, -1) { dialog, which ->
            when (which) {
                0 -> {
                    dialog.dismiss()
                    showPasswordDialog()
                    stopFirebaseBpmListenerIfActive()
                }
                1 -> {
                    dialog.dismiss()
                    snoozeAlarm()
                    stopFirebaseBpmListenerIfActive()
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    //snooze option
    private fun snoozeAlarm() {
        val selectedSnoozeTime = snoozeTimeSpinner.selectedItem.toString().toInt()
        AlarmReceiver.stopAlarm()
        Toast.makeText(this, "Alarm snoozed for $selectedSnoozeTime minutes", Toast.LENGTH_SHORT).show()
        snoozeHandler?.removeCallbacks(snoozeRunnable!!)
        snoozeHandler = Handler(Looper.getMainLooper())
        snoozeRunnable = Runnable {
            val intent = Intent(this, AlarmReceiver::class.java)
            sendBroadcast(intent)
            Toast.makeText(this, "Snooze time is up!", Toast.LENGTH_SHORT).show()
        }
        snoozeHandler?.postDelayed(snoozeRunnable!!, (selectedSnoozeTime * 60 * 1000).toLong())
    }

    //Dialog with stop alarm options
    private fun showPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Last Line of Defense")
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

    //stop alarm by PIN
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
    //2 types of questions: math and color theory
    private fun showPuzzle() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Solve the Puzzle to Stop Alarm")

        // Randomly choose between puzzles
        val puzzleType = (0..1).random()
        
        if (puzzleType == 0) {
            // Simple Math puzzle
            val num1 = (10..30).random()
            val num2 = (10..25).random()
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
            // color theory puzzle
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
                    // Puzzle solved stop the alarm
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

    //game puzzle
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

                scheduleAlarm(calendar.timeInMillis)

                val chosenTime = String.format("%02d:%02d", hourOfDay, minute)
                Toast.makeText(this, "Alarm set for $chosenTime", Toast.LENGTH_SHORT).show()

                // Start Firebase BPM listener only if Wearable from spinner is selected
                val selectedDefense = lastLineOfDefenseSpinner.selectedItem.toString()
                Log.d("FirebasePhone", "lastLineOfDefenseSpinner selected (time picker): $selectedDefense")
                // Save last line of defense to SharedPreferences
                val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                prefs.edit().putString("last_line_of_defense", selectedDefense).apply()
                if (selectedDefense == "Wearable" && !isFirebaseBpmListenerActive) {
                    Log.d("FirebasePhone", "Starting Firebase BPM listener for userId=$userId (time picker)")
                    Toast.makeText(this, "Starting Firebase BPM listener", Toast.LENGTH_SHORT).show()
                    try {
                        Log.d("FirebasePhone", "Calling startFirebaseBpmListener in MainActivity4")
                        WearableListenerService.startFirebaseBpmListener(userId, 100) {
                            runOnUiThread {
                                Toast.makeText(this, "BPM > 100 – alarma se oprește!", Toast.LENGTH_LONG).show()
                                AlarmReceiver.stopAlarm()
                                stopFirebaseBpmListenerIfActive()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("FirebasePhone", "Exception when starting Firebase BPM listener", e)
                    }
                    isFirebaseBpmListenerActive = true
                }
            },
            currentHour,
            currentMinute,
            true
        )
        timePickerDialog.show()
    }

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
