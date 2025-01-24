package com.example.smamysuperalarm.activities

//import kotlinx.android.synthetic.main.activity_main.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smamysuperalarm.R
import com.example.smamysuperalarm.data.Model_Person
import com.example.smamysuperalarm.data.PersonDatabase
import com.example.smamysuperalarm.databinding.ActivityMain1Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity1 : AppCompatActivity() {
    private lateinit var db: PersonDatabase
    private lateinit var binding: ActivityMain1Binding
    //private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)
        //binding = ActivityMain1Binding.inflate(layoutInflater)
        //sheetLayoutBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
        //dialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
        //dialog.setContentView(sheetLayoutBinding.root)
        //setContentView(binding.root)
        //sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        db = PersonDatabase.getDb(this)
        val editTextUsername = findViewById<EditText>(R.id.name)
        val editTextAge = findViewById<EditText>(R.id.age)
        val editTextPassword = findViewById<EditText>(R.id.code)
        val buttonLogin = findViewById<Button>(R.id.button1)

        //val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        //val hasSeenWelcome = prefs.getBoolean("hasSeenWelcome", false)
        //nu arata pagina 1 daca a fost vizitata deja
        /*if (hasSeenWelcome) {
            startActivity(Intent(this, MainActivity2::class.java))
            finish()
            return
        }*/

        /*val continueButton: Button = findViewById(R.id.button1)
        continueButton.setOnClickListener {
            prefs.edit().putBoolean("hasSeenWelcome", true).apply()

            startActivity(Intent(this, MainActivity2::class.java))
            finish()
        }*/
        //enableEdgeToEdge()
        // setContentView(R.layout.activity_main)
        buttonLogin.setOnClickListener {

            val username = editTextUsername.text.toString().trim()
            val age = editTextAge.text.toString().trim()
            val code = editTextPassword.text.toString().trim()
            if (username.isNotEmpty() && age<"99" && code.isNotEmpty()) {
               lifecycleScope.launch(Dispatchers.IO) {
                    val user = Model_Person(nume = username, code = code)
                    db.personDao().addPerson(user)}
                val intent = Intent(this, MainActivity3::class.java)
                startActivity(intent)
            } else
            {
                Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
            }

        }

    }
}
