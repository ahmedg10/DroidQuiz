package edu.uw.ischool.uw2065357.droidquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Overview : AppCompatActivity() {
    private val quizDataList = ArrayList<QuizData>() // Initialize an empty ArrayList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)


        // Retrieve the quiz data and overview from the intent
        val quizTitle = intent.getStringExtra("quizTitle")
        val overview = intent.getStringExtra("overview")
        // Retrieve the jsonQuizData from the intent
        val jsonQuizData = intent.getStringExtra("quizData")

        val overviewTextView = findViewById<TextView>(R.id.overview_title)
        val descriptionTextView = findViewById<TextView>(R.id.overiew_description)

        overviewTextView.text = "$quizTitle Quiz Overview" // Set the quiz title
        descriptionTextView.text = overview // Set the overview text

        val beginButton = findViewById<Button>(R.id.beginButton)
        beginButton.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)

            // Pass the quiz data as JSON to the QuizActivity
            intent.putExtra("quizData", jsonQuizData)

            startActivity(intent)
        }
    }
}