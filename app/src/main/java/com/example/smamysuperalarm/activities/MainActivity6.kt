package com.example.smamysuperalarm.activities

import android.app.Dialog
import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.smamysuperalarm.R
import com.example.smamysuperalarm.databinding.ActivityMain6Binding
import com.example.smamysuperalarm.utils.Util.setupDialog

class MainActivity6 : AppCompatActivity() {
    private val binding6: ActivityMain6Binding by lazy{
        ActivityMain6Binding.inflate(layoutInflater)
    }
    private val timeDialogLayout: Dialog by lazy{
        Dialog(this, R.style.DialogCustomTheme).apply { setupDialog(R.layout.time_dialog_layout) }
    }
   // val okButton: Button = Dialog.findViewById(R.id.ok_button)
    //val cancelButton: Button = Dialog.findViewById(R.id.backDiag)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding6 = ActivityMain6Binding.inflate(layoutInflater)
        setContentView(binding6.root)

        binding6.back6.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        binding6.addSalarm.setOnClickListener {
            val intent = Intent(this, MainActivity4::class.java)
            startActivity(intent)
        }

        binding6.addAlarm.setOnClickListener {
            val intent = Intent(this, MainActivity4::class.java)
            startActivity(intent)
        }
        
        binding6.addAlarm.setOnClickListener{
            timeDialogLayout.show();
        }

        /*timeDialogLayout.back.setOnClickListener{
            timeDialogLayout.dismiss();
        }*/
    }
}