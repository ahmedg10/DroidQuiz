package edu.uw.ischool.uw2065357.droidquiz
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log

class QuizApp : Application() {
    val topicRepository: MemoryQuizRepository by lazy {
        MemoryQuizRepository(this)
    }


    override fun onCreate() {
        super.onCreate()
        AppPreferences.initialize(applicationContext)
        Log.d("QuizApp", "Quiz App is being loaded and run")
    }

    // Define a function to create notification channels
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "download_channel_id"
            val channelName = "Download Channel"
            val channelDescription = "Channel for download notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}