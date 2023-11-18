package edu.uw.ischool.uw2065357.droidquiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class Preferences : AppCompatActivity() {

    private lateinit var prefUrlEditText: EditText
    private lateinit var prefTimeEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var actionBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.prefrence)

        // Set up the ActionBar
        actionBar = findViewById(R.id.preferenceToolbar)
        setSupportActionBar(actionBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Initialize UI elements
        prefUrlEditText = findViewById(R.id.InsertPrefURL)
        prefTimeEditText = findViewById(R.id.InsertPrefTime)
        saveButton = findViewById(R.id.savePreferenceButton)

        // Get SharedPreferences instance and Editor
        val sharedPrefs = getSharedPreferences("AppPreference", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        // Retrieve saved preferences
        val savedUrl = sharedPrefs.getString("url", "http://tednewardsandbox.site44.com/questions.json")
        val savedMinutes = sharedPrefs.getInt("minutes", 0)

        // Set EditText values
        prefUrlEditText.setText(savedUrl)
        prefTimeEditText.setText(savedMinutes.toString())

        // Save button click listener
        saveButton.setOnClickListener {
            // Update SharedPreferences with new values
            editor.putString("url", prefUrlEditText.text.toString())
            editor.putInt("minutes", prefTimeEditText.text.toString().toInt())
            editor.apply()

            // Start MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}



