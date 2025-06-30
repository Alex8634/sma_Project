package com.example.smamysuperalarm.activities

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.smamysuperalarm.R
import com.example.smamysuperalarm.service.AlarmReceiver

class GameActivity : AppCompatActivity() {
    private lateinit var targetCircle: ImageView
    private var score = 0
    private val requiredScore = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        targetCircle = findViewById(R.id.target_circle)
        startGame()
    }

    private fun startGame() {
        moveCircle()
        targetCircle.setOnClickListener {
            score++
            if (score >= requiredScore) {
                // Game won
                AlarmReceiver.stopAlarm()
                finish()
            } else {
                moveCircle()
            }
        }
    }
//move circle by XYaxis
    private fun moveCircle() {
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val randomX = (0..(screenWidth - targetCircle.width)).random()
        val randomY = (0..(screenHeight - targetCircle.height)).random()
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 500
        animator.interpolator = LinearInterpolator()
        val startX = targetCircle.x
        val startY = targetCircle.y
        
        animator.addUpdateListener { animation ->
            val fraction = animation.animatedValue as Float
            targetCircle.x = startX + (randomX - startX) * fraction
            targetCircle.y = startY + (randomY - startY) * fraction
        }
        animator.start()
    }
} 