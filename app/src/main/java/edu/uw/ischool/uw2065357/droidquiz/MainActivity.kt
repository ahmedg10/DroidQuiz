package edu.uw.ischool.uw2065357.droidquiz

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity




class MainActivity : AppCompatActivity() {
    private lateinit var topicRepository: QuizTopicRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the repository
        topicRepository = MemoryQuizRepository(this)

        val listView = findViewById<ListView>(R.id.topicListView)

        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            topicRepository.getAllTopics().map { it.quizTitle }
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedTopic = topicRepository.getAllTopics()[position]

            val intent = Intent(this, Overview::class.java)
            intent.putExtra("quizTitle", selectedTopic.quizTitle)
            // You can add other necessary data as extras if needed

            startActivity(intent)
        }
    }
}