package edu.uw.ischool.uw2065357.droidquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


class Overview : AppCompatActivity() {
    private lateinit var topicRepository: QuizTopicRepository
    private lateinit var selectedTopic: QuizData

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        topicRepository = MemoryQuizRepository()

        val selectedTopicTitle = intent.getStringExtra("quizTitle")

        // Retrieve the selected topic from the repository
        selectedTopic = selectedTopicTitle?.let { topicRepository.getTopicByTitle(it) }
            ?: run {
                showToast("Selected topic not found!")
                finish() // Finish the activity or navigate to another appropriate screen
                return
            }

        // Retrieve the overview from the repository
        val overview = selectedTopic.quizShortOverview

        val overviewTextView = findViewById<TextView>(R.id.overview_title)
        val descriptionTextView = findViewById<TextView>(R.id.overiew_description)

        overviewTextView.text = "${selectedTopic.quizTitle} Quiz Overview" // Set the quiz title
        descriptionTextView.text = overview // Set the overview text

        val beginButton = findViewById<Button>(R.id.beginButton)
        beginButton.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)

            // Pass the selected topic to the QuizActivity
            intent.putExtra("selectedTopic", selectedTopic.quizTitle)

            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}