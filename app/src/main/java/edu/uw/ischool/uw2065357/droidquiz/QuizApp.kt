package edu.uw.ischool.uw2065357.droidquiz
import android.app.Application
import android.util.Log

class QuizApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("QuizApp", "Quiz App is being loaded and run")
    }
}