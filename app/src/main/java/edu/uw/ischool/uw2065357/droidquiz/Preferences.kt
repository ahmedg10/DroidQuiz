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

        // Retrieve saved preferences using AppPreferences
        val appPreferences = AppPreferences.getInstance()
        val savedUrl = appPreferences.getUrl()
        val savedMinutes = appPreferences.getMinutes()

        // Set EditText values
        prefUrlEditText.setText(savedUrl)
        prefTimeEditText.setText(savedMinutes.toString())

        // Save button click listener
        saveButton.setOnClickListener {
            // Update AppPreferences with new values
            appPreferences.setUrl(prefUrlEditText.text.toString())
            appPreferences.setMinutes(prefTimeEditText.text.toString().toInt())

            // Start MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}




