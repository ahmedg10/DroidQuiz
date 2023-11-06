package edu.uw.ischool.uw2065357.droidquiz

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.topicListView)

        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            QuizDataMock.quizDataList.map { it.quizTitle }
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedQuizData = QuizDataMock.quizDataList[position]
            val jsonQuizData = selectedQuizData.toJson()

            val intent = Intent(this, Overview::class.java)
            intent.putExtra("quizData", jsonQuizData)
            intent.putExtra("overview", selectedQuizData.quizOverview)
            intent.putExtra("quizTitle", selectedQuizData.quizTitle)

            startActivity(intent)

        }

    }
}