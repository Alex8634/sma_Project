package com.example.smamysuperalarm

import android.Manifest
//import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smamysuperalarm.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //sheetLayoutBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
        //dialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
        //dialog.setContentView(sheetLayoutBinding.root)
        setContentView(binding.root)

       //enableEdgeToEdge()
        //setContentView(R.layout.activity_main)


        binding.button1.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }
}
