package com.example.smamysuperalarm.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smamysuperalarm.R
import com.example.smamysuperalarm.database.AppDatabase
import com.example.smamysuperalarm.repository.UserRepository
import com.example.smamysuperalarm.databinding.ActivityMain5Binding
import kotlinx.coroutines.launch

class MainActivity5 : AppCompatActivity() {
    private lateinit var binding5: ActivityMain5Binding
    private lateinit var userRepository: UserRepository
    private lateinit var sleepHoursSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding5 = ActivityMain5Binding.inflate(layoutInflater)
        setContentView(binding5.root)

        // Initialize Room database and repository
        val database = AppDatabase.getDatabase(applicationContext)
        userRepository = UserRepository(database.userDao())

        // Initialize spinner
        sleepHoursSpinner = findViewById(R.id.sleep_hours_spinner)

        // Set up PIN change functionality
        binding5.changePinText.setOnClickListener {
            showChangePinDialog()
        }

        // Load current sleep hours and set spinner
        loadCurrentSleepHours()

        // Set up spinner change listener
        sleepHoursSpinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedHours = getHoursFromSpinnerSelection(position)
                saveSleepHours(selectedHours)
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                // Do nothing
            }
        })

        binding5.done.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }
    }

    private fun loadCurrentSleepHours() {
        val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val currentSleepHours = prefs.getInt("sleep_hours", 8) // Default to 8 hours
        
        // Set spinner to current value
        val spinnerPosition = getSpinnerPositionFromHours(currentSleepHours)
        sleepHoursSpinner.setSelection(spinnerPosition)
    }

    private fun getHoursFromSpinnerSelection(position: Int): Int {
        return when (position) {
            0 -> 8  // 8H
            1 -> 7  // 7H
            2 -> 6  // 6.5H (rounding to 6 for simplicity)
            else -> 8
        }
    }

    private fun getSpinnerPositionFromHours(hours: Int): Int {
        return when (hours) {
            8 -> 0
            7 -> 1
            6 -> 2
            else -> 0 // Default to 8H
        }
    }

    private fun saveSleepHours(hours: Int) {
        val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val username = prefs.getString("username", "") ?: ""

        if (username.isNotEmpty()) {
            lifecycleScope.launch {
                try {
                    userRepository.updateSleepHours(username, hours)
                    prefs.edit().putInt("sleep_hours", hours).apply()
                    Toast.makeText(this@MainActivity5, "Sleep hours updated to ${hours}H", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity5, "Error updating sleep hours: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // If no username found, just update SharedPreferences
            prefs.edit().putInt("sleep_hours", hours).apply()
            Toast.makeText(this, "Sleep hours updated to ${hours}H", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showChangePinDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Change PIN")

        // Create input fields
        val input = EditText(this).apply {
            hint = "Enter new PIN"
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        builder.setView(input)

        builder.setPositiveButton("Change PIN") { dialog, _ ->
            val newPin = input.text.toString().trim()
            
            if (newPin.isEmpty()) {
                Toast.makeText(this, "PIN cannot be empty", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            if (newPin.length < 4) {
                Toast.makeText(this, "PIN must be at least 4 characters", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            // Get current username from SharedPreferences
            val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val username = prefs.getString("username", "") ?: ""

            if (username.isEmpty()) {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            // Update PIN in database
            lifecycleScope.launch {
                try {
                    userRepository.updatePassword(username, newPin)
                    
                    // Also update SharedPreferences for backward compatibility
                    prefs.edit().putString("alarm_code", newPin).apply()
                    
                    Toast.makeText(this@MainActivity5, "PIN changed successfully", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity5, "Error changing PIN: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}