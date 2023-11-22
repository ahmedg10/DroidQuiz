package edu.uw.ischool.uw2065357.droidquiz

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {
    private lateinit var topicRepository: MemoryQuizRepository
    private lateinit var mainToolBar: Toolbar
    private lateinit var quizApp: QuizApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quizApp = application as QuizApp
        topicRepository = quizApp.topicRepository
        topicRepository.setDataLoadedListener(object : DataLoadedListener {
            override fun onDataLoaded() {
                updateUI()
            }
        })

        mainToolBar = findViewById(R.id.maintoolbar)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)

        setSupportActionBar(mainToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Hide the default title
        toolbarTitle.text = "QuizDroid" // Set your custom title

        // Initialize the repository
        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute {
            topicRepository.pullJsonFromUrl(AppPreferences.getInstance().getUrl())
        }

        val downloadMinutes = AppPreferences.getInstance().getMinutes()

        download(downloadMinutes)

    }

    private fun updateUI() {
        runOnUiThread {
            // Update the ListView adapter with new data
            val listView = findViewById<ListView>(R.id.topicListView)
            val adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                topicRepository.getAllTopics().map { it.quizTitle }
            )
            listView.adapter = adapter

            // Set the OnItemClickListener for the ListView
            listView.setOnItemClickListener { _, _, position, _ ->
                val selectedTopic = topicRepository.getAllTopics()[position]
                val intent = Intent(this, Overview::class.java)
                intent.putExtra("quizTitle", selectedTopic.quizTitle)
                startActivity(intent)
            }
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

     fun download(downloadMinutes : Int) {
        val intent = Intent(this, DownloadService::class.java)
        DownloadService.enqueueWork(this, intent)

        // Set up AlarmManager for periodic downloads
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent =
            PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val triggerTime = SystemClock.elapsedRealtime() + downloadMinutes * 60 * 1000
            alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                (downloadMinutes * 60 * 1000).toLong(),
                pendingIntent
            )
    }
}