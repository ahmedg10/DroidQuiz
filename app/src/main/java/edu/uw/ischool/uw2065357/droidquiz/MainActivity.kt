package edu.uw.ischool.uw2065357.droidquiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity




class MainActivity : AppCompatActivity() {
    private lateinit var topicRepository: QuizTopicRepository
    private lateinit var mainToolBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainToolBar = findViewById(R.id.maintoolbar)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)

        setSupportActionBar(mainToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Hide the default title
        toolbarTitle.text = "QuizDroid" // Set your custom title

        // Initialize the repository
        topicRepository = MemoryQuizRepository(this)

        Log.d("Debug Activty", "${topicRepository.getAllTopics()}")

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.preference_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_preference -> {
                // Handle Save button click
                val intent = Intent(this, Preferences::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}