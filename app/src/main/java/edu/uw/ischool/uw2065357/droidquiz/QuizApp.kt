package edu.uw.ischool.uw2065357.droidquiz
import android.app.Application
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
}